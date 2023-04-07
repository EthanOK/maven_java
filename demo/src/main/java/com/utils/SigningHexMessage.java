package com.utils;

import java.math.BigInteger;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECDSASignature;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.crypto.Sign.SignatureData;
import org.web3j.utils.Numeric;

public class SigningHexMessage {
    public static void main(String[] args) throws SignatureException {
        // Account #0: 0xf39Fd6e51aad88F6F4ce6aB8827279cffFb92266 (10000 ETH)
        // Private Key:
        // 0xac0974bec39a17e36ba4a6b4d238ff944bacb478cbed5efcae784d7bf4f2ff80

        // 1. 定义私钥和消息哈希
        String privateKeyHex = "0xac0974bec39a17e36ba4a6b4d238ff944bacb478cbed5efcae784d7bf4f2ff80";
        String messageHashHex = "0xf6896007477ab25a659f87c4f8c5e3baac32547bf305e77aa57743046e10578b";
        // 2. 从私钥创建凭证对象
        Credentials credentials = Credentials.create(privateKeyHex);

        // 3. 从凭证对象中获取公钥和地址
        // String publicKeyHex =
        // Numeric.toHexStringWithPrefixZeroPadded(credentials.getEcKeyPair().getPublicKey(),
        // 128);
        String signer = credentials.getAddress();

        // 4. createEthereumSignature
        String signature = createEthereumSignature(messageHashHex, privateKeyHex);

        // System.out.println("Signature: " + signature);

        // 5. 验证签名
        boolean result = verifySignature(messageHashHex, signature, signer);
        System.out.println("verifyResult:" + result);

    }

    public static String createEthereumSignature(String messageHashHex,
            String privateKeyHex) {

        Credentials credentials = Credentials.create(privateKeyHex);

        byte[] signatureHash = getEthereumSignatureHash(messageHashHex);

        Sign.SignatureData signatureData = Sign.signMessage(signatureHash, credentials.getEcKeyPair(), false);

        String signature = Numeric.toHexString(signatureData.getR())
                + Numeric.toHexStringNoPrefix(signatureData.getS())
                + Numeric.toHexStringNoPrefix(signatureData.getV());
        return signature;
    }

    public static boolean verifySignature(String messageHashHex, String signatureHashHex, String signer)
            throws SignatureException {
        byte[] signatureHash = getEthereumSignatureHash(messageHashHex);
        Sign.SignatureData signatureData = convertSignatureToData(signatureHashHex);
        String address = null;
        List<String> addressList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            BigInteger publicKey = Sign.recoverFromSignature((byte) i, new ECDSASignature(
                    new BigInteger(1, signatureData.getR()), new BigInteger(1, signatureData.getS())), signatureHash);
            if (publicKey != null) {
                address = "0x" + Keys.getAddress(publicKey);
                addressList.add(address);
            }
        }

        return addressList.contains(signer.toLowerCase());
    }

    public static SignatureData convertSignatureToData(String signatureString) {
        byte[] signatureBytes = Numeric.hexStringToByteArray(signatureString);
        byte[] r = Arrays.copyOfRange(signatureBytes, 0, 32);
        byte[] s = Arrays.copyOfRange(signatureBytes, 32, 64);
        byte v = signatureBytes[64];
        return new SignatureData(v, r, s);
    }

    public static byte[] getEthereumSignatureHash(String messageHashHex) {
        byte[] messageHash = Numeric.hexStringToByteArray(messageHashHex);
        byte[] prefix = "\u0019Ethereum Signed Message:\n32".getBytes();
        byte[] prefixedHash = new byte[prefix.length + messageHash.length];
        System.arraycopy(prefix, 0, prefixedHash, 0, prefix.length);
        System.arraycopy(messageHash, 0, prefixedHash, prefix.length, messageHash.length);
        byte[] signatureHash = Hash.sha3(prefixedHash);
        return signatureHash;
    }

}
