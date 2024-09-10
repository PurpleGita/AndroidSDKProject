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

        backToMenuButton = findViewById(R.id.backToMenuButton);

        ShoppingCart shoppingCart = ShoppingCart.getInstance();

        backToMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ShoppingCartAdapter(shoppingCart.getItems(), shoppingCart);
        recyclerView.setAdapter(adapter);
    }
}