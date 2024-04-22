package edu.neu.csye6200.utils;

import java.util.Base64;

public class Utility {

    public static String encryptPassword(String password) {
        return Base64.getEncoder().encodeToString(password.getBytes());
    }
}
