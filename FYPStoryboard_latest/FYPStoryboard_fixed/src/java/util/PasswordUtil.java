package util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Password hashing utility using PBKDF2.
 *
 * Stored format:
 *   {@code pbkdf2$<iterations>$<saltBase64>$<hashBase64>}
 */
public final class PasswordUtil {

    private static final String PREFIX = "pbkdf2";
    private static final int DEFAULT_ITERATIONS = 120000;
    private static final int SALT_BYTES = 16;
    private static final int KEY_LENGTH_BITS = 256;

    private PasswordUtil() {}

    public static String hash(String password) {
        if (password == null) password = "";
        byte[] salt = new byte[SALT_BYTES];
        new SecureRandom().nextBytes(salt);
        int iterations = DEFAULT_ITERATIONS;
        byte[] derived = pbkdf2(password.toCharArray(), salt, iterations, KEY_LENGTH_BITS);

        return PREFIX + "$" + iterations + "$" +
                Base64.getEncoder().encodeToString(salt) + "$" +
                Base64.getEncoder().encodeToString(derived);
    }

    public static boolean verify(String password, String stored) {
        if (stored == null) return false;
        if (!stored.startsWith(PREFIX + "$")) {
            // Not hashed format (legacy plaintext)
            return stored.equals(password);
        }

        String[] parts = stored.split("\\$");
        if (parts.length != 4) return false;

        int iterations;
        try {
            iterations = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            return false;
        }

        byte[] salt;
        byte[] hash;
        try {
            salt = Base64.getDecoder().decode(parts[2]);
            hash = Base64.getDecoder().decode(parts[3]);
        } catch (IllegalArgumentException e) {
            return false;
        }

        byte[] test = pbkdf2(password == null ? new char[0] : password.toCharArray(), salt, iterations, hash.length * 8);
        return MessageDigest.isEqual(hash, test);
    }

    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int keyLenBits) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLenBits);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return skf.generateSecret(spec).getEncoded();
        } catch (Exception e) {
            // Fallback to SHA1 variant if server/JDK is old
            try {
                PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLenBits);
                SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
                return skf.generateSecret(spec).getEncoded();
            } catch (Exception ex) {
                throw new RuntimeException("Password hashing not supported: " + ex.getMessage(), ex);
            }
        }
    }
}
