This project also implements RSA encryption to ensure the security of messages being sent over the network. RSA encryption is assymetrical key encryption which allows the server to create a private key that is used to decrypt messages and broadcast a public key which is used to encrypt messages to the network. Each client does the same so the server will have to manage the public key of every client but only one private key and the clients only need one public key and one private key. 

Here is a summary of the math behind RSA encryption: https://towardsdatascience.com/the-math-behind-rsa-910f88b94c36
The keys are created by finding two 1024 bit prime numbers using the probablePrime and secureRandom classes:
```java
    random = new SecureRandom();
    BigInteger p = BigInteger.probablePrime(bitLength, random);
    BigInteger q = BigInteger.probablePrime(bitLength, random);
```
These primes are then used to generate the private and public keys in general RSA fasion. 
```java
    n = p.multiply(q);
    phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
    do {
        e = new BigInteger(bitLength / 2, random);
    } while (e.gcd(phi).compareTo(BigInteger.ONE) != 0);
    d = e.modInverse(phi);
```
Try to crack it. Good luck.