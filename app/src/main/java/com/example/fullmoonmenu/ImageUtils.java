package com.example.fullmoonmenu;

public class ImageUtils {

    // Method to check if the image data is corrupted
    // It returns true if the image data is null or its length is less than 10 bytes
    public static boolean isImageDataCorrupted(byte[] imageData) {
        // Check if the image data length is too short to be a valid image
        return imageData == null || imageData.length < 10;
    }
}