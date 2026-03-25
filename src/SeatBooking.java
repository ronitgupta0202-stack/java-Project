import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;

public class SeatBooking extends JFrame {
    private final String username;
    private final String movieName;
    private final String selectedDate;
    private final Set<String> selectedSeats = new HashSet<>();
    private final Map<String, JButton> seatButtons = new HashMap<>();
    private final Set<String> userBookedSeats = new HashSet<>();

    private JPanel buttonsPanel;
    private JButton snackButton;

    public SeatBooking(String username, String movieName, String selectedDate) {
        this.username = username;
        this.movieName = movieName;
        this.selectedDate = selectedDate;

        ToolTipManager.sharedInstance().setInitialDelay(100);

        Font titleFont = new Font("Futura", Font.BOLD, 30);
        Font buttonFont = new Font("Calibri", Font.PLAIN, 14);

        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(new Color(45, 45, 60));
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel title = new JLabel("🎬 Seat Booking - " + movieName + " on " + selectedDate, JLabel.CENTER);
        title.setFont(titleFont);
        title.setForeground(Color.ORANGE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(title);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JLabel screen = new JLabel("-------- SCREEN THIS WAY --------", JLabel.CENTER);
        screen.setFont(new Font("Arial", Font.ITALIC, 16));
        screen.setForeground(Color.LIGHT_GRAY);
        screen.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(screen);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        buttonsPanel = new JPanel();
        buttonsPanel.setBackground(centerPanel.getBackground());
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 0));

        JPanel seatsContainer = new JPanel();
        seatsContainer.setBackground(centerPanel.getBackground());
        seatsContainer.setLayout(new BoxLayout(seatsContainer, BoxLayout.Y_AXIS));

        seatsContainer.add(createSeatSection("Classic", 6, 6, buttonFont));
        seatsContainer.add(Box.createRigidArea(new Dimension(0, 20)));

        seatsContainer.add(createSeatSection("Prime", 9, 14, buttonFont));
        seatsContainer.add(Box.createRigidArea(new Dimension(0, 20)));

        seatsContainer.add(createSeatSection("Recliner", 3, 6, buttonFont));

        centerPanel.add(seatsContainer);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        JButton bookButton = new JButton("Book Selected");
        JButton cancelButton = new JButton("Cancel Selected");

        bookButton.setFont(buttonFont);
        cancelButton.setFont(buttonFont);

        Color orange = new Color(255, 165, 0);
        Dimension buttonSize = new Dimension(150, 40);

        bookButton.setBackground(orange);
        bookButton.setForeground(Color.BLACK);
        bookButton.setPreferredSize(buttonSize);

        cancelButton.setBackground(orange);
        cancelButton.setForeground(Color.BLACK);
        cancelButton.setPreferredSize(buttonSize);

        buttonsPanel.add(bookButton);
        buttonsPanel.add(cancelButton);

        try {
            snackButton = new JButton("🍿 Order Snacks");
            snackButton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
            snackButton.setBackground(orange);
            snackButton.setForeground(Color.BLACK);
            snackButton.setPreferredSize(buttonSize);
            snackButton.setFocusPainted(false);

            snackButton.addActionListener(e -> new Snack(this).setVisible(true));
            buttonsPanel.add(snackButton);
        } catch (Exception e) {
            System.out.println("❌ Snack button error: " + e.getMessage());
        }

        centerPanel.add(buttonsPanel);

        cancelButton.addActionListener(e -> handleCancel());
        bookButton.addActionListener(e -> handleBooking());

        loadBookedSeats();

