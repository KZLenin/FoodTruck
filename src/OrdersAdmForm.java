import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OrdersAdmForm {
    public JPanel orderAdmPanel;
    private JTable table1;
    private JButton btnBack;
    private DefaultTableModel pedidosModel;
    private CRUD crud;

    public OrdersAdmForm(JFrame frame) {
        crud = new CRUD();

        String[] columnHeaders = {"ID", "Usuario ID", "Total", "Estado", "Fecha"};
        pedidosModel = new DefaultTableModel(columnHeaders, 0);
        table1.setModel(pedidosModel);

        cargarPedidos(); // Carga los pedidos al iniciar la pantalla

        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setContentPane(new AdminForm(frame).adminPanel);
                frame.revalidate();
                frame.repaint();
            }
        });
    }

    private void cargarPedidos() {
        crud.obtenerPedidos(pedidosModel);
    }
}
