import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class Snack extends JDialog {
    private final Map<String, Integer> snackPrices = new LinkedHashMap<>();
    private final Map<String, JSpinner> snackSpinners = new LinkedHashMap<>();
    private final JFrame parent;

    public Snack(JFrame parent) {
        super(parent, "🍿 Order Snacks", true);
        this.parent = parent;

        setUndecorated(true);
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(255, 140, 0));  // dark orange
        headerPanel.setPreferredSize(new Dimension(0, 80));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 20));
        titlePanel.setBackground(headerPanel.getBackground());

        JLabel iconLabel = new JLabel("🍿");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        titlePanel.add(iconLabel);

        JLabel titleLabel = new JLabel("Order Your Snacks");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Futura", Font.BOLD, 32));
        titlePanel.add(titleLabel);

        headerPanel.add(titlePanel, BorderLayout.WEST);

        JButton closeButton = new JButton("✕");
        closeButton.setFont(new Font("Arial", Font.BOLD, 24));
        closeButton.setForeground(Color.WHITE);
        closeButton.setBackground(new Color(255, 69, 0)); // red-orange
        closeButton.setFocusPainted(false);
        closeButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        closeButton.addActionListener(e -> dispose());
        headerPanel.add(closeButton, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        JPanel snackPanel = new JPanel(new GridLayout(0, 2, 20, 20));
        snackPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));
        snackPanel.setBackground(new Color(245, 245, 245));

        snackPrices.put("Popcorn (Salted)", 120);
        snackPrices.put("Cold Drink", 80);
        snackPrices.put("Burger", 100);
        snackPrices.put("French Fries", 100);

        for (String snack : snackPrices.keySet()) {
            JLabel label = new JLabel(snack + " - ₹" + snackPrices.get(snack));
            label.setFont(new Font("Calibri", Font.PLAIN, 22));
            label.setForeground(new Color(40, 40, 40));
            snackPanel.add(label);

            JSpinner spinner = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
            spinner.setFont(new Font("Calibri", Font.PLAIN, 22));
            snackSpinners.put(snack, spinner);
            snackPanel.add(spinner);
        }

        add(new JScrollPane(snackPanel), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton orderButton = new JButton("Place Order");
        orderButton.setFont(new Font("Calibri", Font.BOLD, 24));
        orderButton.setBackground(new Color(255, 165, 0));
        orderButton.setForeground(Color.BLACK);
        orderButton.setFocusPainted(false);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Calibri", Font.BOLD, 24));
        cancelButton.setBackground(Color.LIGHT_GRAY);
        cancelButton.setFocusPainted(false);

        orderButton.addActionListener(e -> placeOrder());
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(orderButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(40, 0)));
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void placeOrder() {
        int total = 0;
        StringBuilder orderSummary = new StringBuilder("🧾 Order Summary:\n\n");

        for (String snack : snackPrices.keySet()) {
            int qty = (int) snackSpinners.get(snack).getValue();
            if (qty > 0) {
                int cost = snackPrices.get(snack) * qty;
                orderSummary.append("• ").append(snack).append(" x ").append(qty).append(" = ₹").append(cost).append("\n");
                total += cost;
            }
        }

        if (total == 0) {
            JOptionPane.showMessageDialog(this, "No snacks selected.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        PaymentDialog paymentDialog = new PaymentDialog(parent);
        paymentDialog.setVisible(true);

        if (!paymentDialog.isPaymentSuccessful()) {
            JOptionPane.showMessageDialog(this, "Payment was cancelled. Order not placed.", "Payment Cancelled", JOptionPane.WARNING_MESSAGE);
            return;
        }

        orderSummary.append("\n🍽 Total: ₹").append(total);
        JOptionPane.showMessageDialog(this, orderSummary.toString(), "Order Placed", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}
