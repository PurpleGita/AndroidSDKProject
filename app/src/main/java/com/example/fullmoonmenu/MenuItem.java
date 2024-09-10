package com.example.fullmoonmenu;

import java.io.Serializable;
import java.util.List;

public class MenuItem implements Serializable {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MenuItem menuItem = (MenuItem) o;

        if (id != menuItem.id) return false;
        if (isFood != menuItem.isFood) return false;
        if (price != menuItem.price) return false;
        if (!name.equals(menuItem.name)) return false;
        if (!currency.equals(menuItem.currency)) return false;
        if (!java.util.Arrays.equals(image, menuItem.image)) return false;
        if (!taste.equals(menuItem.taste)) return false;
        if (!effect.equals(menuItem.effect)) return false;
        return allergies.equals(menuItem.allergies);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (isFood ? 1 : 0);
        result = 31 * result + name.hashCode();
        result = 31 * result + price;
        result = 31 * result + currency.hashCode();
        result = 31 * result + java.util.Arrays.hashCode(image);
        result = 31 * result + taste.hashCode();
        result = 31 * result + effect.hashCode();
        result = 31 * result + allergies.hashCode();
        return result;
    }
}