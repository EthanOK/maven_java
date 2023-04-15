package com.utils;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.Utils;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bytes;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import io.github.cdimascio.dotenv.Dotenv;

public class SelectTokenURIMulTokenId {

        static Dotenv dotenv = Dotenv.load();

        public static String RPC = dotenv.get("ALCHEMY_GOERLI_URL");

        public static void main(String[] args) throws Exception {
                Web3j web3j = Web3j
                                .build(new HttpService(RPC));

                // 获取合约地址和ABI
                String multicalladdress = "0x8c6f3bF9Ed05afa8DC0D3f08C2DB4a6E731a3574";
                String contractAddress = "0x0d3e02768ab63516ab5d386fad462214ca3e6a86";
                String[] tokenIds = { "10", "15", "10", "1" };

                System.out.println("`````````````````````````````````````");
                Map<String, String> mapsTokenURI = getTokenURI(web3j, multicalladdress, contractAddress, tokenIds);
                for (Map.Entry<String, String> entry : mapsTokenURI.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();
                        System.out.println("tokenId: " + key + ", tokenURI: " + value);
                }

        }

        public static Map<String, String> getTokenURI(Web3j web3j, String multicalladdress, String tokenAddress,
                        String[] tokenIds) throws Exception {
                Set<String> set = new HashSet<>(Arrays.asList(tokenIds));
                String[] uniqueTokenIds = set.toArray(new String[0]);
                Map<String, String> map = new HashMap<>();

                int len = uniqueTokenIds.length;

                // struct Call {
                // address target;
                // bytes callData;
                // }
                // function aggregateStaticCall(
                // Call[] memory calls
                // ) public view returns (uint256 blockNumber, bytes[] memory returnData)
                DynamicStruct[] callDatas = new DynamicStruct[len];

                for (int i = 0; i < len; i++) {
                        Function tokenURI = new Function("tokenURI",
                                        Arrays.asList(new Uint256(new BigInteger(uniqueTokenIds[i]))),
                                        Arrays.asList(new TypeReference<Utf8String>() {
                                        }));
                        String encodedtokenURI = FunctionEncoder.encode(tokenURI);
                        byte[] bytesData = Numeric.hexStringToByteArray(encodedtokenURI);
                        callDatas[i] = new DynamicStruct(
                                        new Address(tokenAddress),
                                        new DynamicBytes(bytesData));
                }

                DynamicArray<DynamicStruct> callDatasArray = new DynamicArray<DynamicStruct>(DynamicStruct.class,
                                callDatas);

                Function function = new Function("aggregateStaticCall", Arrays.asList(callDatasArray),
                                Arrays.asList(new TypeReference<Uint256>() {
                                }, new TypeReference<DynamicArray<DynamicBytes>>() {
                                }));

                // method2:
                // Function functionString = new Function(" ", Arrays.asList(),
                // Arrays.asList(new TypeReference<Utf8String>() {
                // }));
                String encodedFunction = FunctionEncoder.encode(function);
                EthCall ethCall = web3j.ethCall(
                                Transaction.createEthCallTransaction("0x0d3e02768ab63516ab5d386fad462214ca3e6a86",
                                                multicalladdress,
                                                encodedFunction),
                                DefaultBlockParameterName.LATEST)
                                .sendAsync().get();

                // 解析返回数据
                List<Type> response = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
                // Uint256 blockNumber = (Uint256) response.get(0);
                DynamicArray<DynamicBytes> returnDataArray = (DynamicArray<DynamicBytes>) response.get(1);
                if (returnDataArray.getValue().size() != len) {
                        throw new Exception("Mismatched length");
                }
                // bytes[]
                List<DynamicBytes> listBytes = returnDataArray.getValue();

                for (int i = 0; i < listBytes.size(); i++) {
                        String bytesHex = Numeric.toHexString(listBytes.get(i).getValue());
                        // method1: bytes decode String
                        byte[] decodedValues = FunctionReturnDecoder.decodeDynamicBytes(
                                        bytesHex);
                        String tokenURI = new String(decodedValues, "UTF-8");
                        map.put(uniqueTokenIds[i], tokenURI);

                        // method2:
                        // List<Type> listTokenURI = FunctionReturnDecoder.decode(bytesHex,
                        // functionString.getOutputParameters());
                        // listTokenURI.get(0).getValue();
                        // System.out.println(listTokenURI.get(0).getValue().toString());
                }
                return map;

        }

}
