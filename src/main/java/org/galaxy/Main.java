package org.galaxy;

import org.web3j.abi.TypeEncoder;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint64;
import org.web3j.crypto.*;
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

    public static Credentials testLoadJsonCredentials(String keyFilePath) throws Exception {
        Credentials credentials = WalletUtils.loadJsonCredentials(
                PASSWORD,
                convertStreamToString(new FileInputStream(new File(keyFilePath)))
        );

        return credentials;

        // assertEquals(credentials, (CREDENTIALS));
    }

    public static Credentials testLoadJsonCredentials() throws Exception {
        return testLoadJsonCredentials("./keystore/key.json");
    }

    public static void main(String[] args) throws Exception {

        System.out.println("Hello world!");
        // testGenerateLightWalletFile(); // to generate key.json
        Credentials credentials = testLoadJsonCredentials("./keystore/key.json");
        System.out.println("Address: " + credentials.getAddress());
        System.out.println("Private Key: " + credentials.getEcKeyPair().getPrivateKey());
        System.out.println("Public Key: " + credentials.getEcKeyPair().getPublicKey());

        Bytes32 node = new Bytes32(
                Numeric.hexStringToByteArray(
                        "0xc6cbe29b02227ba1bb49c0da438c639867e06abe8377a4e69e75a8b705b17b10"
                )
        );

        final long date = (System.currentTimeMillis() / 1000 / (24 * 60 * 60));

        String dataForSign = "0x" +
                TypeEncoder.encode(
                        new DynamicStruct(
                                new Uint64(BigInteger.valueOf(date)),
                                node,
                                new Uint256(new BigInteger("1600000000000000"))
                        )
                );

        System.out.println(dataForSign);

        String ret = Numeric.toHexString(Numeric.hexStringToByteArray(dataForSign));
        System.out.println(ret);
        String hash = Hash.sha3(dataForSign);
        System.out.println("Hash: " + hash);

        Sign.SignatureData signature = Sign.signMessage(Numeric.hexStringToByteArray(dataForSign), credentials.getEcKeyPair());
        System.out.println(
                "0x" +
                Numeric.toHexStringNoPrefix(signature.getR()) +
                Numeric.toHexStringNoPrefix(signature.getS()) +
                Numeric.toHexStringNoPrefix(signature.getV())
        );

    }

}


/*

后端需要签名 => 对数据进行签名 => 数据是什么？=> 零散的数据被编码在一起（abi.encode）=> 对编码后的数据求hash => 用private_key与hash进行签名运算 => signature

链端需要验签 => signature以及零散的数据 => 零散的数据被编码在一起（abi.encode） => 对编码后的数据求hash => 验证hash与signature是否匹配 => 复原出地址

Node: 0xc6cbe29b02227ba1bb49c0da438c639867e06abe8377a4e69e75a8b705b17b10
Amount: 1600000000000000
Signature: 0x3c50c5d8242d78430eb2c54552aaff7843f7a43c0487d621e1910837bd74e987193853c679c8ba83737a0eb6a1406eef87d8b634c56c948a8ae8b767dbfd30331c
R: 0x3c50c5d8242d78430eb2c54552aaff7843f7a43c0487d621e1910837bd74e987
S: 0x193853c679c8ba83737a0eb6a1406eef87d8b634c56c948a8ae8b767dbfd3033
V: 0x1c

Address: 0xef678007D18427E6022059Dbc264f27507CD1ffC
Hash: 0x217bf2e50404f0fcdfbef0fb71c21c9f15fd0f9a4a5c9a5bff497676fcaacfc7

https://goerli.etherscan.io/address/0x12663c108813732Ba75664260a71c4f7261456aB#readContract

*/
