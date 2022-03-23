package com.example.landdserver.Model;

import com.example.landdserver.ViewHolders.OrderViewHolderServer;

import java.util.List;

public class Request {
    private String Name;
    private String Phone;
    private String Address;
    private String Total;
    private String Status;
    private String Comment;
    private List<Order> orders;

    public Request(){}

    public Request(String name, String phone, String address, String total, String status, String comment, List<Order> orders) {
        Name = name;
        Phone = phone;
        Address = address;
        Total = total;
        Status = status;
        Comment = comment;
        this.orders = orders;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
