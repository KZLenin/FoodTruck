import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddForm {
    public JPanel addPanel;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JButton btnExplore;
    private JButton btnSave;
    private JButton btnBack;

    public AddForm(JFrame frame) {
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setContentPane(new AdminForm(frame).adminPanel);
                frame.revalidate();
                frame.repaint();
            }
        });
    }

}
