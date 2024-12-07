package crypto;
import java.math.BigInteger;

public class PublicKey {
    private BigInteger e;
    private BigInteger n;

    public PublicKey(String key) {
        n = new BigInteger(key.split(",")[0]);
        e = new BigInteger(key.split(",")[1]);
    }

    public String getPublicKey() {
        return String.format("%d,%d", n, e);
    }

    public String encryptText(String text) {
        BigInteger cipherInt = new BigInteger(text.getBytes());
        return cipherInt.modPow(e, n).toString();
    }
}