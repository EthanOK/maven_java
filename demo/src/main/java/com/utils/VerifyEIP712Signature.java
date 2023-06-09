package com.utils;

import java.math.BigInteger;
import java.security.SignatureException;
import java.util.Arrays;

import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.crypto.Sign.SignatureData;
import org.web3j.utils.Numeric;

public class VerifyEIP712Signature {
    public static String verifyReturnAddress(String hashHex, String signatureHex) throws SignatureException, Exception {
        byte[] messageHash = Numeric.hexStringToByteArray(hashHex);
        Sign.SignatureData signatureData = convertSignatureToData(signatureHex);
        BigInteger publicKey = Sign.signedMessageHashToKey(messageHash, signatureData);

        if (publicKey != null) {
            String address = "0x" + Keys.getAddress(publicKey);
            return address;
        } else {
            throw new Exception(
                    "publicKey is null");

        }
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
