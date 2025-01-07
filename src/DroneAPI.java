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
    private static final String TOKEN = "28c5253fd117e5e5a96c3684ee61155190899907"; // Dein Token

    public static List<JSONObject> getDrones(String endpoint) throws Exception {
        List<JSONObject> dronesList = new ArrayList<>();
        String response = fetchData(endpoint);
        JSONObject jsonResponse = new JSONObject(response);

        JSONArray results = jsonResponse.getJSONArray("results");
        for (int i = 0; i < results.length(); i++) {
            JSONObject drone = results.getJSONObject(i);
            dronesList.add(drone);
        }

        return dronesList;
    }

    public static JSONObject getPaginationInfo(String endpoint) throws Exception {
        String response = fetchData(endpoint);
        return new JSONObject(response);
    }

    private static String fetchData(String endpoint) throws Exception {
        String fullUrl = endpoint.startsWith("http") ? endpoint : BASE_URL + endpoint;
        System.out.println("Requesting URL: " + fullUrl); // Debug-Ausgabe

        HttpURLConnection connection = (HttpURLConnection) new URL(fullUrl).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Token " + TOKEN);
        connection.setRequestProperty("Accept", "application/json");

        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode); // Debug-Ausgabe

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
}
