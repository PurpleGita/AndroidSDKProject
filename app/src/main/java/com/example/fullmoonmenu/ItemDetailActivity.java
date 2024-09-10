package com.example.fullmoonmenu;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ItemDetailActivity extends AppCompatActivity {

    private ShoppingCart shoppingCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        Intent intent = getIntent();
        MenuItem menuItem = (MenuItem) intent.getSerializableExtra("menuItem");

        shoppingCart = ShoppingCart.getInstance(); // Assuming ShoppingCart is a singleton

        TextView itemName = findViewById(R.id.itemName);
        TextView itemPrice = findViewById(R.id.itemPrice);
        TextView itemCurrency = findViewById(R.id.itemCurrency);
        TextView itemTaste = findViewById(R.id.itemTaste);
        TextView itemEffect = findViewById(R.id.itemEffect);
        ImageView itemImage = findViewById(R.id.itemImage);
        Button backToMenuButton = findViewById(R.id.backToMenuButton);
        Button addToCartButton = findViewById(R.id.addToCartButton);

        itemName.setText(menuItem.getName());
        itemPrice.setText(String.valueOf(menuItem.getPrice()));
        itemCurrency.setText(menuItem.getCurrency());
        itemTaste.setText(menuItem.getTaste());
        itemEffect.setText(menuItem.getEffect());
        itemImage.setImageBitmap(BitmapFactory.decodeByteArray(menuItem.getImage(), 0, menuItem.getImage().length));

        backToMenuButton.setOnClickListener(v -> finish());

        addToCartButton.setOnClickListener(v -> {
            shoppingCart.addItem(menuItem);
            // Optionally show a message or update UI
        });
    }
}