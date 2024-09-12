package com.example.fullmoonmenu;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import android.util.Base64;
import android.util.Log;

public class ApiClient {

    private static final String API_URL = "http://192.168.0.183:8080/items";
    private static final String TAG = "ApiClient";

    // Method to fetch menu items from the server
    public static List<MenuItem> fetchMenuItems() {
        List<MenuItem> menuItems = new ArrayList<>();
        try {
            // Create a URL object with the API URL
            URL url = new URL(API_URL);
            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // Set the request method to GET
            connection.setRequestMethod("GET");

            // Read the response from the server
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Parse the response JSON array
            JSONArray jsonArray = new JSONArray(response.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                // Decode the image from Base64
                String imageBase64 = jsonObject.getString("image");
                byte[] imageBytes = Base64.decode(imageBase64, Base64.DEFAULT);
                Log.d(TAG, "fetchMenuItems: Decoded image bytes for item: " + jsonObject.getString("name"));
                // Create a MenuItem object and add it to the list
                MenuItem menuItem = new MenuItem(
                        jsonObject.getInt("id"),
                        jsonObject.getBoolean("isFood"),
                        jsonObject.getString("name"),
                        jsonObject.getInt("price"),
                        jsonObject.getString("currency"),
                        imageBytes,
                        jsonObject.getString("taste"),
                        jsonObject.getString("effect"),
                        new ArrayList<>()
                );
                menuItems.add(menuItem);
            }
        } catch (Exception e) {
            // Log any errors that occur during the fetch
            Log.e(TAG, "fetchMenuItems: Error fetching menu items", e);
        }
        return menuItems;
    }
}