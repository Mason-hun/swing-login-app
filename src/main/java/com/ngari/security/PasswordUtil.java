package com.ngari.security;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.security.MessageDigest;
import java.util.Arrays;

public class PasswordUtil {

    private static final int ITERATIONS = 600_000;
    private static final int SALT_LENGTH = 16;
    private static final int KEY_LENGTH = 256;

    public static String hashPassword(String password) throws Exception {

        // Generate a random salt
        byte[] salt = new byte[SALT_LENGTH];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);

        // Derive a password hash
        KeySpec spec = new PBEKeySpec(
                password.toCharArray(),
                salt,
                ITERATIONS,
                KEY_LENGTH
        );

        SecretKeyFactory factory =
                SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        byte[] hash = factory.generateSecret(spec).getEncoded();

        // Encode salt and hash for database storage
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String encodedHash = Base64.getEncoder().encodeToString(hash);

        return ITERATIONS + ":" + encodedSalt + ":" + encodedHash;
    }

    public static boolean verifyPassword(
            String submittedPassword,
            String storedValue
    ) throws Exception {

        // 1. Separate the stored iteration count, salt and hash
        String[] parts = storedValue.split(":");

        if (parts.length != 3) {
            return false;
        }

        // 2. Retrieve the original hashing parameters
        int iterations = Integer.parseInt(parts[0]);

        byte[] salt = Base64.getDecoder().decode(parts[1]);

        byte[] expectedHash = Base64.getDecoder().decode(parts[2]);

        // 3. Derive a new hash using the submitted password
        PBEKeySpec spec = new PBEKeySpec(
                submittedPassword.toCharArray(),
                salt,
                iterations,
                expectedHash.length * 8
        );

        try {

            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            byte[] submittedHash =
                    factory.generateSecret(spec).getEncoded();

            // 4. Compare the newly generated hash with the stored hash
            return MessageDigest.isEqual(expectedHash, submittedHash);

        } finally {

            // 5. Clear sensitive password material where possible
            spec.clearPassword();

        }
    }
}