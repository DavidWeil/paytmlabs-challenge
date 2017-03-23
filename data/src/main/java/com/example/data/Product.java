package com.example.data;

/**
 * Represents a single LCBO product offering.
 *
 * Uses a custom deserializer so that not all the fields in the JSON need to be defined here.
 */
public class Product {

    private final int id;
    private final String name;
    private final int price;
    private final int savings;
    private final String category;
    private final String origin;
    private final String packaging;
    private final int volume;
    private final int inventory;
    private final String producer;
    private final String description;
    private final String imageURL;
    private final String thumbURL;

    public Product(int id, String name, int price, int savings, String category, String origin, String packaging,
                   int volume, int inventory, String producer, String description, String imageURL, String thumbURL) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.savings = savings;
        this.category = category;
        this.origin = origin;
        this.packaging = packaging;
        this.volume = volume;
        this.inventory = inventory;
        this.producer = producer;
        this.description = description;
        this.imageURL = imageURL;
        this.thumbURL = thumbURL;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getSavings() {
        return savings;
    }

    public String getCategory() {
        return category;
    }

    public String getOrigin() {
        return origin;
    }

    public String getPackaging() {
        return packaging;
    }

    public int getVolume() {
        return volume;
    }

    public int getInventory() {
        return inventory;
    }

    public String getProducer() {
        return producer;
    }

    public String getDescription() {
        return description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getThumbURL() {
        return thumbURL;
    }
}
