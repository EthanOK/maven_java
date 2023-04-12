package com.utils;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.utils.Numeric;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class Example {

        public static class Order {
                public Address owner;
                public Uint256 sellAmount;
                public Uint256 unitPrice;
                public Uint256 startTime;
                public Uint256 endTime;

                public Order(Address owner, Uint256 sellAmount, Uint256 unitPrice, Uint256 startTime, Uint256 endTime) {
                        this.owner = owner;
                        this.sellAmount = sellAmount;
                        this.unitPrice = unitPrice;
                        this.startTime = startTime;
                        this.endTime = endTime;
                }
        }

        public static void main(String[] args) {
                Address[] ownersArray = new Address[] {
                                new Address("0x123"),
                                new Address("0x456")
                };

                Uint256[] sellAmountsArray = new Uint256[] {
                                new Uint256(BigInteger.valueOf(100)),
                                new Uint256(BigInteger.valueOf(300))
                };

                Uint256[] unitPricesArray = new Uint256[] {
                                new Uint256(BigInteger.valueOf(200)),
                                new Uint256(BigInteger.valueOf(400))
                };

                Uint256[] startTimesArray = new Uint256[] {
                                new Uint256(BigInteger.valueOf(1649683200)),
                                new Uint256(BigInteger.valueOf(1649773200))
                };

                Uint256[] endTimeArray = new Uint256[] {
                                new Uint256(BigInteger.valueOf(1649769600))
                };

                Uint256 endTime = new Uint256(BigInteger.valueOf(1649769600));

                Uint256[] royaltyFeesArray = new Uint256[] {
                                new Uint256(BigInteger.valueOf(100)),
                                new Uint256(BigInteger.valueOf(200))
                };

                Order[] ordersArray = new Order[] {
                                new Order(new Address("0x123"), new Uint256(BigInteger.valueOf(100)),
                                                new Uint256(BigInteger.valueOf(200)),
                                                new Uint256(BigInteger.valueOf(1649683200)),
                                                new Uint256(BigInteger.valueOf(1649769600))),
                                new Order(new Address("0x456"), new Uint256(BigInteger.valueOf(300)),
                                                new Uint256(BigInteger.valueOf(400)),
                                                new Uint256(BigInteger.valueOf(1649773200)),
                                                new Uint256(BigInteger.valueOf(1649859600)))
                };

                List<Type> functionParams = Arrays.asList(

                                new DynamicArray<>(Address.class, Arrays.asList(ownersArray)),
                                new DynamicArray<>(Uint256.class, Arrays.asList(sellAmountsArray)),
                                new DynamicArray<>(Uint256.class, Arrays.asList(unitPricesArray)),
                                new DynamicArray<>(Uint256.class, Arrays.asList(startTimesArray)),
                                new DynamicArray<>(Uint256.class, Arrays.asList(royaltyFeesArray)),
                                endTime);

                Function function = new Function(
                                "functionName",
                                functionParams,
                                Arrays.asList());

                String encodedFunction = FunctionEncoder.encode(function);
                System.out.println(encodedFunction);

                Order[] orders = ordersArray;

                // Encode the array of Order objects to byte array
                byte[] encoded = encode(orders);
                String encodedString = Numeric.toHexString(encoded);
                System.out.println(" encoded(orders):");
                System.out.println(encodedString);
        }

        private static byte[] encode(Serializable obj) {
                try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                ObjectOutputStream oos = new ObjectOutputStream(bos)) {
                        oos.writeObject(obj);
                        oos.flush();
                        return bos.toByteArray();
                } catch (IOException e) {
                        throw new RuntimeException("Failed to encode object", e);
                }
        }
}
