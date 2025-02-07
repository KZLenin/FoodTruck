import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RiderForm {
    public JPanel riderPanel;
    private JTable table1;
    private JButton btnSignOut;
    private JButton btnReady;
    private DefaultTableModel pedidosModel;
    private CRUD crud;

    public RiderForm(JFrame frame) {
        crud = new CRUD();

        // Configurar columnas de la tabla
        String[] columnHeaders = {"Nombre Usuario", "Dirección", "Products ID", "Nombre Producto", "Total", "Estado"};
        pedidosModel = new DefaultTableModel(columnHeaders, 0);
        table1.setModel(pedidosModel);

        cargarPedidos(); // Cargar pedidos al abrir la vista

        btnReady.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table1.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Seleccione un pedido para actualizar.");
                    return;
                }

                int orderId = (int) table1.getValueAt(selectedRow, 2); // ID del producto (relacionado con el pedido)
                String estadoActual = (String) table1.getValueAt(selectedRow, 5);

                if (!estadoActual.equals("pendiente")) {
                    JOptionPane.showMessageDialog(null, "El pedido ya fue entregado.");
                    return;
                }

                crud.actualizarEstadoPedido(orderId, "entregado");
                cargarPedidos();
            }
        });

        btnSignOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setContentPane(new LoginForm(frame).loginPanel);
                frame.revalidate();
                frame.repaint();
            }
        });
    }

    private void cargarPedidos() {
        crud.obtenerPedidosConDetalles(pedidosModel);
    }
}
