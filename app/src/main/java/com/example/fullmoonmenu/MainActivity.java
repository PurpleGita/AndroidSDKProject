package com.example.fullmoonmenu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private MenuItemAdapter adapter;
    private List<com.example.fullmoonmenu.MenuItem> menuItemList;
    private Button showFoodButton;
    private Button showDrinksButton;
    private Button showAllButton;
    private Button viewCartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);

        ShoppingCart shoppingCart = ShoppingCart.getInstance();

        // Load the item_place image and convert it to a byte array in a background thread
        new Thread(() -> {
            Bitmap itemPlaceBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.item_place);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            itemPlaceBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] itemPlaceBytes = byteArrayOutputStream.toByteArray();

            // Post the result back to the main thread
            new Handler(Looper.getMainLooper()).post(() -> {
                Log.d(TAG, "Item place image byte array: " + Arrays.toString(itemPlaceBytes));
            });
        }).start();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            int left = insets.getInsets(WindowInsetsCompat.Type.systemBars()).left;
            int top = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top;
            int right = insets.getInsets(WindowInsetsCompat.Type.systemBars()).right;
            int bottom = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;
            v.setPadding(left, top, right, bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        int spanCount = MenuItemAdapter.calculateSpanCount(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));
        adapter = new MenuItemAdapter(new ArrayList<>(), shoppingCart, this);
        recyclerView.setAdapter(adapter);

        viewCartButton = findViewById(R.id.viewCartButton);
        viewCartButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ShoppingCartActivity.class);
            startActivity(intent);
        });

        showFoodButton = findViewById(R.id.showFoodButton);
        showDrinksButton = findViewById(R.id.showDrinksButton);
        showAllButton = findViewById(R.id.showAllButton);

        showFoodButton.setOnClickListener(v -> filterMenuItems(true));
        showDrinksButton.setOnClickListener(v -> filterMenuItems(false));
        showAllButton.setOnClickListener(v -> adapter.updateMenuItems(menuItemList));

        Log.d(TAG, "onCreate: Activity created");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the XML resource
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        Log.d(TAG, "onCreateOptionsMenu: Menu created");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle menu item selections
        int id = item.getItemId();
        Log.d(TAG, "onOptionsItemSelected: Menu item selected, id=" + id);
        if (id == R.id.action_menu) {
            Log.d(TAG, "onOptionsItemSelected: action_menu selected");
            fetchAndDisplayMenuItems();
            return true;
        }
        if (id == R.id.action_admin_page) {
            Intent intent = new Intent(MainActivity.this, AdminLoginActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void fetchAndDisplayMenuItems() {
        // Fetch menu items from the server and update the UI
        Log.d(TAG, "fetchAndDisplayMenuItems: Fetching menu items");
        new Thread(() -> {
            try {
                List<com.example.fullmoonmenu.MenuItem> menuItems = ApiClient.fetchMenuItems();
                menuItemList = menuItems; // Store the fetched menu items
                Log.d(TAG, "fetchAndDisplayMenuItems: Menu items fetched, count=" + menuItems.size());
                runOnUiThread(() -> {
                    adapter.updateMenuItems(menuItems);
                    showFilterButtons();
                });
            } catch (Exception e) {
                Log.e(TAG, "fetchAndDisplayMenuItems: Error fetching menu items", e);
            }
        }).start();
    }

    private void filterMenuItems(boolean isFood) {
        // Filter the menu items based on whether they are food or drinks
        List<com.example.fullmoonmenu.MenuItem> filteredItems = menuItemList.stream()
                .filter(menuItem -> menuItem.isFood() == isFood)
                .collect(Collectors.toList());
        adapter.updateMenuItems(filteredItems);
    }

    private void showFilterButtons() {
        // Show the filter buttons on the UI
        showFoodButton.setVisibility(View.VISIBLE);
        showDrinksButton.setVisibility(View.VISIBLE);
        showAllButton.setVisibility(View.VISIBLE);
        viewCartButton.setVisibility(View.VISIBLE);
    }
}