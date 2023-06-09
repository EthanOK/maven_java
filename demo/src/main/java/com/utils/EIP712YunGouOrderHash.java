package com.utils;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SignatureException;
import java.util.Arrays;
import java.util.HashMap;

import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.crypto.StructuredDataEncoder;
import org.web3j.crypto.Sign.SignatureData;
import org.web3j.utils.Numeric;

public class EIP712YunGouOrderHash {
    static String jsonMessageString;

    public static void main(String[] args) throws IOException, RuntimeException, SignatureException {
        String JSONFilePath = "demo/src/main/java/com/utils/structured_data_json_files/YunGouOrderStructData.json";
        jsonMessageString = new String(
                Files.readAllBytes(Paths.get(JSONFilePath).toAbsolutePath()), StandardCharsets.UTF_8);
        String verifyAddress = "0xf39Fd6e51aad88F6F4ce6aB8827279cffFb92266";
        StructuredDataEncoder dataEncoder = new StructuredDataEncoder(jsonMessageString);

        // TODO: getorderHash
        byte[] dataHash = dataEncoder.hashMessage(
                dataEncoder.jsonMessageObject.getPrimaryType(),
                (HashMap<String, Object>) dataEncoder.jsonMessageObject.getMessage());
        String dataHashString = Numeric.toHexString(dataHash);
        System.out.println("keccak256(abi.encode(TYPE_HASH, parameters)):");
        System.out.println("YunGou orderHash: " + dataHashString);
        // get hashStructuredData

        byte[] hashStructuredMessage = dataEncoder.hashStructuredData();
        String hashStructuredMessageHex = Numeric.toHexString(hashStructuredMessage);
        System.out.println(hashStructuredMessageHex);

        String signature = "0x6193dd8ea70d5cbfac407cf6d88ef0cd73756bbd9e655aae75e0d217179dd6fe2769a39ceeefd6982b3c8bcc217af57d8a929261200c1ae7304c107dc6f6dc4b1b";
        Boolean result = verify(hashStructuredMessageHex, signature, verifyAddress);
        System.out.println("verify result: " + result);
    }

    public static Boolean verify(String hashHex, String signatureHex, String verifyAddress) throws SignatureException {
        byte[] messageHash = Numeric.hexStringToByteArray(hashHex);
        Sign.SignatureData signatureData = convertSignatureToData(signatureHex);
        BigInteger publicKey = Sign.signedMessageHashToKey(messageHash, signatureData);

        if (publicKey != null) {
            String address = "0x" + Keys.getAddress(publicKey);
            return verifyAddress.equalsIgnoreCase(address);
        }
        return false;

    }

    public static SignatureData convertSignatureToData(String signatureString) {
        // 将签名字符串拆分为字节数组
        byte[] signatureBytes = Numeric.hexStringToByteArray(signatureString);
        // 将签名字节数组解析为SignatureData对象
        byte[] r = Arrays.copyOfRange(signatureBytes, 0, 32);
        byte[] s = Arrays.copyOfRange(signatureBytes, 32, 64);
        byte v = signatureBytes[64];

        return new SignatureData(v, r, s);
    }

}
