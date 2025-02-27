package com.erebos.flu.utils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.security.MessageDigest.getInstance;

/**
 * Utility class providing helper methods for hashing operations.
 * This class contains methods for generating secure hashes of strings.
 */
public class HashUtils {

    // The multiplier 33 is widely used in hash functions.
    // Reason: 33 provides a good balance between performance and collision resistance.
    private static final int HASH_MULTIPLIER = 33;


    /**
     * Private constructor to prevent instantiation of utility class.
     *
     * @throws IllegalStateException when called
     */
    private HashUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Generates a SHA-256 hash of the input string.
     * The hash is returned as a 64-character hexadecimal string.
     *
     * @param s the string to hash
     * @return the SHA-256 hash as a hexadecimal string
     * @throws IllegalStateException if the SHA-256 algorithm is not available
     */
    public static String perfectHash(final String s) {
        try {
            MessageDigest md = getInstance("SHA-256");
            md.update(s.getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest();
            return String.format("%064x", new BigInteger(1, digest));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Error during hashing!");
        }
    }

    /**
     * Computes a hash value for a given string.
     * <p>
     * This hash function is based on the DJB2 hash algorithm and uses a multiplier
     * of 33, as it provides a good balance between performance and uniform distribution.
     * </p>
     *
     * @param str The input string for which the hash value is computed.
     * @return A positive 32-bit hash value of the input string.
     * If the computed value is negative, its absolute value is returned.
     */
    public static int myHashCode(String str) {
        int hash = 0;
        for (int i = 0; i < str.length(); i++) {
            hash = (hash * HASH_MULTIPLIER) + str.charAt(i);
            hash = hash & 0xFFFFFFFF; // Ensures 32-bit representation
        }
        return Math.abs(hash); // Ensures the hash value is always positive
    }

}






