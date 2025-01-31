package com.example.flora_mart;

public class Plant {
    private final int id;
    private final String name;
    private final String category;
    private final double price;
    private final int quantity;
    private final byte[] imageBytes;

    public Plant(int id, String name, String category, double price, int quantity, byte[] imageBytes) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.imageBytes = imageBytes;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }
}
