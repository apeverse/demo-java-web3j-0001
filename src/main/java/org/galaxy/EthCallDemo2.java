package org.galaxy;

import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.Test;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Array;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Author Ant
 * @Date 2022/10/24
 * @Description: 调用合约方法
 */
public class EthCallDemo2 {
    public static void main(String[] args) throws IOException {
        String reqUrl = "https://eth-goerli.g.alchemy.com/v2/-s1zkDpkEmnjF4wIk8pLsiJBuxWelYV0";

        final Web3j web3j = Web3j.build(new HttpService(reqUrl));
        // https://goerli.etherscan.io/address/0x8A5FD1838Bad8f1731Ea31c2cF2ba3b8C9942b58#code
        Function function = new Function("getMessage", Collections.emptyList(),
               Arrays.asList(new TypeReference<Utf8String>() {})  );
        final String functionEncode  = FunctionEncoder.encode(function);
        String from = "0xb6B78b6F7C461d9D33d5D8c9f9366215C416AeB1";
        Transaction transaction = Transaction.createEthCallTransaction(from,
                "0x8A5FD1838Bad8f1731Ea31c2cF2ba3b8C9942b58",functionEncode);
        final String value = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send().getValue();

        final List<Type> decode = FunctionReturnDecoder.decode(value, function.getOutputParameters());
        System.out.println(decode.get(0).getValue());
    }

    /**
     * 测试发起交易
     */
    @Test
    public void testSendTx() throws Exception {
        String reqUrl = "https://eth-goerli.g.alchemy.com/v2/-s1zkDpkEmnjF4wIk8pLsiJBuxWelYV0";

        final Web3j web3j = Web3j.build(new HttpService(reqUrl));
        // https://goerli.etherscan.io/address/0x8A5FD1838Bad8f1731Ea31c2cF2ba3b8C9942b58#code

        // 获取私钥
        final Credentials credentials = Main.testLoadJsonCredentials();

        // 获取nonce
        final BigInteger nonce = web3j.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST)
                .send().getTransactionCount();


        // 获取

        Function function = new Function("setMessage",
                Arrays.asList(new Utf8String("111")),
                Collections.emptyList());
         String encodeData = FunctionEncoder.encode(function);
        // 创建交易
       // createTransaction(BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to, String data)
        final RawTransaction transaction = RawTransaction.createTransaction(nonce ,
                new BigInteger("35000000000"), BigInteger.valueOf(400000L),
                "0x8A5FD1838Bad8f1731Ea31c2cF2ba3b8C9942b58", encodeData);
        final byte[] signMesageBytes = TransactionEncoder.signMessage(transaction,credentials);
        final EthSendTransaction send = web3j.ethSendRawTransaction(Numeric.toHexString(signMesageBytes)).send();
        System.out.println("nonce:  "+nonce);
        System.out.println(JSONUtil.toJsonStr(send));
    }

    @Test
    public void testGetBalance() throws Exception {
        String reqUrl = "https://eth-goerli.g.alchemy.com/v2/-s1zkDpkEmnjF4wIk8pLsiJBuxWelYV0";

        final Web3j web3j = Web3j.build(new HttpService(reqUrl));
        // https://goerli.etherscan.io/address/0x8A5FD1838Bad8f1731Ea31c2cF2ba3b8C9942b58#code

        // 获取私钥
        final Credentials credentials = Main.testLoadJsonCredentials();

        final BigInteger balance = web3j.ethGetBalance(credentials.getAddress(), DefaultBlockParameterName.LATEST)
                .send().getBalance();
        System.out.println(balance);
    }


    /**
     * 添加测试转账demo
     * @throws Exception
     */
    @Test
    public void testTranform() throws Exception {

        String reqUrl = "https://eth-goerli.g.alchemy.com/v2/-s1zkDpkEmnjF4wIk8pLsiJBuxWelYV0";

        final Web3j web3j = Web3j.build(new HttpService(reqUrl));
        // https://goerli.etherscan.io/address/0x8A5FD1838Bad8f1731Ea31c2cF2ba3b8C9942b58#code

        // 获取私钥
        final Credentials credentials = Main.testLoadJsonCredentials();

        // 获取nonce
        final BigInteger nonce = web3j.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST)
                .send().getTransactionCount();


        String to = "0xe26f015ba6b8c400cE327CeEBE34B717e6897e69";
        // 创建交易
        /**
         *  createTransaction(
         *             BigInteger nonce,
         *             BigInteger gasPrice,
         *             BigInteger gasLimit,
         *             String to,
         *             BigInteger value,
         *             String data)
         */

        final RawTransaction transaction = RawTransaction.createTransaction(nonce ,
                new BigInteger("35000000000"), BigInteger.valueOf(400000L),
                to,new BigInteger("900000000000000000"),"");

        final byte[] signMesageBytes = TransactionEncoder.signMessage(transaction,credentials);
        final EthSendTransaction send = web3j.ethSendRawTransaction(Numeric.toHexString(signMesageBytes)).send();
        System.out.println("nonce:  "+nonce);
        System.out.println(JSONUtil.toJsonStr(send));
    }
}








