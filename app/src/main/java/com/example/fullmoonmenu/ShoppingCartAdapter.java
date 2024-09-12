package com.example.fullmoonmenu;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ViewHolder> {

    private static final String TAG = "ShoppingCartAdapter";
    private List<MenuItem> items;
    private ShoppingCart shoppingCart;

    // Constructor to initialize the adapter with items and shopping cart
    public ShoppingCartAdapter(List<MenuItem> items, ShoppingCart shoppingCart) {
        this.items = items;
        this.shoppingCart = shoppingCart;
    }

    @NonNull
    @Override
    // Method to create new ViewHolder instances
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each shopping cart item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shopping_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    // Method to bind data to the ViewHolder
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the item at the given position
        MenuItem item = items.get(position);
        // Set the item name and price
        holder.itemName.setText(item.getName());
        holder.itemPrice.setText(String.valueOf(item.getPrice()) + "-" + String.valueOf(item.getCurrency()));

        // Set click listener for the remove from cart button
        holder.removeFromCartButton.setOnClickListener(v -> {
            synchronized (items) {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && pos < items.size()) {
                    Log.d(TAG, "Removing item at position: " + pos);
                    shoppingCart.removeItem(item);
                    updateItems(shoppingCart.getItems());
                } else {
                    Log.e(TAG, "Invalid position: " + pos);
                }
            }
        });
    }

    @Override
    // Method to get the total number of items in the shopping cart
    public int getItemCount() {
        return items.size();
    }

    // Method to update the list of items and refresh the RecyclerView
    public void updateItems(List<MenuItem> newItems) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return items.size();
            }

            @Override
            public int getNewListSize() {
                return newItems.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return items.get(oldItemPosition).getId() == newItems.get(newItemPosition).getId();
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return items.get(oldItemPosition).equals(newItems.get(newItemPosition));
            }
        });
        items.clear();
        items.addAll(newItems);
        diffResult.dispatchUpdatesTo(this);
    }

    // ViewHolder class to hold references to the UI components for each shopping cart item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView itemName;
        public TextView itemPrice;
        public ImageButton removeFromCartButton;

        // Constructor to initialize the UI components
        public ViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            removeFromCartButton = itemView.findViewById(R.id.removeFromCartButton);
        }
    }
}