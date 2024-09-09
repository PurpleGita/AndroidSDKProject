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

    private static final String API_URL = "http://192.168.1.139:8080/items";
    private static final String TAG = "ApiClient";

    public static List<MenuItem> fetchMenuItems() {
        List<MenuItem> menuItems = new ArrayList<>();
        try {
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONArray jsonArray = new JSONArray(response.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String imageBase64 = jsonObject.getString("image");
                byte[] imageBytes = Base64.decode(imageBase64, Base64.DEFAULT);
                Log.d(TAG, "fetchMenuItems: Decoded image bytes for item: " + jsonObject.getString("name"));
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
            Log.e(TAG, "fetchMenuItems: Error fetching menu items", e);
        }
        return menuItems;
    }
}