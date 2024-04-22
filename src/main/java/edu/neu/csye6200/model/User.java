package edu.neu.csye6200.model;

import org.bson.Document;

public class User {
    private String username;
    private String encryptedPassword;
    private String userType;

    // public User() {
    // }

    public User(String username, String encryptedPassword, String userType) {
        this.username = username;
        this.encryptedPassword = encryptedPassword;
        this.userType = userType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Document toDocument() {
        return new Document("username", this.username)
                .append("encryptedPassword", this.encryptedPassword)
                .append("userType", this.userType);
    }
}

// public class Customer extends User {
//     // Customer-specific fields and methods
// }

// public class Staff extends User {
//     private String role;

//     public Staff(String username, String encryptedPassword, String userType, String role) {
//         super(username, encryptedPassword, userType);
//         this.role = role;
//     }

//     // getters and setters for role
// }