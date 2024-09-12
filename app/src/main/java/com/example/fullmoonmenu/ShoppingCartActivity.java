package com.example.fullmoonmenu;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ShoppingCartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ShoppingCartAdapter adapter;
    private Button backToMenuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        // Initialize the back to menu button
        backToMenuButton = findViewById(R.id.backToMenuButton);

        // Get the singleton instance of the shopping cart
        ShoppingCart shoppingCart = ShoppingCart.getInstance();

        // Set click listener for the back to menu button to finish the activity
        backToMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Initialize the RecyclerView and set its layout manager
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the adapter with the items from the shopping cart and set it to the RecyclerView
        adapter = new ShoppingCartAdapter(shoppingCart.getItems(), shoppingCart);
        recyclerView.setAdapter(adapter);
    }
}