package com.example.fullmoonmenu;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShoppingCart {
    private static final String TAG = "ShoppingCart";
    private static ShoppingCart instance;
    private List<MenuItem> items;

    // Private constructor to initialize the items list
    private ShoppingCart() {
        items = Collections.synchronizedList(new ArrayList<>());
    }

    // Method to get the singleton instance of ShoppingCart
    public static synchronized ShoppingCart getInstance() {
        if (instance == null) {
            instance = new ShoppingCart();
        }
        return instance;
    }

    // Method to get a copy of the items list
    public List<MenuItem> getItems() {
        return new ArrayList<>(items);
    }

    // Method to add an item to the shopping cart
    public void addItem(MenuItem item) {
        Log.d(TAG, "Adding item: " + item.getName());
        items.add(item);
    }

    // Method to remove an item from the shopping cart
    public void removeItem(MenuItem item) {
        Log.d(TAG, "Removing item: " + item.getName());
        if (items.contains(item)) {
            items.remove(item);
        } else {
            Log.e(TAG, "Item not found: " + item.getName());
        }
    }
}