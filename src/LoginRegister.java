
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginRegister {
    public boolean verificacion(String correo, String telefono) {
        String query = "SELECT COUNT(*) FROM usuarios WHERE correo = ? OR telefono = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, correo);
            ps.setString(2, telefono);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Retorna true si ya existe un registro
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Retorna false si no se encuentra duplicado
    }

    // Metodo para insertar datos
    public void registrarUsuario(String nombre, String direccion, String telefono, String correo, String contraseña) {
        String query = "INSERT INTO usuarios(nombre, direccion, telefono, correo, contraseña) VALUES (?,?,?,?,?)";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, nombre);
            ps.setString(2, direccion);
            ps.setString(3, telefono);
            ps.setString(4, correo);
            ps.setString(5, contraseña);
            ps.executeUpdate();
            System.out.println("Usuario insertado exitosamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean loginUsuario(String correo, String password) {
        String query = "SELECT COUNT(*) FROM usuarios WHERE correo = ? AND contraseña = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, correo);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Retorna true si las credenciales son correctas
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Retorna false si no se encuentran las credenciales
    }
}
