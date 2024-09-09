package com.example.fullmoonmenu;

public class ImageUtils {

    public static boolean isImageDataCorrupted(byte[] imageData) {
        // Check if the image data length is too short to be a valid image
        return imageData == null || imageData.length < 10;
    }
}