package com.utils;

import java.math.BigInteger;
import java.security.SignatureException;
import java.util.Arrays;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.crypto.Sign.SignatureData;
import org.web3j.utils.Numeric;

public class SignPrefixedHexMessage {
    public static void main(String[] args) throws SignatureException {
        // Account #0: 0xf39Fd6e51aad88F6F4ce6aB8827279cffFb92266 (10000 ETH)
        // Private Key:
        // 0xac0974bec39a17e36ba4a6b4d238ff944bacb478cbed5efcae784d7bf4f2ff80

        String privateKeyHex = "0xac0974bec39a17e36ba4a6b4d238ff944bacb478cbed5efcae784d7bf4f2ff80";

        // messageHashHex = keccak256(abi.encode(address, uint256))
        String messageHashHex = "0xf6896007477ab25a659f87c4f8c5e3baac32547bf305e77aa57743046e10578b";

        String signerAddress = getAddressFromPrivateKey(privateKeyHex);

        String signatureString = getEthereumSignature(messageHashHex, privateKeyHex);

        boolean verifyResult = verifyEthereumSignature(messageHashHex, signatureString, signerAddress);

        System.out.println("verifyResult:" + verifyResult);
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

    public static boolean verifyEthereumSignature(String messageHashHex, String signatureHashHex, String signer)
            throws SignatureException {
        byte[] messageHash = Numeric.hexStringToByteArray(messageHashHex);
        Sign.SignatureData signatureData = convertSignatureToData(signatureHashHex);
        BigInteger publicKey = Sign.signedPrefixedMessageToKey(messageHash, signatureData);

        if (publicKey != null) {
            String address = "0x" + Keys.getAddress(publicKey);
            return signer.equalsIgnoreCase(address);
        }
        return false;
    }

    public static String getEthereumSignature(String messageHashHex,
            String privateKeyHex) {
        byte[] messageHash = Numeric.hexStringToByteArray(messageHashHex);
        Credentials credentials = Credentials.create(privateKeyHex);
        Sign.SignatureData signatureData = Sign.signPrefixedMessage(messageHash, credentials.getEcKeyPair());
        String signature = Numeric.toHexString(signatureData.getR())
                + Numeric.toHexStringNoPrefix(signatureData.getS())
                + Numeric.toHexStringNoPrefix(signatureData.getV());
        return signature;
    }

    public static String getAddressFromPrivateKey(String privateKeyHex) {
        Credentials credentials = Credentials.create(privateKeyHex);
        String address = credentials.getAddress();
        return address;
    }
}
