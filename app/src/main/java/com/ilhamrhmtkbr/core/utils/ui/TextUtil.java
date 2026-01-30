package com.ilhamrhmtkbr.core.utils.ui;

import android.text.TextUtils;

public class TextUtil {
    public static String check(String text, String messageIfNull){
        if (TextUtils.isEmpty(text) || text.equals("null")){
            return messageIfNull;
        } else {
            return text;
        }
    }

    // Method to capitalize the first letter of a string
    public static String capitalize(String text) {
        if (text == null || text.isEmpty()) {
            return text; // Return original if string is null or empty
        }

        // Create a new StringBuilder to build the capitalized string
        StringBuilder result = new StringBuilder();

        // Convert the first character to uppercase
        result.append(Character.toUpperCase(text.charAt(0)));

        // Append the rest of the string in lowercase
        result.append(text.substring(1).toLowerCase());

        // Convert StringBuilder to string and return it
        return result.toString();
    }

    // Fungsi untuk memformat angka menjadi format Rupiah
    public static String formatRupiah(int amount) {
        // Tambahkan pemisah ribuan secara manual
        String amountStr = String.format("%,d", amount).replace(',', '.');

        // Tambahkan simbol Rp di depan angka
        return "Rp. " + amountStr;
    }
}
