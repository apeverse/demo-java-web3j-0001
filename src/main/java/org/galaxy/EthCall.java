package org.galaxy;

import org.junit.jupiter.api.Test;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

/**
 * @Author Ant
 * @Date 2022/10/24
 * @Description: 调用合约方法
 */
public class EthCall {
    @Test
    public void testEthCall() throws IOException {
        String reqUrl = "https://eth-goerli.g.alchemy.com/v2/-s1zkDpkEmnjF4wIk8pLsiJBuxWelYV0";

        final Web3j web3j = Web3j.build(new HttpService(reqUrl));
        // https://goerli.etherscan.io/address/0x966a31ff3eb02144369887d4bd3a9238a7ff925c#code
        Function function = new Function("addNum", Arrays.asList(new Uint8(8)),
                Collections.emptyList());
        final String functionEncode  = FunctionEncoder.encode(function);
        System.out.println(functionEncode);
        String from = "0xb6B78b6F7C461d9D33d5D8c9f9366215C416AeB1";
        Transaction transaction = Transaction.createEthCallTransaction(from,
                "0x966a31ff3eb02144369887d4bd3a9238a7ff925c",functionEncode);
        final String value = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST)
                .send().getValue();
        System.out.println(Numeric.toBigInt(value));
    }
}



/*

curl https://eth-goerli.g.alchemy.com/v2/-s1zkDpkEmnjF4wIk8pLsiJBuxWelYV0 \
--request POST --header "Content-Type: application/json" --data \
'{
    "jsonrpc":"2.0",
    "method":"eth_call",
    "params":[
        {
            "to":"0x966a31ff3eb02144369887d4bd3a9238a7ff925c",
            "data":"0xd56aae550000000000000000000000000000000000000000000000000000000000000008"
        }
    ],
    "id":1
}'

# return value
{
    "jsonrpc":"2.0",
    "id":1,
    "result":"0x0000000000000000000000000000000000000000000000000000000000000009"
}

*/




