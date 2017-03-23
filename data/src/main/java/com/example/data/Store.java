package com.example.data;

/**
 * Contains the details of a single LCBO store record.
 */
public class Store {

    private final int id;
    private final String name;
    private final String address1;
    private final String address2;
    private final String city;
    private final String postal;
    private final String phone;
    private final boolean isAccessible;
    private final boolean hasVintages;

    public Store(int id, String name, String address1, String address2, String city, String postal, String phone,
                 boolean isAccessible, boolean hasVintages) {

        this.id = id;
        this.name = name;
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.postal = postal;
        this.phone = phone;
        this.isAccessible = isAccessible;
        this.hasVintages = hasVintages;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public String getCity() {
        return city;
    }

    public String getPostal() {
        return postal;
    }

    public String getPhone() {
        return phone;
    }

    public boolean isAccessible() {
        return isAccessible;
    }

    public boolean isHasVintages() {
        return hasVintages;
    }
}
