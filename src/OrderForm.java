import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class OrderForm {
    public JPanel userPanel;
    private JTable table1;
    private JTextField txtId;
    private JTextField txtName;
    private JButton btnAddCart;
    private JButton btnCart;
    private JTextField txtCantidad;

    public OrderForm(JFrame frame) {
    }

    private void createUIComponents() {
        String[] columnHeaders = {"ID", "Name", "Price", "Image"};
        DefaultTableModel tableModel = new DefaultTableModel(columnHeaders, 0);
        table1 = new JTable(tableModel);
    }
}
