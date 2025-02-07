import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CartForm {
    public JPanel cartPanel;
    private JTable table1;
    private JTextField txtSubTotal; // ID de usuario
    private JTextField txtTotal; // No se usa en este caso
    private JButton btnPayment;
    private DefaultTableModel tableModel;
    private CRUD crud;

    public CartForm(JFrame frame) {
        crud = new CRUD();
        txtSubTotal.setEditable(false);
        txtTotal.setEditable(false);

        String[] columnHeaders = {"ID", "Producto_ID", "Nombre","Precio", "Cantidad"};
        tableModel = new DefaultTableModel(columnHeaders, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Deshabilita la edición de todas las celdas
            }
        };

        table1.setModel(tableModel);
        table1.setRowHeight(50);

        int userId = SessionManager.getUserId();
        if (userId == -1) {
            JOptionPane.showMessageDialog(null, "Error: Usuario no autenticado.");
            return;
        }

        crud.obtenerCarrito(userId, tableModel);
        calcularTotales();

        // Acción del botón de pago
        btnPayment.addActionListener(e -> procesarPago());
    }

    private void procesarPago() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "El carrito está vacío.");
            return;
        }

        int userId = SessionManager.getUserId();
        if (userId == -1) {
            JOptionPane.showMessageDialog(null, "Error: Usuario no autenticado.");
            return;
        }


        // Calcular el total
        double subtotal = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            double precio = Double.parseDouble(tableModel.getValueAt(i, 3).toString());
            int cantidad = Integer.parseInt(tableModel.getValueAt(i, 4).toString());
            subtotal += precio * cantidad;
        }

        double iva = subtotal * 0.15;
        double envio = 2.5;
        double total = subtotal + iva + envio;

        // Registrar el pedido en la base de datos
        int orderId = crud.registrarPedido(userId, total);
        if (orderId == -1) {
            JOptionPane.showMessageDialog(null, "Error al procesar el pedido.");
            return;
        }

        // Registrar detalles del pedido
        crud.registrarDetallesPedido(orderId, tableModel);

        // Vaciar carrito después del pago
        crud.vaciarCarrito(userId);
        tableModel.setRowCount(0);
        calcularTotales(); // Actualizar los totales en la UI

        JOptionPane.showMessageDialog(null, "Pago realizado con éxito. Pedido #" + orderId + " registrado.");

    }
    private void calcularTotales() {
        double subtotal = 0;

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            double precio = Double.parseDouble(tableModel.getValueAt(i, 3).toString());
            int cantidad = Integer.parseInt(tableModel.getValueAt(i, 4).toString());
            subtotal += precio * cantidad;
        }

        double iva = subtotal * 0.15;
        double envio = 2.5;
        double total = subtotal + iva + envio;

        txtSubTotal.setText(String.format("%.2f", subtotal));
        txtTotal.setText(String.format("%.2f", total));
    }
}
