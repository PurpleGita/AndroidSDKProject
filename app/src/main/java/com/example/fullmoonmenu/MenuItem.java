package com.example.fullmoonmenu;

import android.util.Base64;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class MenuItem implements Serializable {
    @PrimaryKey
    private int id;
    private boolean isFood;
    private String name;
    private int price;
    private String currency;
    private byte[] image;
    private String taste;
    private String effect;
    private List<Object> allergies;

    // Constructor to initialize all fields of the MenuItem class
    public MenuItem(int id, boolean isFood, String name, int price, String currency, byte[] image, String taste, String effect, List<Object> allergies) {
        this.id = id;
        this.isFood = isFood;
        this.name = name;
        this.price = price;
        this.currency = currency;
        this.image = image;
        this.taste = taste;
        this.effect = effect;
        this.allergies = allergies;
    }

    // Getter method for the id field
    public int getId() {
        return id;
    }

    // Getter method for the isFood field
    public boolean isFood() {
        return isFood;
    }

    // Getter method for the name field
    public String getName() {
        return name;
    }

    // Getter method for the price field
    public int getPrice() {
        return price;
    }

    // Getter method for the currency field
    public String getCurrency() {
        return currency;
    }

    // Getter method for the image field
    public byte[] getImage() {
        return image;
    }

    // Getter method for the taste field
    public String getTaste() {
        return taste;
    }

    // Getter method for the effect field
    public String getEffect() {
        return effect;
    }

    // Getter method for the allergies field
    public List<Object> getAllergies() {
        return allergies;
    }

    // Method to convert the MenuItem object to a JSON string
    public String toJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("isFood", isFood);
            jsonObject.put("name", name);
            jsonObject.put("price", price);
            jsonObject.put("currency", currency);
            jsonObject.put("image", Base64.encodeToString(image, Base64.DEFAULT));
            jsonObject.put("taste", taste);
            jsonObject.put("effect", effect);
            JSONArray allergiesArray = new JSONArray(allergies);
            jsonObject.put("allergies", allergiesArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    // Static method to create a MenuItem object from a JSON string
    public static MenuItem fromJson(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            int id = jsonObject.getInt("id");
            boolean isFood = jsonObject.getBoolean("isFood");
            String name = jsonObject.getString("name");
            int price = jsonObject.getInt("price");
            String currency = jsonObject.getString("currency");
            byte[] image = Base64.decode(jsonObject.getString("image"), Base64.DEFAULT);
            String taste = jsonObject.getString("taste");
            String effect = jsonObject.getString("effect");
            JSONArray allergiesArray = jsonObject.getJSONArray("allergies");
            List<Object> allergies = new ArrayList<>();
            for (int i = 0; i < allergiesArray.length(); i++) {
                allergies.add(allergiesArray.get(i));
            }
            MenuItem menuItem = new MenuItem(id, isFood, name, price, currency, image, taste, effect, allergies);
            return menuItem;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}