package shop.mtcoding.getintherelogin.util;

import java.security.SecureRandom;
import java.util.Base64;

public class RandomPasswordGenerator {

    private static final int PASSWORD_LENGTH = 16;

    public static String generateRandomPassword() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[PASSWORD_LENGTH];
        random.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }
}
