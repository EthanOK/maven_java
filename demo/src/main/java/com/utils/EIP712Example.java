package com.utils;

import org.web3j.crypto.StructuredDataEncoder;
import org.web3j.utils.Numeric;
import org.web3j.crypto.StructuredData;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import static org.web3j.crypto.Hash.sha3;
// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertThrows;

public class EIP712Example {
	private static String jsonMessageString;

	public static void main(String[] args) throws IOException, RuntimeException {
		// 定义结构化数据类型
		validSetUp();
		System.out.println(jsonMessageString);
		StructuredDataEncoder dataEncoder = new StructuredDataEncoder(jsonMessageString);
		// System.out.println(dataEncoder.jsonMessageObject.getPrimaryType());
		testGetDependencies();
		// TODO:type
		testEncodeType();
		testTypeHash();

		// TODO:message
		testEncodeData();
		testHashData();

		testHashDomain();
		// 直接签名这个数据，不加ethereum 头部信息
		testHashStructuredMessage();

	}

	public static void validSetUp() throws IOException, RuntimeException {
		String validStructuredDataJSONFilePath = "demo/src/main/java/com/utils/structured_data_json_files/ValidStructuredData.json";
		jsonMessageString = getResource(validStructuredDataJSONFilePath);
	}

	private static String getResource(String jsonFile) throws IOException {
		return new String(
				Files.readAllBytes(Paths.get(jsonFile).toAbsolutePath()), StandardCharsets.UTF_8);
	}

	public static void testGetDependencies() throws IOException, RuntimeException {
		StructuredDataEncoder dataEncoder = new StructuredDataEncoder(jsonMessageString);
		Set<String> deps = dataEncoder.getDependencies(dataEncoder.jsonMessageObject.getPrimaryType());

		Set<String> depsExpected = new HashSet<>();
		depsExpected.add("Mail");
		depsExpected.add("Person");

		// assertEquals(deps, depsExpected);
		System.out.println(deps);
		// [Mail, Person]
	}

	public static void testEncodeType() throws IOException, RuntimeException {
		StructuredDataEncoder dataEncoder = new StructuredDataEncoder(jsonMessageString);
		String expectedTypeEncoding = "Mail(Person from,Person to,string contents)"
				+ "Person(string name,address wallet)";

		String encodeType = dataEncoder.encodeType(dataEncoder.jsonMessageObject.getPrimaryType());
		System.out.println(encodeType);
		// Mail(Person from,Person to,string contents)Person(string name,address wallet)
	}

	public static void testTypeHash() throws IOException, RuntimeException {
		StructuredDataEncoder dataEncoder = new StructuredDataEncoder(jsonMessageString);
		String expectedTypeHashHex = "0xa0cedeb2dc280ba39b857546d74f5549c" + "3a1d7bdc2dd96bf881f76108e23dac2";

		String TypeHashHex = Numeric.toHexString(
				dataEncoder.typeHash(dataEncoder.jsonMessageObject.getPrimaryType()));
		System.out.println(TypeHashHex);
		// 0xa0cedeb2dc280ba39b857546d74f5549c3a1d7bdc2dd96bf881f76108e23dac2
	}

	public static void testEncodeData() throws RuntimeException, IOException {
		StructuredDataEncoder dataEncoder = new StructuredDataEncoder(jsonMessageString);
		byte[] encodedData = dataEncoder.encodeData(
				dataEncoder.jsonMessageObject.getPrimaryType(),
				(HashMap<String, Object>) dataEncoder.jsonMessageObject.getMessage());
		String expectedDataEncodingHex = "0xa0cedeb2dc280ba39b857546d74f5549c3a1d7bd"
				+ "c2dd96bf881f76108e23dac2fc71e5fa27ff56c350aa531bc129ebdf613b772b6"
				+ "604664f5d8dbe21b85eb0c8cd54f074a4af31b4411ff6a60c9719dbd559c221c8"
				+ "ac3492d9d872b041d703d1b5aadf3154a261abdd9086fc627b61efca26ae57027"
				+ "01d05cd2305f7c52a2fc8";

		String encodedDataString = Numeric.toHexString(encodedData);
		System.out.println(encodedDataString);
		// 0xa0cedeb2dc280ba39b857546d74f5549c3a1d7bdc2dd96bf881f76108e23dac2fc71e5fa27ff56c350aa531bc129ebdf613b772b6604664f5d8dbe21b85eb0c8cd54f074a4af31b4411ff6a60c9719dbd559c221c8ac3492d9d872b041d703d1b5aadf3154a261abdd9086fc627b61efca26ae5702701d05cd2305f7c52a2fc8
		// System.out.println(sha3(encodedDataString));
		// 0xc52c0ee5d84264471806290a3f2c4cecfc5490626bf912d01f240d7a274b371e
	}

	public static void testHashData() throws RuntimeException, IOException {
		StructuredDataEncoder dataEncoder = new StructuredDataEncoder(jsonMessageString);
		byte[] dataHash = dataEncoder.hashMessage(
				dataEncoder.jsonMessageObject.getPrimaryType(),
				(HashMap<String, Object>) dataEncoder.jsonMessageObject.getMessage());
		String expectedMessageStructHash = "0xc52c0ee5d84264471806290a3f2c4cecf" + "c5490626bf912d01f240d7a274b371e";

		// assertEquals(Numeric.toHexString(dataHash), expectedMessageStructHash);
		String dataHashString = Numeric.toHexString(dataHash);
		System.out.println(dataHashString);
		// 0xc52c0ee5d84264471806290a3f2c4cecfc5490626bf912d01f240d7a274b371e
	}

	public static void testHashDomain() throws RuntimeException, IOException {
		StructuredDataEncoder dataEncoder = new StructuredDataEncoder(jsonMessageString);
		// sha3(encodeData("EIP712Domain", data))
		byte[] structHashDomain = dataEncoder.hashDomain();
		String expectedDomainStructHash = "0xf2cee375fa42b42143804025fc449deafd" + "50cc031ca257e0b194a650a912090f";

		// assertEquals(Numeric.toHexString(structHashDomain),
		// expectedDomainStructHash);
		String structHashString = Numeric.toHexString(structHashDomain);
		System.out.println(structHashString);
		// 0xf2cee375fa42b42143804025fc449deafd50cc031ca257e0b194a650a912090f
	}

	public static void testHashStructuredMessage() throws RuntimeException, IOException {
		StructuredDataEncoder dataEncoder = new StructuredDataEncoder(jsonMessageString);
		byte[] hashStructuredMessage = dataEncoder.hashStructuredData();
		String expectedDomainStructHash = "0xbe609aee343fb3c4b28e1df9e632fca64fcfaede20" + "f02e86244efddf30957bd2";

		// assertEquals(Numeric.toHexString(hashStructuredMessage),
		// expectedDomainStructHash);
		String hashStructuredMessageHex = Numeric.toHexString(hashStructuredMessage);
		System.out.println(hashStructuredMessageHex);
		// 0xbe609aee343fb3c4b28e1df9e632fca64fcfaede20f02e86244efddf30957bd2
	}

}
