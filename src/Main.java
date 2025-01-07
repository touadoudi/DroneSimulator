import javax.swing.*;
import java.awt.*;
import java.util.List;
import org.json.JSONObject;

public class Main extends JFrame {
    public Main() {
        setTitle("Drone Simulation Interface");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Hauptlayout: alles zentrieren
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Rand um das Fenster

        // Überschrift
        JLabel headerLabel = new JLabel("Welcome to the Drone Simulation Interface!");
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Größere Schriftart
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Drohnen-Datenbereich
        JPanel dronePanel = new JPanel();
        dronePanel.setLayout(new BoxLayout(dronePanel, BoxLayout.Y_AXIS));
        dronePanel.setBackground(Color.WHITE); // Weißer Hintergrund
        dronePanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1)); // Rahmen um den Panel

        try {
            List<JSONObject> drones = DroneAPI.getAllDrones();
            for (JSONObject drone : drones) {
                String displayText = String.format(
                        "ID: %s, Manufacturer: %s, Model: %s, Status: %s",
                        drone.getInt("id"),
                        drone.optString("manufacturer", "Unknown"),
                        drone.optString("model", "Unknown"),
                        drone.optString("status", "Unknown")
                );
                JLabel droneLabel = new JLabel(displayText);
                droneLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                droneLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Abstand zwischen den Labels
                dronePanel.add(droneLabel);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JLabel errorLabel = new JLabel("Error fetching drone data.");
            errorLabel.setForeground(Color.RED);
            errorLabel.setFont(new Font("Arial", Font.BOLD, 14));
            dronePanel.add(errorLabel);
        }

        JScrollPane scrollPane = new JScrollPane(dronePanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Kein Rand für den Scrollbereich
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Button unten
        JButton startButton = new JButton("Start Simulation");
        startButton.setFont(new Font("Arial", Font.BOLD, 14));
        startButton.addActionListener(e -> JOptionPane.showMessageDialog(Main.this, "Simulation Started!"));
        mainPanel.add(startButton, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}
