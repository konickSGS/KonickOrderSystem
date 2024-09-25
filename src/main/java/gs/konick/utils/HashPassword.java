package gs.konick.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;

/**
 * Класс для хеширования пароля
 */
public class HashPassword {
    private static final String ALGORITHM = "SHA-256";
    private static final int SIZE = 128;

    private static final MessageDigest md;

    static {
        try {
            md = MessageDigest.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String hash(char[] password) {
        return hash(String.valueOf(password));
    }

    public static String hash(String password) {
        // Преобразуем пароль в байтовый массив и вычисляем хэш-значение
        byte[] byteHash = md.digest(password.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(byteHash);
    }

    public static boolean verify(char[] password, String token) {
        return Objects.equals(hash(password), token);
    }

}
