package com.example.fullmoonmenu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ShoppingCartActivity extends AppCompatActivity {

    private static final String TAG = "ShoppingCartActivity";
    private RecyclerView recyclerView;
    private ShoppingCartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        ShoppingCart shoppingCart = ShoppingCart.getInstance();

        Button backToMenuButton = findViewById(R.id.action_menu);
        backToMenuButton.setOnClickListener(v -> {
            Intent intent = new Intent(ShoppingCartActivity.this, MainActivity.class);
            startActivity(intent);
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ShoppingCartAdapter(shoppingCart.getItems(), shoppingCart);
        recyclerView.setAdapter(adapter);

        Log.d(TAG, "onCreate: ShoppingCartActivity created");
    }
}