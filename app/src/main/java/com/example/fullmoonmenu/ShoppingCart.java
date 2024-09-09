package com.example.fullmoonmenu;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShoppingCart {
    private static final String TAG = "ShoppingCart";
    private static ShoppingCart instance;
    private List<MenuItem> items;

    private ShoppingCart() {
        items = Collections.synchronizedList(new ArrayList<>());
    }

    public static synchronized ShoppingCart getInstance() {
        if (instance == null) {
            instance = new ShoppingCart();
        }
        return instance;
    }

    public List<MenuItem> getItems() {
        return new ArrayList<>(items);
    }

    public void addItem(MenuItem item) {
        Log.d(TAG, "Adding item: " + item.getName());
        items.add(item);
    }

    public void removeItem(MenuItem item) {
        Log.d(TAG, "Removing item: " + item.getName());
        if (items.contains(item)) {
            items.remove(item);
        } else {
            Log.e(TAG, "Item not found: " + item.getName());
        }
    }
}