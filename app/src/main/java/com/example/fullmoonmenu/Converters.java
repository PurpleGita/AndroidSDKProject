package com.example.fullmoonmenu;

import androidx.room.TypeConverter;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.List;

public class Converters {
    @TypeConverter
    public static String fromList(List<Object> list) {
        JSONArray jsonArray = new JSONArray();
        for (Object item : list) {
            jsonArray.put(item.toString());
        }
        return jsonArray.toString();
    }

    @TypeConverter
    public static List<Object> fromString(String value) {
        List<Object> list = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(value);
            for (int i = 0; i < jsonArray.length(); i++) {
                list.add(jsonArray.get(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}