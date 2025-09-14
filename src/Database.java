import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String URL = "jdbc:mysql://bjzjysoocubt3wxoo9wh-mysql.services.clever-cloud.com:3306/bjzjysoocubt3wxoo9wh?useSSL=false&serverTimezone=UTC";
    private static final String USER = "uckcbzgbpi9xcrbe";
    private static final String PASS = "gKGS0svGCMCTSGLwBmuh";

    static {
        try { Class.forName("com.mysql.cj.jdbc.Driver"); }
        catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver MySQL no encontrado en el classpath", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}