import java.sql.*;
import java.util.Scanner;

public class BankTransaction {
    
    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/lenden";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Accept input 
        System.out.print("Enter account number to debit from: ");
        int fromAccount = scanner.nextInt();
        
        System.out.print("Enter amount to transfer: ");
        double amount = scanner.nextDouble();
        
        // Target account for credit (fixed as 102)
        int toAccount = 102;
        
        // Perform the transaction
        performTransaction(fromAccount, toAccount, amount);
        
        scanner.close();
    }
    
    public static void performTransaction(int fromAccount, int toAccount, double amount) {
        Connection conn = null;
        PreparedStatement checkBalanceStmt = null;
        PreparedStatement debitStmt = null;
        PreparedStatement creditStmt = null;
        ResultSet rs = null;
        
        try {
            // Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Establish connection
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            
            // Disable auto-commit for transaction management
            conn.setAutoCommit(false);
            
            // Check if source account has sufficient balance
            String checkBalanceQuery = "SELECT balance FROM accounts WHERE account_number = ?";
            checkBalanceStmt = conn.prepareStatement(checkBalanceQuery);
            checkBalanceStmt.setInt(1, fromAccount);
            rs = checkBalanceStmt.executeQuery();
            
            if (!rs.next()) {
                System.out.println("Transaction Failed - Account not found");
                conn.rollback();
                return;
            }
            
            double currentBalance = rs.getDouble("balance");
            
            // Check for sufficient balance
            if (currentBalance < amount) {
                System.out.println("Transaction Failed - Insufficient balance");
                conn.rollback();
                return;
            }
            
            // Debit amount from source account
            String debitQuery = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
            debitStmt = conn.prepareStatement(debitQuery);
            debitStmt.setDouble(1, amount);
            debitStmt.setInt(2, fromAccount);
            int debitResult = debitStmt.executeUpdate();
            
            if (debitResult == 0) {
                System.out.println("Transaction Failed - Could not debit from account");
                conn.rollback();
                return;
            }
            
            // Credit amount to target account (102)
            String creditQuery = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
            creditStmt = conn.prepareStatement(creditQuery);
            creditStmt.setDouble(1, amount);
            creditStmt.setInt(2, toAccount);
            int creditResult = creditStmt.executeUpdate();
            
            if (creditResult == 0) {
                System.out.println("Transaction Failed - Could not credit to account " + toAccount);
                conn.rollback();
                return;
            }
            
            // Commit the transaction if both operations successful
            conn.commit();
            System.out.println("Transaction Successful");
            System.out.println("Amount " + amount + " transferred from account " + fromAccount + " to account " + toAccount);
            
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Database error occurred");
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback();
                    System.out.println("Transaction rolled back due to error");
                }
            } catch (SQLException rollbackEx) {
                System.out.println("Error during rollback");
                rollbackEx.printStackTrace();
            }
        } finally {
            // Close all resources in reverse order
            try {
                if (rs != null) rs.close();
                if (checkBalanceStmt != null) checkBalanceStmt.close();
                if (debitStmt != null) debitStmt.close();
                if (creditStmt != null) creditStmt.close();
                if (conn != null) {
                    conn.setAutoCommit(true); // Reset auto-commit before closing
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing resources");
                e.printStackTrace();
            }
        }
    }
}
