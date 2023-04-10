package com.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.web3j.utils.Numeric;

public class EncodeOrder {
    public static void main(String[] args) {
        // Create an array of Order objects
        Asset sellAsset1 = new Asset("0x1234", 1, AssetType.ERC20);
        Asset buyAsset1 = new Asset("0x5678", 2, AssetType.ERC721);
        OrderKey key1 = new OrderKey("0x1111", 123456, sellAsset1, buyAsset1);
        Order order1 = new Order(key1, 10L, 100L, 1618128000L, 1618214400L);

        Asset sellAsset2 = new Asset("0x4321", 3, AssetType.ERC20);
        Asset buyAsset2 = new Asset("0x8765", 4, AssetType.ERC721);
        OrderKey key2 = new OrderKey("0x2222", 654321, sellAsset2, buyAsset2);
        Order order2 = new Order(key2, 20L, 200L, 1618128000L, 1618214400L);

        Order[] orders = { order1, order2 };

        // Encode the array of Order objects to byte array
        byte[] encoded = encode(orders);
        String encodedString = Numeric.toHexString(encoded);
        System.out.println(" encoded(orders):");
        System.out.println(encodedString);

        // Decode the byte array back to array of Order objects
        Order[] decoded = decode(encoded, Order[].class);

        // Print the original and decoded Order objects
        // System.out.println("Original Orders:");
        // for (Order order : orders) {
        // System.out.println("Owner: " + order.getKey().getOwner());
        // System.out.println("Salt: " + order.getKey().getSalt());
        // System.out.println("Sell Asset Token: " +
        // order.getKey().getSellAsset().getToken());
        // System.out.println("Sell Asset Token ID: " +
        // order.getKey().getSellAsset().getTokenId());
        // System.out.println("Sell Asset Type: " +
        // order.getKey().getSellAsset().getAssetType());
        // System.out.println("Buy Asset Token: " +
        // order.getKey().getBuyAsset().getToken());
        // System.out.println("Buy Asset Token ID: " +
        // order.getKey().getBuyAsset().getTokenId());
        // System.out.println("Buy Asset Type: " +
        // order.getKey().getBuyAsset().getAssetType());
        // System.out.println("Sell Amount: " + order.getSellAmount());
        // System.out.println("Unit Price: " + order.getUnitPrice());
        // System.out.println("Start Time: " + order.getStartTime());
        // System.out.println("End Time: " + order.getEndTime());
        // }

        // System.out.println("Decoded Orders:");
        // for (Order order : decoded) {
        // System.out.println("Owner: " + order.getKey().getOwner());
        // System.out.println("Salt: " + order.getKey().getSalt());
        // System.out.println("Sell Asset Token: " +
        // order.getKey().getSellAsset().getToken());
        // System.out.println("Sell Asset Token ID: " +
        // order.getKey().getSellAsset().getTokenId());
        // System.out.println("Sell Asset Type: " +
        // order.getKey().getSellAsset().getAssetType());
        // System.out.println("Buy Asset Token: " +
        // order.getKey().getBuyAsset().getToken());
        // System.out.println("Buy Asset Token ID: " +
        // order.getKey().getBuyAsset().getTokenId());
        // System.out.println("Buy Asset Type: " +
        // order.getKey().getBuyAsset().getAssetType());
        // System.out.println("Sell Amount: " + order.getSellAmount());
        // System.out.println("Unit Price: " + order.getUnitPrice());
        // System.out.println("Start Time: " + order.getStartTime());
        // System.out.println("End Time: " + order.getEndTime());
        // }

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

    // Utility method to decode a byte array to an object
    private static <T> T decode(byte[] bytes, Class<T> clazz) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                ObjectInputStream ois = new ObjectInputStream(bis)) {
            Object obj = ois.readObject();
            return clazz.cast(obj);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to decode object", e);
        }
    }

    static class Asset implements Serializable {
        private final String token;
        private final long tokenId;
        private final AssetType assetType;

        public Asset(String token, long tokenId, AssetType assetType) {
            this.token = token;
            this.tokenId = tokenId;
            this.assetType = assetType;
        }

        public String getToken() {
            return token;
        }

        public long getTokenId() {
            return tokenId;
        }

        public AssetType getAssetType() {
            return assetType;
        }
    }

    static class OrderKey implements Serializable {
        private final String owner;
        private final long salt;
        private final Asset sellAsset;
        private final Asset buyAsset;

        public OrderKey(String owner, long salt, Asset sellAsset, Asset buyAsset) {
            this.owner = owner;
            this.salt = salt;
            this.sellAsset = sellAsset;
            this.buyAsset = buyAsset;
        }

        public String getOwner() {
            return owner;
        }

        public long getSalt() {
            return salt;
        }

        public Asset getSellAsset() {
            return sellAsset;
        }

        public Asset getBuyAsset() {
            return buyAsset;
        }
    }

    static class Order implements Serializable {
        private final OrderKey key;
        private final long sellAmount;
        private final long unitPrice;
        private final long startTime;
        private final long endTime;

        public Order(OrderKey key, long sellAmount, long unitPrice, long startTime, long endTime) {
            this.key = key;
            this.sellAmount = sellAmount;
            this.unitPrice = unitPrice;
            this.startTime = startTime;
            this.endTime = endTime;
        }

        public OrderKey getKey() {
            return key;
        }

        public long getSellAmount() {
            return sellAmount;
        }

        public long getUnitPrice() {
            return unitPrice;
        }

        public long getStartTime() {
            return startTime;
        }

        public long getEndTime() {
            return endTime;
        }
    }

    enum AssetType {
        ERC20,
        ERC721
    }
}
