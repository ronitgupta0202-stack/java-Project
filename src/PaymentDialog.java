import javax.swing.*;
import java.awt.*;

public class PaymentDialog extends JDialog {
    private boolean paymentSuccessful = false;
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel methodPanel = new JPanel(cardLayout);
    private final Font labelFont = new Font("Futura", Font.PLAIN, 14);
    private final Font titleFont = new Font("Futura", Font.BOLD, 18);

    public PaymentDialog(JFrame parent) {
        super(parent, "Secure Payment", true);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(new Color(245, 245, 245));

        String[] paymentMethods = {"Debit Card", "Credit Card", "UPI ID", "Internet Banking"};
        JComboBox<String> methodComboBox = new JComboBox<>(paymentMethods);
        methodComboBox.setFont(labelFont);

        methodComboBox.addActionListener(e -> {
            String selected = (String) methodComboBox.getSelectedItem();
            if (selected != null) cardLayout.show(methodPanel, selected);
        });

        methodPanel.setBackground(new Color(245, 245, 245));
        methodPanel.add(createCardFormPanel(), "Debit Card");
        methodPanel.add(createCardFormPanel(), "Credit Card");
        methodPanel.add(createUPIPanel(), "UPI ID");
        methodPanel.add(createNetBankingPanel(), "Internet Banking");

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(new Color(245, 245, 245));
        JLabel methodLabel = new JLabel("💳 Select Payment Method:");
        methodLabel.setFont(titleFont);
        topPanel.add(methodLabel, BorderLayout.WEST);
        topPanel.add(methodComboBox, BorderLayout.CENTER);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 20));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(245, 245, 245));

        JButton payButton = new JButton("Pay");
        JButton cancelButton = new JButton("Cancel");

        styleButton(payButton, new Color(46, 204, 113));  // green
        styleButton(cancelButton, new Color(231, 76, 60)); // red

        buttonPanel.add(payButton);
        buttonPanel.add(cancelButton);

        payButton.addActionListener(e -> {
            String selected = (String) methodComboBox.getSelectedItem();
            JPanel selectedPanel = (JPanel) methodPanel.getComponent(methodComboBox.getSelectedIndex());

            if (!validatePanel(selectedPanel)) {
                JOptionPane.showMessageDialog(this, "All fields are required for " + selected + ".", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            paymentSuccessful = true;
            JOptionPane.showMessageDialog(this, "✅ Payment Successful via " + selected + "!");
            dispose();
        });

        cancelButton.addActionListener(e -> {
            paymentSuccessful = false;
            dispose();
        });

        methodPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        add(topPanel, BorderLayout.NORTH);
        add(methodPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setSize(460, 340);
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    public boolean isPaymentSuccessful() {
        return paymentSuccessful;
    }

    private JPanel createCardFormPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        addLabeledField(panel, "Name on Card:");
        addLabeledField(panel, "Card Number:");
        addLabeledField(panel, "Expiry (MM/YY):");
        addLabeledPassword(panel, "CVV:");

        return panel;
    }

    private JPanel createUPIPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 10));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        addLabeledField(panel, "Enter UPI ID:");
        return panel;
    }

    private JPanel createNetBankingPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        addLabeledField(panel, "Bank Name:");
        addLabeledField(panel, "Customer ID:");
        return panel;
    }

    private void addLabeledField(JPanel panel, String label) {
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(labelFont);
        panel.add(jLabel);
        JTextField textField = new JTextField();
        textField.setFont(labelFont);
        panel.add(textField);
    }

    private void addLabeledPassword(JPanel panel, String label) {
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(labelFont);
        panel.add(jLabel);
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(labelFont);
        panel.add(passwordField);
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("Futura", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
    }

    private boolean validatePanel(JPanel panel) {
        Component[] components = panel.getComponents();
        for (Component c : components) {
            if (c instanceof JTextField) {
                if (((JTextField) c).getText().trim().isEmpty()) return false;
            } else if (c instanceof JPasswordField) {
                if (new String(((JPasswordField) c).getPassword()).trim().isEmpty()) return false;
            }
        }
        return true;
    }
}
