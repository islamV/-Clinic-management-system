package com.clinicmanagementsystem.clinicmanagementsystem;

public class SecurityController {
    public static String hashPassword(String password) {
        StringBuilder encryptedPassword = new StringBuilder();
        for (char ch : password.toCharArray()) {
            char encryptedChar = (char) (ch + 5);
            encryptedPassword.append(encryptedChar);
        }
        return encryptedPassword.toString();
    }
    }

