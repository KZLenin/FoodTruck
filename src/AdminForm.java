import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminForm {
    public JPanel adminPanel;
    private JButton btnEdit;
    private JButton btnStades;
    private JButton btnReports;
    private JButton btnAdd;
    private JButton btnSignOut;

    public AdminForm(JFrame frame) {
        btnSignOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setContentPane(new LoginForm(frame).loginPanel);
                frame.revalidate();
                frame.repaint();
            }
        });
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setContentPane(new AddForm(frame).addPanel);
                frame.revalidate();
                frame.repaint();
            }
        });
        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setContentPane(new EditForm(frame).editPanel);
                frame.revalidate();
                frame.repaint();
            }
        });
        btnStades.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setContentPane(new StadesForm(frame).stadePanel);
                frame.revalidate();
                frame.repaint();
            }
        });
        btnReports.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setContentPane(new ReportForm(frame).reportPanel);
                frame.revalidate();
                frame.repaint();
            }
        });
    }
}
