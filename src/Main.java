import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import org.json.JSONObject;

public class Main extends JFrame {
    private JPanel dronePanel;
    private JScrollPane scrollPane;
    private String nextPage = "/drones/?format=json";
    private String previousPage = null;

    // Instanzvariablen für Buttons
    private JButton prevButton;
    private JButton nextButton;

    public Main() {
        setTitle("Drone Simulation Interface");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Hauptlayout: alles zentrieren
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Überschrift
        JLabel headerLabel = new JLabel("Welcome to the Drone Simulation Interface!");
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Drohnen-Datenbereich
        dronePanel = new JPanel();
        dronePanel.setLayout(new BoxLayout(dronePanel, BoxLayout.Y_AXIS));
        dronePanel.setBackground(Color.WHITE);
        dronePanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        scrollPane = new JScrollPane(dronePanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Navigation Buttons
        JPanel navigationPanel = new JPanel();
        prevButton = new JButton("Previous");
        nextButton = new JButton("Next");

        prevButton.setEnabled(false); // Anfangs deaktiviert
        prevButton.addActionListener(e -> {
            if (previousPage != null) {
                loadDrones(previousPage);
            }
        });

        nextButton.addActionListener(e -> {
            if (nextPage != null) {
                loadDrones(nextPage);
            }
        });

        navigationPanel.add(prevButton);
        navigationPanel.add(nextButton);
        mainPanel.add(navigationPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);

        // Erste Seite laden
        loadDrones(nextPage);
    }

    private void loadDrones(String endpoint) {
        try {
            dronePanel.removeAll();
            List<JSONObject> drones = DroneAPI.getDrones(endpoint);
            for (JSONObject drone : drones) {
                String displayText = String.format(
                        "ID: %d\nSerialnumber: %s\nCreated: %s\nCarriage Weight: %d\nCarriage Type: %s\n",
                        drone.getInt("id"),
                        drone.optString("serialnumber", "Unknown"),
                        drone.optString("created", "Unknown"),
                        drone.optInt("carriage_weight", 0),
                        drone.optString("carriage_type", "Unknown")
                );

                JTextArea droneText = new JTextArea(displayText);
                droneText.setFont(new Font("Arial", Font.PLAIN, 14));
                droneText.setEditable(false);
                droneText.setLineWrap(true);
                droneText.setWrapStyleWord(true);
                droneText.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

                dronePanel.add(droneText);
            }

            dronePanel.revalidate();
            dronePanel.repaint();

            // Update next and previous links
            JSONObject pagination = DroneAPI.getPaginationInfo(endpoint);
            nextPage = pagination.optString("next", null);
            previousPage = pagination.optString("previous", null);

            // Buttons aktivieren/deaktivieren
            prevButton.setEnabled(previousPage != null);
            nextButton.setEnabled(nextPage != null);

        } catch (Exception e) {
            e.printStackTrace();
            JLabel errorLabel = new JLabel("Error fetching drone data.");
            errorLabel.setForeground(Color.RED);
            errorLabel.setFont(new Font("Arial", Font.BOLD, 14));
            dronePanel.add(errorLabel);
            dronePanel.revalidate();
            dronePanel.repaint();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}
