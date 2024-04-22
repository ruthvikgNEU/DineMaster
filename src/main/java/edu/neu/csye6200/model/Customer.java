package edu.neu.csye6200.model;

public class Customer extends User {
    private boolean membership = false;

    public Customer(String username, String encryptedPassword, String userType) {
        super(username, encryptedPassword, userType);
    }

    public Customer(String username, String encryptedPassword, String userType, Boolean membership) {
        super(username, encryptedPassword, userType);
        this.membership = membership;
    }
}