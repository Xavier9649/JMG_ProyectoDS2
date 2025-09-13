import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String URL = "jdbc:mysql://<HOST>:<PUERTO>/<DBNAME>?useSSL=false&serverTimezone=UTC";
    private static final String USER = "<USUARIO>";
    private static final String PASS = "<PASSWORD>";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
