package com.utils;

import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.StaticStruct;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;

public class EncodeStructAndDynamicArray {
        public static void main(String[] args) {
                // 定义 StaticStruct 数组
                int len = 2;

                StaticStruct[] orders = new StaticStruct[len];

                Uint256[] royaltyFees = new Uint256[len];
                DynamicStruct eip712Domain = new DynamicStruct(
                                new Utf8String("YUNGOU"), new Utf8String("1.0.0"),
                                new Uint256(1),
                                new Address("0x0fC5025C764cE34df352757e82f7B5c4Df39A836"));

                for (int i = 0; i < len; i++) {
                        orders[i] = new StaticStruct(
                                        new Address("0x6278A1E803A76796a3A1f7F6344fE874ebfe94B2"),
                                        new Uint256(new BigInteger(
                                                        "54571680155704634228761355587088252262739171014089674062080564818317375787868")),
                                        new Address("0xEAAfcC17f28Afe5CdA5b3F76770eFb7ef162D20b"),
                                        new Uint256(2),
                                        new Uint8(3),
                                        new Address("0x0000000000000000000000000000000000000000"),
                                        new Uint256(0),
                                        new Uint8(0), new Uint256(1), new Uint256(new BigInteger(
                                                        "50000000000000000")),
                                        new Uint256(1681349261), new Uint256(1683941230));

                        royaltyFees[i] = new Uint256(250);
                }

                // 将 StaticStruct[] 转换为 DynamicArray<StaticStruct> 类型
                DynamicArray<StaticStruct> ordersArray = new DynamicArray<>(StaticStruct.class, orders);

                DynamicArray<Uint256> royaltyFeesArray = new DynamicArray<>(Uint256.class, royaltyFees);

                // long timestamp = System.currentTimeMillis() / 1000;
                // Uint256 endTime = new Uint256(timestamp + 5 * 60);
                Uint256 endTime = new Uint256(0);

                // abi.encode(order)
                List<Type> list0 = Arrays.asList(orders[0]);
                String encodeData0 = FunctionEncoder.encodeConstructor(list0);
                encodeData0 = "0x" + encodeData0;
                System.out.println("encodeData: oeder");
                System.out.println(encodeData0);

                // abi.encode(orders,royaltyFees,endTime)
                List<Type> list = Arrays.asList(ordersArray, royaltyFeesArray, endTime);
                String encodeData = FunctionEncoder.encodeConstructor(list);
                encodeData = "0x" + encodeData;
                System.out.println("encodeData: orders royaltyFees endTime");
                System.out.println(encodeData);

                // abi.encode(domain,order)
                List<Type> listEIP712 = Arrays.asList(eip712Domain, orders[0]);
                String encodeDataEIP712Domain = FunctionEncoder.encodeConstructor(listEIP712);
                encodeDataEIP712Domain = "0x" + encodeDataEIP712Domain;
                System.out.println("encodeData EIP712Domain: domain order");
                System.out.println(encodeDataEIP712Domain);

                // // abi.encode(domain,orders,royaltyFees,endTime)
                List<Type> lists = Arrays.asList(eip712Domain, ordersArray, royaltyFeesArray, endTime);
                String encodeDataendTime712 = FunctionEncoder.encodeConstructor(lists);
                encodeDataendTime712 = "0x" + encodeDataendTime712;
                System.out.println("encodeDataendTime712: domian orders fees endtime");
                System.out.println(encodeDataendTime712);

        }

}
