package com.example.fullmoonmenu;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ItemDetailActivity extends AppCompatActivity {

    private static final String TAG = "ItemDetailActivity";
    private ShoppingCart shoppingCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        Intent intent = getIntent();
        int itemId = intent.getIntExtra("itemId", -1);

        if (itemId == -1) {
            Log.e(TAG, "Invalid itemId, finishing activity");
            finish();
            return;
        }

        shoppingCart = ShoppingCart.getInstance(); // Assuming ShoppingCart is a singleton
        ImageView itemImage = findViewById(R.id.itemImage);
        TextView itemName = findViewById(R.id.itemName);
        TextView itemPrice = findViewById(R.id.itemPrice);
        TextView itemCurrency = findViewById(R.id.itemCurrency);
        TextView itemTaste = findViewById(R.id.itemTaste);
        TextView itemEffect = findViewById(R.id.itemEffect);
        Button backToMenuButton = findViewById(R.id.backToMenuButton);
        Button addToCartButton = findViewById(R.id.addToCartButton);

        backToMenuButton.setOnClickListener(v -> finish());

        fetchMenuItem(itemId, menuItem -> {
            runOnUiThread(() -> {
                itemName.setText(menuItem.getName());
                itemPrice.setText(String.valueOf(menuItem.getPrice())+menuItem.getCurrency());
                itemTaste.setText(menuItem.getTaste());
                itemEffect.setText(menuItem.getEffect());
                itemImage.setImageBitmap(BitmapFactory.decodeByteArray(menuItem.getImage(), 0, menuItem.getImage().length));

                addToCartButton.setOnClickListener(v -> {
                    shoppingCart.addItem(menuItem);
                    // Optionally show a message or update UI
                });
            });
        });
    }

    private void fetchMenuItem(int itemId, MenuItemCallback callback) {
        new Thread(() -> {
            try {
                URL url = new URL("http://192.168.1.139:8080/items/" + itemId);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONObject jsonObject = new JSONObject(response.toString());
                String imageBase64 = jsonObject.getString("image");
                byte[] imageBytes = android.util.Base64.decode(imageBase64, android.util.Base64.DEFAULT);
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

                callback.onMenuItemFetched(menuItem);
            } catch (Exception e) {
                Log.e(TAG, "Error fetching menu item", e);
                runOnUiThread(() -> Toast.makeText(ItemDetailActivity.this, "Error fetching menu item", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    interface MenuItemCallback {
        void onMenuItemFetched(MenuItem menuItem);
    }
}