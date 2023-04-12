package com.utils;

import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.StaticStruct;
import org.web3j.abi.datatypes.Type;
import java.util.Arrays;
import java.util.List;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.generated.Uint256;

public class EncodeStaticStructDynamicArray {
    public static void main(String[] args) {
        // 定义 StaticStruct 数组
        int len = 3;
        StaticStruct[] orders = new StaticStruct[len];
        Uint256[] royaltyFees = new Uint256[len];

        for (int i = 0; i < len; i++) {
            orders[i] = new StaticStruct(
                    new Address("0x0987654321098765432109876543210987654325"),
                    new Uint256(i + 10));
            royaltyFees[i] = new Uint256(100 + i);
        }

        // 将 StaticStruct[] 转换为 DynamicArray<StaticStruct> 类型
        DynamicArray<StaticStruct> ordersArray = new DynamicArray<>(StaticStruct.class, orders);
        DynamicArray<Uint256> royaltyFeesArray = new DynamicArray<>(Uint256.class, royaltyFees);

        long timestamp = System.currentTimeMillis() / 1000;
        Uint256 endTime = new Uint256(timestamp + 5 * 60);

        List<Type> list = Arrays.asList(ordersArray, royaltyFeesArray, endTime);
        String encodeData = FunctionEncoder.encodeConstructor(list);
        encodeData = "0x" + encodeData;
        System.out.println("encodeData: ");
        System.out.println(encodeData);
    }

}
