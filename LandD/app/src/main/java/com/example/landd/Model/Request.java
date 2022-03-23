package com.example.landd.Model;

import java.util.List;

public class Request {
    private String Name;
    private String Phone;
    private String Address;
    private String Total;
    private String Status;
    private String Comment;
    private List<Order> orders;
//    private String Comment;,String comment

    public Request(){}

    public Request(String name, String phone, String address, String total,String status, String comment,List<Order> orders) {
        Name = name;
        Phone = phone;
        Address = address;
        Total = total;
        Status = status;   //0 is default, 1 is shipping, 2 is shipped
        Comment=comment;
        this.orders = orders;
//        Comment=comment;
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

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
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
}
