package crypto;
import java.math.BigInteger;
import java.security.SecureRandom;

public class RSAKey {
    private final int bitLength = 2048;
    private SecureRandom random;
    private BigInteger e;
    private BigInteger n;
    private BigInteger phi;
    private BigInteger d;

    public RSAKey() {
        random = new SecureRandom();
        BigInteger p = BigInteger.probablePrime(bitLength, random);
        BigInteger q = BigInteger.probablePrime(bitLength, random);
        n = p.multiply(q);
        phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        do {
            e = new BigInteger(bitLength / 2, random);
        } while (e.gcd(phi).compareTo(BigInteger.ONE) != 0);

        d = e.modInverse(phi);
    }

    public String getPublicKey() {
        return String.format("%d,%d", n, e);
    }

    public String decryptCiphertext(String ciphertext) {
        BigInteger cipherInt = new BigInteger(ciphertext);
        BigInteger decryptedInt = cipherInt.modPow(d, n);
        byte[] decryptedBytes = decryptedInt.toByteArray();
        return new String(decryptedBytes);
    }
}