package com.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SignatureException;
import java.util.HashMap;

import org.web3j.crypto.StructuredDataEncoder;
import org.web3j.utils.Numeric;

public class CalcOpenseaOrderHashVerrify {
    private static String jsonMessageString;

    public static void main(String[] args) throws SignatureException, Exception {
        String JSONFilePath = "demo/src/main/java/com/utils/structured_data_json_files/OpenSeaOrder.json";
        jsonMessageString = new String(
                Files.readAllBytes(Paths.get(JSONFilePath).toAbsolutePath()), StandardCharsets.UTF_8);
        System.out.println(jsonMessageString);
        StructuredDataEncoder dataEncoder = new StructuredDataEncoder(jsonMessageString);
        //
        byte[] structHashDomain = dataEncoder.hashDomain();

        String structHashDomainString = Numeric.toHexString(structHashDomain);
        System.out.println("keccak256(abi.encode(typeHash, nameHash, versionHash, block.chainid, address(this))):");
        System.out.println(structHashDomainString);

        // TODO: orderHash
        byte[] dataHash = dataEncoder.hashMessage(
                dataEncoder.jsonMessageObject.getPrimaryType(),
                (HashMap<String, Object>) dataEncoder.jsonMessageObject.getMessage());
        String dataHashString = Numeric.toHexString(dataHash);
        System.out.println("keccak256(abi.encode(BASICORDER_TYPE_HASH, parameters)):");
        System.out.println("orderHash: " + dataHashString);

        byte[] hashStructuredMessage = dataEncoder.hashStructuredData();
        String hashStructuredMessageHex = Numeric.toHexString(hashStructuredMessage);
        System.out.println("_hashTypedDataV4(keccak256(abi.encode(BASICORDER_TYPE_HASH, parameters))):");
        System.out.println(hashStructuredMessageHex);

        String signature = "0x73d60b5ff016ceda71bf58c680796a5d680188c197569b5d26f4c7eefdbaacbd4024a918f9413cae25f35f782f09f453b11dd1937b5d676da025c63ac2b6cb0f1c";
        String address = VerifyEIP712Signature.verifyReturnAddress(hashStructuredMessageHex, signature);
        System.out.println(address);
    }
}
