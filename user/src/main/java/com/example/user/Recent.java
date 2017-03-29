package com.example.user;

/**
 * Information for a recently-viewed product.
 */
public class Recent {

    private final String id;
    private final String name;
    private final String thumbnail;

    public Recent(String id, String name, String thumbnail) {
        this.id = id;
        this.name = name;
        this.thumbnail = thumbnail;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
