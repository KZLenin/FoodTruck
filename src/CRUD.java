import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class CRUD {
    public void insertarProductos(String nombre, String descripcion, double precio, File image) {
        String query = "INSERT INTO products(name, description, price, image) VALUES (?,?,?,?)";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(query)) {

            // Convertir la imagen en un arreglo de bytes
            byte[] imageBytes = null;
            if (image != null) {
                try (FileInputStream fis = new FileInputStream(image)) {
                    imageBytes = new byte[(int) image.length()];
                    fis.read(imageBytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Establecer los parámetros del PreparedStatement
            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.setDouble(3, precio);
            ps.setBytes(4, imageBytes);  // Usamos setBytes para almacenar la imagen en la base de datos

            // Ejecutar la actualización
            ps.executeUpdate();
            System.out.println("Insertado exitosamente");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Método para obtener todos los productos de la base de datos
    public List<Product> obtenerProductos() {
        List<Product> productos = new ArrayList<>();
        String query = "SELECT id, name, description, price, image FROM products"; // Agregamos 'image'

        try (Connection con = Conexion.conectar();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                byte[] imageBytes = rs.getBytes("image"); // Obtener imagen como byte[]

                productos.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        imageBytes // Ahora pasamos el byte[] como parámetro
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productos;
    }

    // Método para editar un producto
    public void editarProducto(int id, String nuevoNombre, String nuevaDescripcion, double nuevoPrecio) {
        String query = "UPDATE products SET name=?, description=?, price=? WHERE id=?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, nuevoNombre);
            ps.setString(2, nuevaDescripcion);
            ps.setDouble(3, nuevoPrecio);
            ps.setInt(4, id);
            ps.executeUpdate();
            System.out.println("Producto actualizado correctamente");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para eliminar un producto
    public void eliminarProducto(int id) {
        String query = "DELETE FROM products WHERE id=?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Producto eliminado correctamente");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean agregarAlCarrito(int userId, int productId, int cantidad) {
        String query = "INSERT INTO cart (user_id, product_id, quantity) VALUES (?, ?, ?) ";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ps.setInt(3, cantidad);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void obtenerCarrito(int userId, DefaultTableModel model) {
        String query = "SELECT c.id, c.product_id, p.name, p.price,c.quantity " +
                "FROM cart c " +
                "JOIN products p ON c.product_id = p.id " +
                "WHERE c.user_id = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                model.setRowCount(0); // Limpiar tabla antes de cargar datos

                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getInt("id"),
                            rs.getInt("product_id"),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            rs.getInt("quantity")
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int registrarPedido(int userId, double total) {
        String query = "INSERT INTO orders (user_id, total, status) VALUES (?, ?, ?) RETURNING id";
        int orderId = -1; // Inicializamos en -1 para verificar si hubo error

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, userId);
            ps.setDouble(2, total);
            ps.setString(3, "pendiente");

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                orderId = rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderId;
    }

    // Método para registrar los productos comprados en order_details
    public void registrarDetallesPedido(int orderId, DefaultTableModel model) {
        String query = "INSERT INTO order_details (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(query)) {

            for (int i = 0; i < model.getRowCount(); i++) {
                int productId = Integer.parseInt(model.getValueAt(i, 1).toString());
                int cantidad = Integer.parseInt(model.getValueAt(i, 4).toString());
                double precio = Double.parseDouble(model.getValueAt(i, 3).toString());

                ps.setInt(1, orderId);
                ps.setInt(2, productId);
                ps.setInt(3, cantidad);
                ps.setDouble(4, precio);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para vaciar el carrito después del pago
    public void vaciarCarrito(int userId) {
        String query = "DELETE FROM cart WHERE user_id = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void obtenerPedidos(DefaultTableModel model) {
        String query = "SELECT id, user_id, total, status, created_at FROM orders";

        try (Connection con = Conexion.conectar();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            model.setRowCount(0); // Limpiar la tabla antes de cargar datos

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getDouble("total"),
                        rs.getString("status"),
                        rs.getTimestamp("created_at")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void obtenerPedidosConDetalles(DefaultTableModel model) {
        String query = "SELECT u.nombre AS usuario, u.direccion, " +
                "od.product_id, p.name AS producto, o.total, o.status " +
                "FROM orders o " +
                "JOIN usuarios u ON o.user_id = u.id " +
                "JOIN order_details od ON o.id = od.order_id " +
                "JOIN products p ON od.product_id = p.id";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            model.setRowCount(0); // Limpiar la tabla antes de cargar datos

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("usuario"),
                        rs.getString("direccion"),
                        rs.getInt("product_id"),
                        rs.getString("producto"),
                        rs.getDouble("total"),
                        rs.getString("status")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Object[] obtenerDetallePedido(int orderId) {
        String query = "SELECT u.nombre AS usuario, u.direccion, " +
                "od.product_id, p.name AS producto, od.price AS precio_unitario, od.quantity AS cantidad, o.total " +
                "FROM orders o " +
                "JOIN usuarios u ON o.user_id = u.id " +
                "JOIN order_details od ON o.id = od.order_id " +
                "JOIN products p ON od.product_id = p.id " +
                "WHERE o.id = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Retornar los datos del pedido: Usuario, Dirección, Producto, Precio Unitario, Cantidad, Estado
                return new Object[]{
                        rs.getString("usuario"),
                        rs.getString("direccion"),
                        rs.getInt("product_id"),
                        rs.getString("producto"),
                        rs.getDouble("precio_unitario"),
                        rs.getInt("cantidad"),
                        rs.getString("total")
                };
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void actualizarEstadoPedido(int orderId, String nuevoEstado) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";

        try (Connection conn = Conexion.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nuevoEstado);
            pstmt.setInt(2, orderId);

            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas == 0) {
                System.out.println("No se encontró el pedido con ID: " + orderId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}




