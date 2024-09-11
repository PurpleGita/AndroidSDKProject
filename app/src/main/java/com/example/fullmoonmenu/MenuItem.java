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

    public int getId() {
        return id;
    }

    public boolean isFood() {
        return isFood;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    public byte[] getImage() {
        return image;
    }

    public String getTaste() {
        return taste;
    }

    public String getEffect() {
        return effect;
    }

    public List<Object> getAllergies() {
        return allergies;
    }

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
            String imageUri = jsonObject.getString("imageUri");
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