        setTitle("Seat Booking");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        add(new JScrollPane(centerPanel));
        setVisible(true);
    }

    private JPanel createSeatSection(String section, int rows, int cols, Font buttonFont) {
        JPanel panel = new JPanel(new GridLayout(rows, cols, 2, 2)) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(cols * 35, rows * 35);
            }
        };

        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.ORANGE, 2),
                section + " Seats",
                0, 0,
                new Font("Arial", Font.BOLD, 16),
                Color.ORANGE));

        Color lightBlue = new Color(160, 200, 250);
        char sectionPrefix = section.charAt(0);
        int seatCounter = 1;

        for (int i = 0; i < rows * cols; i++) {
            String seatId = sectionPrefix + String.valueOf(seatCounter++);
            JButton seatButton = new JButton("");
            seatButton.setToolTipText(seatId);
            seatButton.setBackground(lightBlue);
            seatButton.setFont(buttonFont);
            seatButton.setPreferredSize(new Dimension(5, 5));
            seatButton.setMargin(new Insets(0, 0, 0, 0));

            seatButtons.put(seatId, seatButton);
            panel.add(seatButton);

            seatButton.addActionListener(e -> toggleSeat(seatId));
        }
        return panel;
    }

    private void toggleSeat(String seatId) {
        JButton button = seatButtons.get(seatId);
        if (button == null || !button.isEnabled()) return;

        Color defaultColor = new Color(173, 216, 230);

        if (selectedSeats.contains(seatId)) {
            selectedSeats.remove(seatId);
            button.setBackground(defaultColor);
        } else {
            selectedSeats.add(seatId);
            button.setBackground(Color.YELLOW);
        }
    }

    private void handleBooking() {
        if (selectedSeats.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select at least one seat.");
            return;
        }

        PaymentDialog paymentDialog = new PaymentDialog(this);
        paymentDialog.setVisible(true);

        if (paymentDialog.isPaymentSuccessful()) {
            try {
                bookSeats(selectedSeats);
                JOptionPane.showMessageDialog(this, "Seats booked successfully!");
                reloadSeats();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Booking Error: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Payment cancelled. Booking not completed.");
        }
    }

    private void handleCancel() {
        if (selectedSeats.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No seats selected to cancel.");
            return;
        }

        Set<String> cancelable = new HashSet<>(selectedSeats);
        cancelable.retainAll(userBookedSeats);

        if (cancelable.isEmpty()) {
            JOptionPane.showMessageDialog(this, "You can only cancel your own bookings.");
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this,
                "Cancel selected seat(s)? You will receive a refund.", "Confirm", JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            try {
                cancelSeats(cancelable);
                int refundAmount = calculateRefund(cancelable);
                JOptionPane.showMessageDialog(this,
                        "✅ Booking(s) cancelled.\n💰 Refund Processed: ₹" + refundAmount,
                        "Refund", JOptionPane.INFORMATION_MESSAGE);
                reloadSeats();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Cancellation Error: " + ex.getMessage());
            }
        }
    }

    private void loadBookedSeats() {
        userBookedSeats.clear();
        Color defaultColor = new Color(173, 216, 230);

        for (JButton button : seatButtons.values()) {
            button.setEnabled(true);
            button.setBackground(defaultColor);
        }

        try (Connection con = getConnection()) {
            String sql = "SELECT seat_id, username FROM seat_bookings WHERE movie_name = ? AND selected_date = ?";
            try (PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setString(1, movieName);
                pst.setDate(2, java.sql.Date.valueOf(selectedDate));
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    String seatId = rs.getString("seat_id");
                    String bookedByUser = rs.getString("username");
                    JButton btn = seatButtons.get(seatId);

                    if (btn != null) {
                        btn.setBackground(Color.RED);
                        if (bookedByUser.equals(username)) {
                            btn.setEnabled(true);
                            userBookedSeats.add(seatId);
                        } else {
                            btn.setEnabled(false);
                        }
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "DB Load Error: " + e.getMessage());
        }

        selectedSeats.clear();
    }

    private void reloadSeats() {
        loadBookedSeats();
        selectedSeats.clear();
    }

    private void bookSeats(Set<String> seats) throws SQLException {
        try (Connection con = getConnection()) {
            String sql = "INSERT INTO seat_bookings (username, seat_id, movie_name, selected_date) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pst = con.prepareStatement(sql)) {
                for (String seat : seats) {
                    pst.setString(1, username);
                    pst.setString(2, seat);
                    pst.setString(3, movieName);
                    pst.setDate(4, java.sql.Date.valueOf(selectedDate));
                    pst.addBatch();
                }
                pst.executeBatch();
            }
        }
    }

    private void cancelSeats(Set<String> seats) throws SQLException {
        try (Connection con = getConnection()) {
            String sql = "DELETE FROM seat_bookings WHERE username=? AND seat_id=? AND movie_name=? AND selected_date=?";
            try (PreparedStatement pst = con.prepareStatement(sql)) {
                for (String seat : seats) {
                    pst.setString(1, username);
                    pst.setString(2, seat);
                    pst.setString(3, movieName);
                    pst.setDate(4, java.sql.Date.valueOf(selectedDate));
                    pst.addBatch();
                }
                pst.executeBatch();
            }
        }
    }

    private int calculateRefund(Set<String> seatIds) {
        int total = 0;
        for (String seatId : seatIds) {
            if (seatId.startsWith("C")) total += 150;
            else if (seatId.startsWith("P")) total += 250;
            else if (seatId.startsWith("R")) total += 400;
        }
        return total;
    }

    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/cinema";
        String user = "root";
        String pass = "vaniiiiii02";
        return DriverManager.getConnection(url, user, pass);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new SeatBooking("rutika", "Phir Hera Pheri", "2025-08-05"));
    }
}
