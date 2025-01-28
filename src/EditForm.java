import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;

public class EditForm {
    public JPanel editPanel;
    private JTextField textField1;
    private JTextField textField2;
    private JButton btnEdit;
    private JButton btnDelete;
    private JButton btnBack;
    private JTable table1;

    public EditForm(JFrame frame) {

        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setContentPane(new AdminForm(frame).adminPanel);
                frame.revalidate();
                frame.repaint();
            }
        });
    }

    private void createUIComponents() {
        String[] columnHeaders = {"ID", "Name", "Price", "Image"};
        DefaultTableModel tableModel = new DefaultTableModel(columnHeaders, 0);
        table1 = new JTable(tableModel);
    }
}
