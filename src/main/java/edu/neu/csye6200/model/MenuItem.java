package edu.neu.csye6200.model;

import org.bson.Document;

public class MenuItem {
    private String name;
    private double price;

    private String type;

    public MenuItem(String name, double price, String type) {
        this.name = name;
        this.price = price;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Document toDocument() {
        return new Document("name", this.name)
                .append("price", this.price)
                .append("type", this.type);
    }
}
