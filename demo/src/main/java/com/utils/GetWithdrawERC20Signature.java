package com.utils;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign;
import org.web3j.abi.datatypes.Address;
import org.web3j.utils.Numeric;
import java.math.BigInteger;
import java.util.Arrays;
import org.web3j.abi.datatypes.Type;
import java.util.List;

public class GetWithdrawERC20Signature {
    public static void main(String[] args) throws Exception {

        // Private key: 503f38a9c967ed597e47fe25643985f032b072db8075426a92110f82df48dfcb
        // Address: 0x5B38Da6a701c568545dCfcB03FcB875f56beddC4

        String privateKeyHex = "0x503f38a9c967ed597e47fe25643985f032b072db8075426a92110f82df48dfcb";

        BigInteger orderId = BigInteger.valueOf(1);
        String account = "0x5B38Da6a701c568545dCfcB03FcB875f56beddC4";
        BigInteger amount = BigInteger.valueOf(1);

        String encodedDataHex = encodeData(orderId, account, amount);
        System.out.println("Encoded data: " + encodedDataHex);

        String hashDataHex = hashEncodeData(encodedDataHex);

        System.out.println("Hash data: " + hashDataHex);

        String signatureHex = getEthereumSignature(hashDataHex, privateKeyHex);

        System.out.println("Signature: " + signatureHex);

    }

    private static String encodeData(BigInteger orderId, String account, BigInteger amount) {
        // encodeData
        List<Type> list = Arrays.asList(new Uint256(orderId), new Address(account),
                new Uint256(amount));
        String encodeData = FunctionEncoder.encodeConstructor(list);
        encodeData = "0x" + encodeData;
        return encodeData;
    }

    private static String hashEncodeData(String encodedDataHex) {
        String hashDataHex = Hash.sha3(encodedDataHex);
        return hashDataHex;
    }

    private static String getEthereumSignature(String messageHashHex,
            String privateKeyHex) {
        byte[] messageHash = Numeric.hexStringToByteArray(messageHashHex);
        Credentials credentials = Credentials.create(privateKeyHex);
        Sign.SignatureData signatureData = Sign.signPrefixedMessage(messageHash, credentials.getEcKeyPair());
        String signature = Numeric.toHexString(signatureData.getR())
                + Numeric.toHexStringNoPrefix(signatureData.getS())
                + Numeric.toHexStringNoPrefix(signatureData.getV());
        return signature;
    }
}
