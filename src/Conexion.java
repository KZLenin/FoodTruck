import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:postgresql://pg-3451b877-bd-foodtruck.c.aivencloud.com:15972/db-restaurant?ssl=require&user=avnadmin&password=AVNS_4KzKkga0p_rnDDsOjK7";

    public void conectar() {
        try (Connection connection = DriverManager.getConnection(URL)) {
            System.out.println("Conexión realizada exitosamente.");
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos.");
            e.printStackTrace();
        }
    }
}