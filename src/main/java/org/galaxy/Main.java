package org.galaxy;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.WalletUtils;
import org.web3j.utils.Numeric;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;

public class Main {


    public static final String PRIVATE_KEY_STRING = "a392604efc2fad9c0b3da43b5f698a2e3f270f170d859912be0d54742275c5f6";
    static final String PUBLIC_KEY_STRING = "0x506bc1dc099358e5137292f4efdd57e400f29ba5132aa5d12b18dac1c1f6aaba645c0b7b58158babbfa6c6cd5a48aa7340a8749176b120e8516216787a13dc76";
    public static final String ADDRESS = "0xef678007d18427e6022059dbc264f27507cd1ffc";
    public static final String ADDRESS_NO_PREFIX = Numeric.cleanHexPrefix("0xef678007d18427e6022059dbc264f27507cd1ffc");
    public static final String PASSWORD = "Insecure Pa55w0rd";
    public static final String MNEMONIC = "scatter major grant return flee easy female jungle vivid movie bicycle absent weather inspire carry";
    static final BigInteger PRIVATE_KEY = Numeric.toBigInt("a392604efc2fad9c0b3da43b5f698a2e3f270f170d859912be0d54742275c5f6");
    static final BigInteger PUBLIC_KEY = Numeric.toBigInt("0x506bc1dc099358e5137292f4efdd57e400f29ba5132aa5d12b18dac1c1f6aaba645c0b7b58158babbfa6c6cd5a48aa7340a8749176b120e8516216787a13dc76");
    static final ECKeyPair KEY_PAIR;
    public static final Credentials CREDENTIALS;

    static {
        KEY_PAIR = new ECKeyPair(PRIVATE_KEY, PUBLIC_KEY);
        CREDENTIALS = Credentials.create(KEY_PAIR);
    }

    private static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    private static void testGenerateWalletFile(String fileName) throws Exception {
        Credentials credentials =
                WalletUtils.loadCredentials(PASSWORD, new File("./keystore", fileName));

        // assertEquals(credentials, (CREDENTIALS));
    }


    public static void testGenerateLightWalletFile() throws Exception {
        String fileName = WalletUtils.generateWalletFile(PASSWORD, KEY_PAIR, new File("./keystore"), false);
        testGenerateWalletFile(fileName);
    }

    public static void testLoadJsonCredentials() throws Exception {
        Credentials credentials =
                WalletUtils.loadJsonCredentials(
                        PASSWORD,
                        convertStreamToString(
                                new FileInputStream(new File("/tmp/a.json"))
                        ));

        // assertEquals(credentials, (CREDENTIALS));
        System.out.println(credentials.getAddress());
        System.out.println(credentials.getEcKeyPair().getPrivateKey());
        System.out.println(credentials.getEcKeyPair().getPublicKey());
    }

    public static void main(String[] args) throws Exception {

        System.out.println("Hello world!");
        // testGenerateLightWalletFile();
        testLoadJsonCredentials();
        System.out.println(new FileInputStream(new File("/tmp/a.json")));

    }
}
