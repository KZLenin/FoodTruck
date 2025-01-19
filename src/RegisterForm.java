import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterForm {
    public JPanel registerPanel;
    private JTextField txtName;
    private JTextField txtAdress;
    private JTextField txtTelefono;
    private JTextField txtEmail;
    private JPasswordField pswContra;
    private JButton btnRegister;

    public RegisterForm(JFrame frame) {
        // Inicialmente, deshabilitamos el botón de registro
        btnRegister.setEnabled(false);

        // Agregamos un DocumentListener a los campos de texto para detectar cambios
        txtName.getDocument().addDocumentListener(new DocumentListener() {
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
        });

        txtAdress.getDocument().addDocumentListener(new DocumentListener() {
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
        });

        txtTelefono.getDocument().addDocumentListener(new DocumentListener() {
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
        });

        txtEmail.getDocument().addDocumentListener(new DocumentListener() {
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
        });

        pswContra.getDocument().addDocumentListener(new DocumentListener() {
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
        });

        // Agregar ActionListener al botón de registro
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = txtName.getText();
                String direccion = txtAdress.getText();
                String telefono = txtTelefono.getText();
                String correo = txtEmail.getText();
                String contraseña = new String(pswContra.getPassword());

                LoginRegister crud = new LoginRegister();

                if (crud.verificacion(correo, telefono)) {
                    JOptionPane.showMessageDialog(frame,
                            "El correo o teléfono ya están registrados. Intente con otros datos.",
                            "Error de Registro",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    // Registrar usuario si la verificación fue exitosa
                    crud.registrarUsuario(nombre, direccion, telefono, correo, contraseña);
                    JOptionPane.showMessageDialog(frame,
                            "Usuario registrado exitosamente.",
                            "Registro Exitoso",
                            JOptionPane.INFORMATION_MESSAGE);

                    // Cambiar al formulario de inicio de sesión
                    frame.setContentPane(new LoginForm(frame).loginPanel);
                    frame.revalidate(); // Refrescar el JFrame
                    frame.repaint();
                }
            }
        });
    }

    // Método para verificar si todos los campos están completos
    private void checkFields() {
        String nombre = txtName.getText();
        String direccion = txtAdress.getText();
        String telefono = txtTelefono.getText();
        String correo = txtEmail.getText();
        String contraseña = new String(pswContra.getPassword());

        // Habilitar el botón solo si todos los campos están llenos
        btnRegister.setEnabled(!nombre.isEmpty() && !direccion.isEmpty() && !telefono.isEmpty() && !correo.isEmpty() && !contraseña.isEmpty());
    }
}
