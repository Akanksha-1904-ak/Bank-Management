import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/bankdb";
    private static final String USER = "root";   // replace with your MySQL username
    private static final String PASSWORD = "Akanksha@123"; // replace with your MySQL password

    public static Connection getConnection() {
    try {
        Class.forName("com.mysql.cj.jdbc.Driver"); // 👈 Add this line
        return DriverManager.getConnection(URL, USER, PASSWORD);
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}

}
