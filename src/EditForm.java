import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class EditForm {
    public JPanel editPanel;
    private JTextField txtName;
    private JTextField txtPrice;
    private JTextField txtDesc;
    private JButton btnEdit;
    private JButton btnDelete;
    private JButton btnBack;
    private JButton btnSelectImage;
    private JTable table1;
    private JLabel lblImagePreview;
    private DefaultTableModel tableModel;
    private int selectedProductId = -1;
    private CRUD crud;

    public EditForm(JFrame frame) {
        crud = new CRUD();

        // Configurar la tabla
        String[] columnHeaders = {"ID", "Nombre", "Descripcion", "Precio", "Imagen"};
        tableModel = new DefaultTableModel(columnHeaders, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return (columnIndex == 4) ? ImageIcon.class : Object.class; // Para la imagen, usamos ImageIcon
            }
        };

        table1.setModel(tableModel);
        table1.setRowHeight(50); // Ajustamos el alto de la fila para mostrar imágenes
        table1.getColumn("Imagen").setCellRenderer(new ImageRenderer()); // Renderizador personalizado

        cargarProductos();

        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table1.getSelectedRow();
                if (row >= 0) {
                    selectedProductId = (int) tableModel.getValueAt(row, 0);
                    txtName.setText((String) tableModel.getValueAt(row, 1));
                    txtDesc.setText((String) tableModel.getValueAt(row, 2));
                    txtPrice.setText(String.valueOf(tableModel.getValueAt(row, 3)));
                }
            }
        });

        btnEdit.addActionListener(e -> editarProducto());
        btnDelete.addActionListener(e -> eliminarProducto());

        btnBack.addActionListener(e -> {
            frame.setContentPane(new AdminForm(frame).adminPanel);
            frame.revalidate();
            frame.repaint();
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

    private void editarProducto() {
        if (selectedProductId == -1) {
            JOptionPane.showMessageDialog(null, "Selecciona un producto para editar");
            return;
        }

        try {
            String nuevoNombre = txtName.getText();
            String nuevaDescripcion = txtDesc.getText();
            double nuevoPrecio = Double.parseDouble(txtPrice.getText());

            crud.editarProducto(selectedProductId, nuevoNombre, nuevaDescripcion, nuevoPrecio);
            JOptionPane.showMessageDialog(null, "Producto actualizado correctamente");

            cargarProductos(); // Recargar la tabla
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Error: Ingresa un precio válido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarProducto() {
        if (selectedProductId == -1) {
            JOptionPane.showMessageDialog(null, "Selecciona un producto para eliminar");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(null, "¿Seguro que deseas eliminar este producto?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            crud.eliminarProducto(selectedProductId);
            JOptionPane.showMessageDialog(null, "Producto eliminado correctamente");
            cargarProductos();
        }
    }

    static class ImageRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof ImageIcon) {
                JLabel label = new JLabel((ImageIcon) value);
                label.setHorizontalAlignment(JLabel.CENTER);
                return label;
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }
}

