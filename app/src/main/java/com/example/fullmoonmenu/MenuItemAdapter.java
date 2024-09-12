package com.example.fullmoonmenu;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.ViewHolder> {
    private static final String TAG = "MenuItemAdapter";
    private List<MenuItem> menuItems;
    private ShoppingCart shoppingCart;
    private Context context;

    // Constructor to initialize the adapter with menu items, shopping cart, and context
    public MenuItemAdapter(List<MenuItem> menuItems, ShoppingCart shoppingCart, Context context) {
        this.menuItems = menuItems;
        this.shoppingCart = shoppingCart;
        this.context = context;
    }

    @NonNull
    @Override
    // Method to create new ViewHolder instances
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each menu item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    // Method to bind data to the ViewHolder
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the menu item at the given position
        MenuItem menuItem = menuItems.get(position);
        // Set the item name and price
        holder.itemName.setText(menuItem.getName());
        holder.itemPrice.setText(menuItem.getPrice() + " " + menuItem.getCurrency());
        // Set the item image
        holder.itemImage.setImageBitmap(BitmapFactory.decodeByteArray(menuItem.getImage(), 0, menuItem.getImage().length));

        // Set click listener for the add to cart button
        holder.addToCartButton.setOnClickListener(v -> {
            shoppingCart.addItem(menuItem);
            // Optionally show a message or update UI
        });

        // Set click listener for the item image to open item details
        holder.itemImage.setOnClickListener(v -> {
            Intent intent = new Intent(context, ItemDetailActivity.class);
            intent.putExtra("itemId", menuItem.getId());
            context.startActivity(intent);
        });
    }

    @Override
    // Method to get the total number of menu items
    public int getItemCount() {
        return menuItems.size();
    }

    // Method to update the list of menu items and refresh the RecyclerView
    public void updateMenuItems(List<MenuItem> newMenuItems) {
        this.menuItems = newMenuItems;
        notifyDataSetChanged();
    }

    // Static method to calculate the number of columns for the grid layout based on screen width
    public static int calculateSpanCount(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
        int columnWidthDp = 170; // Change this value to adjust the column width
        return Math.max(1, (int) (screenWidthDp / columnWidthDp));
    }

    // ViewHolder class to hold references to the UI components for each menu item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView itemName;
        public TextView itemPrice;
        public ImageView itemImage;
        public Button addToCartButton;

        // Constructor to initialize the UI components
        public ViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemImage = itemView.findViewById(R.id.itemImage);
            addToCartButton = itemView.findViewById(R.id.addToCartButton);
        }
    }
}