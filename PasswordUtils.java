import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

public class PasswordUtils {

    private static final int SALT_SIZE = 16;
    private static final int ITERATIONS = 600_000;
    private static final int KEY_LENGTH = 256;

    public static String hashPassword(String password) throws Exception {
        byte[] salt = new byte[SALT_SIZE];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);

        KeySpec spec = new PBEKeySpec(
            password.toCharArray(),
            salt,
            ITERATIONS,
            KEY_LENGTH
        );

        SecretKeyFactory factory =
            SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        byte[] hash = factory.generateSecret(spec).getEncoded();
        
        return Base64.getEncoder().encodeToString(salt)
            + ":"
            + Base64.getEncoder().encodeToString(hash);   
    }

    public static boolean verifyPassword(
        String password,
        String storedValue
    ) throws Exception {
        
        String[] parts = storedValue.split(":");

        byte[] salt = Base64.getDecoder().decode(parts[0]);
        byte[] expectedHash = Base64.getDecoder().decode(parts[1]);

        KeySpec spec = new PBEKeySpec(
            password.toCharArray(),
            salt,
            ITERATIONS,
            KEY_LENGTH
        );

        SecretKeyFactory factory =
            SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        byte[] actualHash = factory.generateSecret(spec).getEncoded();

        return java.security.MessageDigest.isEqual(
            expectedHash,
            actualHash
        );
    }
}