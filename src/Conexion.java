import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String url = "jdbc:postgresql://pg-3451b877-bd-foodtruck.c.aivencloud.com:15972/db-restaurant?ssl=require";
    private static final String usuario = "avnadmin";
    private static final String contraseña = "AVNS_4KzKkga0p_rnDDsOjK7";

    public static Connection conectar() throws SQLException {
        try {
            Connection con = DriverManager.getConnection(url, usuario, contraseña);
            System.out.println("Conexión realizada exitosamente.");
            return con;
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos.");
            throw e; // Re-lanzar la excepción para manejo externo si es necesario
        }
    }
}
