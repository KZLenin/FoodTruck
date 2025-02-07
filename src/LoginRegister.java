import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
        String contraseñaEncriptada = encriptarSHA256(contraseña);
        String query = "INSERT INTO usuarios(nombre, direccion, telefono, correo, contraseña) VALUES (?,?,?,?,?)";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, nombre);
            ps.setString(2, direccion);
            ps.setString(3, telefono);
            ps.setString(4, correo);
            ps.setString(5, contraseñaEncriptada);
            ps.executeUpdate();
            System.out.println("Usuario insertado exitosamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public int loginUsuario(String correo, String password) {
        String passwordEncriptada = encriptarSHA256(password);
        String query = "SELECT id FROM usuarios WHERE correo = ? AND contraseña = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, correo);
            ps.setString(2, passwordEncriptada);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id"); // Retorna el ID del usuario autenticado
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Retorna -1 si las credenciales no son válidas
    }


    private String encriptarSHA256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al encriptar la contraseña", e);
        }
    }
}
