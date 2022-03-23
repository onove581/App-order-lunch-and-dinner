package com.example.landdserver.Model;

public class Order {

    private int ID;
    private String MonId;
    private String MonName;
    private String Price;
    private String Quantity;
    private String Discount;
    private String Image;

    public Order(){}

    public Order(String monId, String monName, String quantity, String price, String discount,String image) {

        MonId=monId;
        MonName = monName;
        Price = price;
        Quantity = quantity;
        Discount = discount;
        Image=image;
    }

    public Order(int ID, String monId, String monName, String price, String quantity, String discount,String image) {
        this.ID = ID;
        MonId= monId;
        MonName= monName;
        Price = price;
        Quantity = quantity;
        Discount = discount;
        Image=image;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getMonId() {
        return MonId;
    }

    public void setMonId(String MontId) {
        MonId = MonId;
    }

    public String getMonName() {
        return MonName;
    }

    public void setMonName(String MontName) {
        MonName = MonName;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getImage() {
        return Image;
    }
    public void setImage(String image)
    {  Image=image;
    }
}