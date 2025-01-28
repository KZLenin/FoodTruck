import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class LoginForm {
    public JPanel loginPanel;
    private JTextField txtEmail;
    private JPasswordField pswContra;
    private JButton btnLogin;
    private JButton btnRegister;

    public LoginForm(JFrame frame) {
        // Deshabilitar el botón de login al principio
        btnLogin.setEnabled(false);

        // Listener para habilitar el botón de login cuando ambos campos estén llenos
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


        // Ir al formulario de registro
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setContentPane(new RegisterForm(frame).registerPanel);
                frame.revalidate(); // Refrescar el JFrame
                frame.repaint();
            }
        });

        // Iniciar sesión
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String correo = txtEmail.getText();
                String password = new String(pswContra.getPassword()); // Obtener la contraseña como String

                LoginRegister crud = new LoginRegister();

                // Verificar si es admin
                if (correo.equals("admin") && password.equals("admin1234")) {
                    JOptionPane.showMessageDialog(frame,
                            "Acceso como Administrador",
                            "Bienvenido",
                            JOptionPane.INFORMATION_MESSAGE);

                    // Cambiar al formulario de Admin
                    frame.setContentPane(new AdminForm(frame).adminPanel);
                    frame.revalidate();
                    frame.repaint();
                } else {
                    // Verificar con la base de datos para usuarios regulares
                    if (crud.loginUsuario(correo, password)) {
                        JOptionPane.showMessageDialog(frame,
                                "Acceso como Usuario",
                                "Bienvenido",
                                JOptionPane.INFORMATION_MESSAGE);

                        // Cambiar al formulario de Usuario
                        frame.setContentPane(new OrderForm(frame).userPanel);
                        frame.revalidate();
                        frame.repaint();
                    } else {
                        // Mostrar mensaje de error si no se encuentran las credenciales
                        JOptionPane.showMessageDialog(frame,
                                "Correo o Contraseña incorrectos.",
                                "Error de Login",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }
    private void checkFields() {
        String correo = txtEmail.getText();
        String password = new String(pswContra.getPassword());

        // Habilitar el botón solo si todos los campos están llenos
        btnLogin.setEnabled(!correo.isEmpty() && !password.isEmpty());
    }
}
