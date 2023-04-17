package com.mybatisplus_comp3334.util;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.Timestamp;

@Component
public class HashUtils {
    public String hash(String studentID, Timestamp currentTime) throws Exception {

        String h;
        /*
         * Implement your code here
         */
        String originalString = studentID + currentTime;
        // Generate the hash code by SHA-256 algorithm
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] hash = messageDigest.digest(originalString.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            // Make sure that every hash string is two digits
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        h = hexString.toString();
        return h;
    }
}
