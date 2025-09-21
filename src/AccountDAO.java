import java.sql.*;

public class AccountDAO {
public void createAccount(String name, double initialBalance) {
    String getNextIdQuery = "SELECT MIN(t1.account_no + 1) AS nextId " +
                            "FROM accounts t1 " +
                            "LEFT JOIN accounts t2 ON t1.account_no + 1 = t2.account_no " +
                            "WHERE t2.account_no IS NULL";

    String insertQuery = "INSERT INTO accounts (account_no, name, balance) VALUES (?, ?, ?)";

    try (Connection conn = DatabaseConnection.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(getNextIdQuery)) {

        int newId = 1; // default if table is empty
        if (rs.next() && rs.getInt("nextId") > 0) {
            newId = rs.getInt("nextId");
        }

        try (PreparedStatement ps = conn.prepareStatement(insertQuery)) {
            ps.setInt(1, newId);
            ps.setString(2, name);
            ps.setDouble(3, initialBalance);
            ps.executeUpdate();
            System.out.println("✅ Account created successfully! Account No: " + newId);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    public void deposit(int accountNo, double amount) {
        String query = "UPDATE accounts SET balance = balance + ? WHERE account_no = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setDouble(1, amount);
            ps.setInt(2, accountNo);
            int rows = ps.executeUpdate();
            if (rows > 0) System.out.println("✅ Deposit successful!");
            else System.out.println("⚠️ Account not found.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void withdraw(int accountNo, double amount) {
        String checkBalanceQuery = "SELECT balance FROM accounts WHERE account_no = ?";
        String updateQuery = "UPDATE accounts SET balance = balance - ? WHERE account_no = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement psCheck = conn.prepareStatement(checkBalanceQuery);
             PreparedStatement psUpdate = conn.prepareStatement(updateQuery)) {
            psCheck.setInt(1, accountNo);
            ResultSet rs = psCheck.executeQuery();
            if (rs.next()) {
                double balance = rs.getDouble("balance");
                if (balance >= amount) {
                    psUpdate.setDouble(1, amount);
                    psUpdate.setInt(2, accountNo);
                    psUpdate.executeUpdate();
                    System.out.println("✅ Withdrawal successful!");
                } else {
                    System.out.println("⚠️ Insufficient balance!");
                }
            } else {
                System.out.println("⚠️ Account not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void transfer(int fromAcc, int toAcc, double amount) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            PreparedStatement psCheck = conn.prepareStatement("SELECT balance FROM accounts WHERE account_no=?");
            psCheck.setInt(1, fromAcc);
            ResultSet rs = psCheck.executeQuery();

            if (rs.next() && rs.getDouble("balance") >= amount) {
                PreparedStatement withdraw = conn.prepareStatement("UPDATE accounts SET balance=balance-? WHERE account_no=?");
                withdraw.setDouble(1, amount);
                withdraw.setInt(2, fromAcc);
                withdraw.executeUpdate();

                PreparedStatement deposit = conn.prepareStatement("UPDATE accounts SET balance=balance+? WHERE account_no=?");
                deposit.setDouble(1, amount);
                deposit.setInt(2, toAcc);
                deposit.executeUpdate();

                conn.commit();
                System.out.println("✅ Transfer successful!");
            } else {
                System.out.println("⚠️ Transfer failed. Insufficient funds or account not found.");
                conn.rollback();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkBalance(int accountNo) {
        String query = "SELECT balance FROM accounts WHERE account_no = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, accountNo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("💰 Balance: " + rs.getDouble("balance"));
            } else {
                System.out.println("⚠️ Account not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void viewAllAccounts() {
        String query = "SELECT * FROM accounts";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            System.out.println("\n📋 All Accounts:");
            while (rs.next()) {
                System.out.println("Account No: " + rs.getInt("account_no") +
                        ", Name: " + rs.getString("name") +
                        ", Balance: " + rs.getDouble("balance"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void deleteAccount(int accountNo) {
    String query = "DELETE FROM accounts WHERE account_no = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(query)) {
        ps.setInt(1, accountNo);
        int rows = ps.executeUpdate();
        if (rows > 0) {
            System.out.println("✅ Account deleted successfully!");
        } else {
            System.out.println("⚠️ Account not found.");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

}
