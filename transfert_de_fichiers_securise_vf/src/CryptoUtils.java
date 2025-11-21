import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Base64;

public class CryptoUtils {

    private static final String AES_ALGORITHM = "AES/ECB/PKCS5Padding";
    private static final byte[] SECRET_KEY = "MySecretKey12345".getBytes(); // 16 bytes

    // Chiffrement AES
    public static byte[] encrypt(byte[] data) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY, "AES");
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        return cipher.doFinal(data);
    }

    // Déchiffrement AES
    public static byte[] decrypt(byte[] encryptedData) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY, "AES");
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        return cipher.doFinal(encryptedData);
    }

    // Calcul du hash SHA-256
    public static String sha256(byte[] data) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(data);
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    // Lire tout le contenu d'un fichier
    public static byte[] readFileBytes(java.io.File file) throws Exception {
        java.nio.file.Path path = file.toPath();
        return java.nio.file.Files.readAllBytes(path);
    }

    // Écrire des bytes dans un fichier
    public static void writeFileBytes(byte[] data, java.io.File file) throws Exception {
        java.nio.file.Files.write(file.toPath(), data);
    }
}
