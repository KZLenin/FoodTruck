import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class AddForm {
    public JPanel addPanel;
    private JTextField txtName;
    private JTextField txtDesc;
    private JTextField txtPrice;
    private JButton btnExplore;
    private JButton btnSave;
    private JButton btnBack;
    private JLabel lblName;

    private File file;

    public AddForm(JFrame frame) {

        // Deshabilitar el botón de guardar al inicio
        btnSave.setEnabled(false);

        // Listener para el botón de retroceso
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setContentPane(new AdminForm(frame).adminPanel);
                frame.revalidate();
                frame.repaint();
            }
        });

        // Listener para seleccionar el archivo de imagen
        btnExplore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setMultiSelectionEnabled(false);
                if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                    file = fileChooser.getSelectedFile();
                    lblName.setText(file.getName());
                }
                // Verificar si todos los campos están completos y si hay una imagen
                checkFields();
            }
        });

        // Listeners para los campos de texto para habilitar el botón de guardar
        DocumentListener fieldListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checkFields();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checkFields();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                checkFields();
            }
        };

        // Agregar el DocumentListener a cada campo de texto
        txtName.getDocument().addDocumentListener(fieldListener);
        txtDesc.getDocument().addDocumentListener(fieldListener);
        txtPrice.getDocument().addDocumentListener(fieldListener);

        // Listener para el botón de guardar
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = txtName.getText();
                String desc = txtDesc.getText();
                Double price = Double.parseDouble(txtPrice.getText());
                CRUD crud = new CRUD();
                crud.insertarProductos(name, desc, price, file);
            }
        });
    }

    // Método para verificar si todos los campos están completos y si se ha seleccionado una imagen
    private void checkFields() {
        String name = txtName.getText();
        String desc = txtDesc.getText();
        String priceText = txtPrice.getText();

        // Comprobar que los campos no estén vacíos y que se haya seleccionado una imagen
        boolean allFieldsFilled = !name.isEmpty() && !desc.isEmpty() && !priceText.isEmpty();
        boolean validImageSelected = file != null && file.exists();

        // Si todos los campos están llenos y hay una imagen, habilitar el botón
        btnSave.setEnabled(allFieldsFilled && validImageSelected);
    }
}
