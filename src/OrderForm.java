import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class OrderForm {
    public JPanel userPanel;
    private JTable table1;
    private JTextField txtName;
    private JButton btnAddCart;
    private JButton btnCart;
    private DefaultTableModel tableModel;
    private JTextField txtCantidad;
    private JLabel lblTotal;
    private int selectedProductId = -1;
    private CRUD crud;

    public OrderForm(JFrame frame) {
        txtName.setEditable(false);
        crud = new CRUD();
        String[] columnHeaders = {"ID", "Nombre", "Descripcion", "Precio", "Imagen"};
        tableModel = new DefaultTableModel(columnHeaders, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Deshabilita la edición de todas las celdas
            }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return (columnIndex == 4) ? ImageIcon.class : Object.class; // Para la imagen, usamos ImageIcon
            }
        };

        table1.setModel(tableModel);
        table1.setRowHeight(50); // Ajustamos el alto de la fila para mostrar imágenes
        table1.getColumn("Imagen").setCellRenderer(new EditForm.ImageRenderer()); // Renderizador personalizado

        cargarProductos();

        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table1.getSelectedRow();
                if (row >= 0) {
                    selectedProductId = (int) tableModel.getValueAt(row, 0);
                    txtName.setText((String) tableModel.getValueAt(row, 1));
                }
            }
        });

        btnAddCart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedProductId == -1) {
                    JOptionPane.showMessageDialog(null, "Seleccione un producto de la tabla.");
                    return;
                }

                String cantidadTexto = txtCantidad.getText().trim();
                if (cantidadTexto.isEmpty() || !cantidadTexto.matches("\\d+")) {
                    JOptionPane.showMessageDialog(null, "Ingrese una cantidad válida.");
                    return;
                }

                int cantidad = Integer.parseInt(cantidadTexto);
                if (cantidad <= 0) {
                    JOptionPane.showMessageDialog(null, "La cantidad debe ser mayor a 0.");
                    return;
                }

                // Obtener el usuario (en este ejemplo, se asume un ID fijo, debes cambiarlo según tu lógica)
                int userId = SessionManager.getUserId();
                if (userId == -1) {
                    JOptionPane.showMessageDialog(null, "Error: Usuario no autenticado.");
                    return;
                }

                // Llamar al método para agregar al carrito
                if (crud.agregarAlCarrito(userId, selectedProductId, cantidad)) {
                    JOptionPane.showMessageDialog(null, "Producto agregado al carrito.");
                } else {
                    JOptionPane.showMessageDialog(null, "Error al agregar al carrito.");
                }
            }
        });

        btnCart.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setContentPane(new CartForm(frame).cartPanel);
                frame.revalidate();
                frame.repaint();
            }
        });
    }
    private void cargarProductos() {
        tableModel.setRowCount(0);
        List<Product> productos = crud.obtenerProductos();

        for (Product p : productos) {
            tableModel.addRow(new Object[]{
                    p.getId(), p.getName(), p.getDescription(), p.getPrice(), p.getImageIcon()
            });
        }
    }

}
