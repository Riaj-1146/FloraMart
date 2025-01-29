package com.example.flora_mart;

public class Plant {
    private final String name;
    private final byte[] imageBytes;

    public Plant(String name, byte[] imageBytes) {
        this.name = name;
        this.imageBytes = imageBytes;
    }

    public String getName() {
        return name;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }
}
