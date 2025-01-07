import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DroneAPI {
    private static final String BASE_URL = "http://dronesim.facets-labs.com/api";
    private static final String TOKEN = "28c5253fd117e5e5a96c3684ee61155190899907"; // Ersetze mit deinem Token
    private static final List<JSONObject> dronesList = new ArrayList<>();

    public static List<JSONObject> getAllDrones() throws Exception {
        try {
            String dronesJson = fetchData("/drones/?format=json");
            JSONObject response = new JSONObject(dronesJson);
            JSONArray results = response.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject drone = results.getJSONObject(i);
                String dronetypeUrl = drone.getString("dronetype");

                JSONObject dronetype = fetchDroneType(dronetypeUrl);

                // Setze die richtigen Werte für Manufacturer und Model
                drone.put("manufacturer", dronetype.optString("manufacturer", "Unknown"));
                drone.put("model", dronetype.optString("typename", "Unknown"));
                drone.put("status", drone.optString("carriage_type", "Unknown")); // Beispiel für Status
                dronesList.add(drone);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dronesList;
    }


    private static String fetchData(String endpoint) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(BASE_URL + endpoint).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Token " + TOKEN);
        connection.setRequestProperty("Accept", "application/json");

        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            return response.toString();
        } else {
            throw new Exception("Error fetching data: " + responseCode);
        }
    }

    private static JSONObject fetchDroneType(String url) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Token " + TOKEN);
        connection.setRequestProperty("Accept", "application/json");

        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            return new JSONObject(response.toString());
        } else {
            throw new Exception("Error fetching dronetype: " + responseCode);
        }
    }
}
