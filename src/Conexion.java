import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Conexion {
    private static String url;
    private static String user;
    private static String password;

    static {
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            Properties properties = new Properties();
            properties.load(fis);
            url = properties.getProperty("db.url");
            user = properties.getProperty("db.user");
            password = properties.getProperty("db.password");
        } catch (IOException e) {
            System.err.println("Error al cargar el archivo de configuración.");
            e.printStackTrace();
        }
    }

    public void conectar() {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            System.out.println("Conexión realizada exitosamente.");
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos.");
            e.printStackTrace();
        }
    }
}
