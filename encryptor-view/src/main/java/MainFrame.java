import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class MainFrame extends JFrame {

    private JTextField keyTextField;
    private JLabel keyLabel;
    private JTextField sourceTextField;
    private JLabel sourceLabel;
    private JTextField targetTextField;
    private JLabel targetLabel;
    private JRadioButton encryptButton;
    private JRadioButton decryptButton;
    private JButton sourceButton;
    private JButton targetButton;
    private JButton okButton;

    private JFileChooser sourceFileChooser;
    private JFileChooser targetFileChooser;

    public MainFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(new BorderLayout());

        this.sourceFileChooser = new JFileChooser();
        this.targetFileChooser = new JFileChooser();
        this.targetFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        this.keyTextField = new JTextField();
        this.keyTextField.setText("0123456789012345");
        this.keyLabel = new JLabel("Key (16 symbols):");
        JPanel keyPanel = new JPanel();
        keyPanel.setLayout(new BorderLayout());
        keyPanel.setPreferredSize(new Dimension(0, 32));
        keyPanel.add(this.keyLabel, BorderLayout.LINE_START);
        keyPanel.add(this.keyTextField, BorderLayout.CENTER);
        //keyPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.sourceLabel = new JLabel("Source file:");
        this.sourceTextField = new JTextField();
        this.sourceButton = new JButton("...");
        this.sourceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = sourceFileChooser.showOpenDialog(MainFrame.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = sourceFileChooser.getSelectedFile();
                    sourceTextField.setText(file.getAbsolutePath());
                }
            }
        });
        JPanel sourcePanel = new JPanel();
        sourcePanel.setPreferredSize(new Dimension(0, 32));
        sourcePanel.setLayout(new BorderLayout());
        sourcePanel.add(this.sourceLabel, BorderLayout.LINE_START);
        sourcePanel.add(this.sourceTextField, BorderLayout.CENTER);
        sourcePanel.add(this.sourceButton, BorderLayout.LINE_END);

        this.targetLabel = new JLabel("Target directory:");
        this.targetTextField = new JTextField();
        this.targetButton = new JButton("...");
        this.targetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = targetFileChooser.showOpenDialog(MainFrame.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = targetFileChooser.getSelectedFile();
                    targetTextField.setText(file.getAbsolutePath());
                }
            }
        });
        JPanel targetPanel = new JPanel();
        targetPanel.setPreferredSize(new Dimension(0, 32));
        targetPanel.setLayout(new BorderLayout());
        targetPanel.add(this.targetLabel, BorderLayout.LINE_START);
        targetPanel.add(this.targetTextField, BorderLayout.CENTER);
        targetPanel.add(this.targetButton, BorderLayout.LINE_END);

        this.encryptButton = new JRadioButton("encrypt");
        this.encryptButton.setSelected(true);
        this.decryptButton = new JRadioButton("decrypt");
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(this.encryptButton);
        buttonGroup.add(this.decryptButton);

        this.okButton = new JButton("OK");
        this.okButton.addActionListener(new OkButtonActionListener());
        JPanel okPanel = new JPanel();
        okPanel.setLayout(new FlowLayout());
        okPanel.add(this.okButton);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(keyPanel);
        //panel.add(new Box.Filler(new Dimension(0, 50), new Dimension(0, 0), new Dimension(Short.MAX_VALUE, Short.MAX_VALUE)));
        panel.add(sourcePanel);
        //panel.add(new Box.Filler(new Dimension(0, 0), new Dimension(0, 0), new Dimension(Short.MAX_VALUE, Short.MAX_VALUE)));
        panel.add(targetPanel);
        //panel.add(new Box.Filler(new Dimension(0, 0), new Dimension(0, 0), new Dimension(Short.MAX_VALUE, Short.MAX_VALUE)));
        //this.encryptButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        //this.decryptButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(this.encryptButton);
        //panel.add(new Box.Filler(new Dimension(0, 0), new Dimension(0, 0), new Dimension(Short.MAX_VALUE, Short.MAX_VALUE)));
        panel.add(this.decryptButton);

        this.add(panel, BorderLayout.CENTER);
        this.add(okPanel, BorderLayout.PAGE_END);

        this.setTitle("Encryptor");
        this.setPreferredSize(new Dimension(400, 220));
        this.setLocationRelativeTo(null);
        this.pack();
    }

    public static void main(String[] args) {
        MainFrame f = new MainFrame();
        f.setVisible(true);
    }

    private class OkButtonActionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(sourceTextField.getText() == null || sourceTextField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(MainFrame.this,
                        "Source destination is empty!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(targetTextField.getText() == null || targetTextField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(MainFrame.this,
                        "Target destination is empty!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(!FileUtils.getFile(sourceTextField.getText()).exists()) {
                JOptionPane.showMessageDialog(MainFrame.this,
                        "Source destination doesn't exist!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(!FileUtils.getFile(targetTextField.getText()).exists()) {
                JOptionPane.showMessageDialog(MainFrame.this,
                        "Target destination doesn't exist!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(keyTextField.getText() == null || keyTextField.getText().length() != 16) {
                JOptionPane.showMessageDialog(MainFrame.this,
                        "Key length must be 16!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            File sourceFile = FileUtils.getFile(sourceTextField.getText());
            File targetDirectory = FileUtils.getFile(targetTextField.getText());
            //System.out.println(sourceFile.getAbsolutePath());
            //System.out.println(targetDirectory.getAbsolutePath());
            if(encryptButton.isSelected()) {
                FileEncryptorDecryptor fed = new FileEncryptorDecryptor(sourceFile, keyTextField.getText(), false);
                fed.encrypt();
                try {
                    fed.save(targetDirectory);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            else if(decryptButton.isSelected()) {
                FileEncryptorDecryptor fed = new FileEncryptorDecryptor(sourceFile, keyTextField.getText(), true);
                fed.decrypt();
                try {
                    fed.save(targetDirectory);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

}
