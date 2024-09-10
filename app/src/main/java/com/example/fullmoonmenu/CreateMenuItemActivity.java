package com.example.fullmoonmenu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CreateMenuItemActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private CheckBox isFoodCheckBox;
    private EditText itemNameEditText;
    private EditText itemPriceEditText;
    private EditText itemCurrencyEditText;
    private EditText itemTasteEditText;
    private EditText itemEffectEditText;
    private EditText itemAllergiesEditText;
    private ImageView itemImageView;
    private Button selectImageButton;
    private Button createItemButton;

    private byte[] imageByteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_menu_item);

        isFoodCheckBox = findViewById(R.id.isFoodCheckBox);
        itemNameEditText = findViewById(R.id.itemNameEditText);
        itemPriceEditText = findViewById(R.id.itemPriceEditText);
        itemCurrencyEditText = findViewById(R.id.itemCurrencyEditText);
        itemTasteEditText = findViewById(R.id.itemTasteEditText);
        itemEffectEditText = findViewById(R.id.itemEffectEditText);
        itemAllergiesEditText = findViewById(R.id.itemAllergiesEditText);
        itemImageView = findViewById(R.id.itemImageView);
        selectImageButton = findViewById(R.id.selectImageButton);
        createItemButton = findViewById(R.id.createItemButton);

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

        createItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isFood = isFoodCheckBox.isChecked();
                String name = itemNameEditText.getText().toString();
                int price = Integer.parseInt(itemPriceEditText.getText().toString());
                String currency = itemCurrencyEditText.getText().toString();
                String taste = itemTasteEditText.getText().toString();
                String effect = itemEffectEditText.getText().toString();
                List<Object> allergies = new ArrayList<>();
                allergies.add(itemAllergiesEditText.getText().toString());

                MenuItem menuItem = new MenuItem(0, isFood, name, price, currency, imageByteArray, taste, effect, allergies);
                sendMenuItemToServer(menuItem);
            }
        });
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                itemImageView.setImageBitmap(bitmap);
                imageByteArray = convertBitmapToByteArray(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private void sendMenuItemToServer(MenuItem menuItem) {
        new Thread(() -> {
            try {
                URL url = new URL("http://192.168.1.139:8080/items");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; utf-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);

                JSONObject jsonInput = new JSONObject();
                jsonInput.put("isFood", menuItem.isFood());
                jsonInput.put("name", menuItem.getName());
                jsonInput.put("price", menuItem.getPrice());
                jsonInput.put("currency", menuItem.getCurrency());
                jsonInput.put("image", Base64.encodeToString(menuItem.getImage(), Base64.DEFAULT));
                jsonInput.put("taste", menuItem.getTaste());
                jsonInput.put("effect", menuItem.getEffect());
                JSONArray allergiesArray = new JSONArray(menuItem.getAllergies());
                jsonInput.put("allergies", allergiesArray);

                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonInput.toString().getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    runOnUiThread(() -> Toast.makeText(CreateMenuItemActivity.this, "Menu item created successfully", Toast.LENGTH_SHORT).show());
                } else {
                    runOnUiThread(() -> Toast.makeText(CreateMenuItemActivity.this, "Failed to create menu item", Toast.LENGTH_SHORT).show());
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(CreateMenuItemActivity.this, "An error occurred", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}