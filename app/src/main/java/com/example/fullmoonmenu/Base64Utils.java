package com.example.fullmoonmenu;

import android.util.Base64;

public class Base64Utils {

    public static boolean isValidBase64(String base64) {
        try {
            Base64.decode(base64, Base64.DEFAULT);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}