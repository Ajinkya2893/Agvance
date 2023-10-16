package encryption;

import org.apache.commons.codec.binary.Base64;

public class Base64EncryptionUtils {

    public String base64_Encrypt(String text) {
        byte[] bytesToEncrypt = text.getBytes();
        byte[] encryptedBytes = Base64.encodeBase64(bytesToEncrypt);
        return new String(encryptedBytes);
    }

    public String base64_Decrypt(String encryptedText) {
        byte[] encryptedBytes = encryptedText.getBytes();
        byte[] decryptedBytes = Base64.decodeBase64(encryptedBytes);
        return new String(decryptedBytes);
    }

}
