package edu.neu.csye6200.model;

import org.bson.Document;

public class Employee extends User {
    private String id;
    private int age;
    private String role;
    private String inTime;
    private String outTime;

    public Employee(String username, String encryptedPassword, String userType) {
        super(username, encryptedPassword, userType);
    }

    public Employee(String username, String encryptedPassword, String userType, String id, int age, String role, String inTime, String outTime) {
        super(username, encryptedPassword, userType);
        this.id = id;
        this.age = age;
        this.role = role;
        this.inTime = inTime;
        this.outTime = outTime;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    @Override
    public Document toDocument() {
        return new Document("employeeId", this.id)
                .append("age", this.age)
                .append("role", this.role)
                .append("intime", this.inTime)
                .append("outtime", this.outTime);
    }
}

