import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MovieSelection extends JFrame {
    private final ButtonGroup movieGroup = new ButtonGroup();
    private final ButtonGroup dateGroup = new ButtonGroup();


    public MovieSelection(String username) {
        setTitle("🎬 CineBooking");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.DARK_GRAY);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        top.setBackground(Color.DARK_GRAY);
        top.add(createLabel("📅 Select Date:", 18, Color.WHITE));

        LocalDate today = LocalDate.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("EEE, MMM dd");

        for (int i = 0; i < 3; i++) {
            LocalDate date = today.plusDays(i);
            JRadioButton rb = new JRadioButton(date.format(fmt));
            rb.setHorizontalAlignment(SwingConstants.CENTER);

            rb.setActionCommand(date.toString());
            styleRadio(rb);
            if (i == 0) rb.setSelected(true);
            dateGroup.add(rb);
            top.add(rb);
        }

        add(top, BorderLayout.NORTH);

        JPanel moviePanel = new JPanel();
        moviePanel.setLayout(new BoxLayout(moviePanel, BoxLayout.Y_AXIS));
        moviePanel.setBackground(Color.DARK_GRAY);
        moviePanel.setBorder(BorderFactory.createTitledBorder(null, "🎞 Now Showing",
                0, 0, new Font("Arial", Font.BOLD, 20), Color.ORANGE));
        moviePanel.add(Box.createRigidArea(new Dimension(0, 30)));

        List<Movie> movies = Movie.getDefaultMovies();

        for (Movie movie : movies) {
            String movieInfo = "<html><div style='text-align:center;'><table style='margin:auto;' width='300' cellpadding='0' cellspacing='0'>" +
                    "<tr>" +
                    "<td align='left' style='padding-right:10px;'>" +
                    "<b>" + movie.getName() + "</b></td>" +
                    "<td align='center' style='" +
                    "background:#FFA500; " +
                    "color:white; " +
                    "font-weight:bold; " +
                    "padding:6px 12px; " +
                    "border-radius:6px; " +
                    "width:80px; " +
                    "display:inline-block; " +
                    "text-align:center;" +
                    "'>" + movie.getTime() + "</td>" +
                    "</tr>" +
                    "</table></div></html>";


            JRadioButton rb = new JRadioButton(movieInfo);
            rb.setActionCommand(movie.getName());
            styleRadio(rb);
            rb.setHorizontalAlignment(SwingConstants.CENTER);rb.setToolTipText("<html>Seat Prices:<br>Classic: ₹220<br>Prime: ₹260<br>Recliner: ₹350</html>");

            movieGroup.add(rb);
            moviePanel.add(rb);
        }




        JScrollPane scroll = new JScrollPane(moviePanel);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));
        add(scroll, BorderLayout.CENTER);

        JButton proceed = new JButton("Proceed to Seat Booking 🎫");
        proceed.setFont(new Font("Arial", Font.BOLD, 18));
        proceed.setBackground(Color.ORANGE);
        proceed.setFocusPainted(false);
        proceed.addActionListener(e -> {
            if (movieGroup.getSelection() != null && dateGroup.getSelection() != null) {
                String movie = movieGroup.getSelection().getActionCommand();
                String date = dateGroup.getSelection().getActionCommand();

                System.out.println("Proceed clicked: Movie=" + movie + ", Date=" + date);
                SeatBooking seatBooking = new SeatBooking(username, movie, date);
                seatBooking.toFront();
                seatBooking.requestFocus();
                dispose();


                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Please select both movie and date.");
            }
        });

        JPanel bottom = new JPanel();
        bottom.setBackground(Color.DARK_GRAY);
        bottom.add(proceed);
        add(bottom, BorderLayout.SOUTH);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setVisible(true);
    }

    private JLabel createLabel(String text, int size, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, size));
        label.setForeground(color);
        return label;
    }

    private void styleRadio(JRadioButton rb) {
        rb.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        rb.setForeground(Color.WHITE);
        rb.setBackground(Color.DARK_GRAY);
        rb.setFocusPainted(false);
    }

    public static void main(String[] args) {
        new MovieSelection("rutika");
    }
}