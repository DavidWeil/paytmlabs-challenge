package com.example.user;

/**
 * The data that is persisted for a user.
 */
public class User {

    private final String name;
    private String passwordHash;
    private String selectedStore;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getSelectedStore() {
        return selectedStore;
    }

    public void setSelectedStore(String selectedStore) {
        this.selectedStore = selectedStore;
    }
}
