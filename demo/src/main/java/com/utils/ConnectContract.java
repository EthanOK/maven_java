package com.utils;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.http.HttpService;

public class ConnectContract {

    public static String RPC = "https://eth-goerli.g.alchemy.com/v2/83yL2qXH68vnTkzzida15zCVNGZCy1JO";

    public static void main(String[] args) throws IOException {
        Web3j web3j = Web3j
                .build(new HttpService(RPC));

        // 获取合约地址和ABI
        String contractAddress = "0x0d3e02768ab63516ab5d386fad462214ca3e6a86";
        String tokenId = "10";
        String uri = getTokenURI(web3j, contractAddress, tokenId);
        System.out.println("TokenURI:" + uri);
        String owner = getTOwnerOf(web3j, contractAddress, tokenId);
        System.out.println("OwnerOf:" + owner);
        String name = getName(web3j, contractAddress);
        System.out.println("Name:" + name);
        String symbol = getSymbol(web3j, contractAddress);
        System.out.println("Symbol:" + symbol);
    }

    public static String getTokenURI(Web3j web3j, String contractAddress,
            String tokenId) throws IOException {
        BigInteger tokenId_ = new BigInteger(tokenId);
        Function function = new Function("tokenURI", Arrays.asList(new Uint256(tokenId_)),
                Arrays.asList(new TypeReference<DynamicBytes>() {
                }));
        String encodedFunction = FunctionEncoder.encode(function);
        EthCall response = web3j.ethCall(
                Transaction.createEthCallTransaction("0x0d3e02768ab63516ab5d386fad462214ca3e6a86", contractAddress,
                        encodedFunction),
                DefaultBlockParameterName.LATEST)
                .send();
        List<Type> outputParams = FunctionReturnDecoder.decode(response.getValue(), function.getOutputParameters());
        DynamicBytes dynamicBytes = (DynamicBytes) outputParams.get(0);
        byte[] bytesValue = dynamicBytes.getValue();
        String stringValue = new String(bytesValue);
        return stringValue;
    }

    public static String getName(Web3j web3j, String contractAddress) throws IOException {

        Function function = new Function("name", Arrays.asList(),
                Arrays.asList(new TypeReference<DynamicBytes>() {
                }));
        String encodedFunction = FunctionEncoder.encode(function);
        EthCall response = web3j.ethCall(
                Transaction.createEthCallTransaction("0x0d3e02768ab63516ab5d386fad462214ca3e6a86", contractAddress,
                        encodedFunction),
                DefaultBlockParameterName.LATEST)
                .send();
        List<Type> outputParams = FunctionReturnDecoder.decode(response.getValue(), function.getOutputParameters());
        DynamicBytes dynamicBytes = (DynamicBytes) outputParams.get(0);
        byte[] bytesValue = dynamicBytes.getValue();
        String stringValue = new String(bytesValue);
        return stringValue;
    }

    public static String getSymbol(Web3j web3j, String contractAddress) throws IOException {

        Function function = new Function("symbol", Arrays.asList(),
                Arrays.asList(new TypeReference<DynamicBytes>() {
                }));
        String encodedFunction = FunctionEncoder.encode(function);
        EthCall response = web3j.ethCall(
                Transaction.createEthCallTransaction("0x0d3e02768ab63516ab5d386fad462214ca3e6a86", contractAddress,
                        encodedFunction),
                DefaultBlockParameterName.LATEST)
                .send();
        List<Type> outputParams = FunctionReturnDecoder.decode(response.getValue(), function.getOutputParameters());
        DynamicBytes dynamicBytes = (DynamicBytes) outputParams.get(0);
        byte[] bytesValue = dynamicBytes.getValue();
        String stringValue = new String(bytesValue);
        return stringValue;
    }

    public static String getTOwnerOf(Web3j web3j, String contractAddress,
            String tokenId) throws IOException {
        BigInteger tokenId_ = new BigInteger(tokenId);
        Function function = new Function("ownerOf", Arrays.asList(new Uint256(tokenId_)),
                Arrays.asList(new TypeReference<Address>() {
                }));
        String encodedFunction = FunctionEncoder.encode(function);
        EthCall response = web3j.ethCall(
                Transaction.createEthCallTransaction("0x0d3e02768ab63516ab5d386fad462214ca3e6a86", contractAddress,
                        encodedFunction),
                DefaultBlockParameterName.LATEST)
                .send();
        List<Type> outputParams = FunctionReturnDecoder.decode(response.getValue(), function.getOutputParameters());

        Object bytesValue = outputParams.get(0).getValue();
        String stringValue = (String) bytesValue;
        return stringValue;
    }
}