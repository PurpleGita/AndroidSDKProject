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

    public MenuItemAdapter(List<MenuItem> menuItems, ShoppingCart shoppingCart, Context context) {
        this.menuItems = menuItems;
        this.shoppingCart = shoppingCart;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MenuItem menuItem = menuItems.get(position);
        holder.itemName.setText(menuItem.getName());
        holder.itemPrice.setText(menuItem.getPrice() + " " + menuItem.getCurrency());
        holder.itemImage.setImageBitmap(BitmapFactory.decodeByteArray(menuItem.getImage(), 0, menuItem.getImage().length));

        holder.addToCartButton.setOnClickListener(v -> {
            shoppingCart.addItem(menuItem);
            // Optionally show a message or update UI
        });

        holder.itemImage.setOnClickListener(v -> {
            Intent intent = new Intent(context, ItemDetailActivity.class);
            intent.putExtra("itemId", menuItem.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public void updateMenuItems(List<MenuItem> newMenuItems) {
        this.menuItems = newMenuItems;
        notifyDataSetChanged();
    }

    public static int calculateSpanCount(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
        int columnWidthDp = 180; // You can change this value to adjust the column width
        return Math.max(1, (int) (screenWidthDp / columnWidthDp));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView itemName;
        public TextView itemPrice;
        public ImageView itemImage;
        public Button addToCartButton;

        public ViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemImage = itemView.findViewById(R.id.itemImage);
            addToCartButton = itemView.findViewById(R.id.addToCartButton);
        }
    }
}