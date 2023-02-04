package se.na.shoedatabase.dao;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encrypt {

    public static String encryptSHA3(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA3-256");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hastText = number.toString(16);
            while (hastText.length() < 64) {
                hastText = "0" + hastText;
            }
            return hastText;
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error while encrypting string with SHA3-256");
            throw new RuntimeException(e);
        }
    }
}