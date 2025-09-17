import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    //declaramos una constante para la URL de conexion a la base de datos
    // static significa que pertenece a la clase, no a instancias
    // final significa que su valor no puede cambiar despues de inicializarse
    // ysamos el porotocolo jdbc:mysql:// para indicar que es una conexion a MySQL
    private static final String URL = "jdbc:mysql://bjzjysoocubt3wxoo9wh-mysql.services.clever-cloud.com:3306/bjzjysoocubt3wxoo9wh?useSSL=false&serverTimezone=UTC";
    private static final String USER = "uckcbzgbpi9xcrbe";
    private static final String PASS = "gKGS0svGCMCTSGLwBmuh";

    static {//Cuando se pone static se ejecuta una sola vez al cargar la clase
        try {
            // aqui cargamos la clase del driver JDBC para MySQL
            Class.forName("com.mysql.cj.jdbc.Driver"); }
        catch (ClassNotFoundException e) {
            // si el driver no se encuentra, lanzamos una excepcion en tiempo de ejecucion
            throw new RuntimeException("Driver MySQL no encontrado en el classpath", e);
        }
    }
// cremos un metodo estatico  y publico para obtener la conexion a la base de datos
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
        // usamos DriverManager para obtener la conexion
    }
}