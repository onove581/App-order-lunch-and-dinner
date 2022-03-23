package com.example.landdserver.Model;

public class Account {
    private String Name;
    private String Password;
    private String Phone;
    private String isStaff;
    public Account(){}
    public Account(String name, String password){
        Name=name;
        Password=password;

    }
    public String getIsStaff() {
        return isStaff;
    }

    public void setIsStaff(String isStaff) {
        this.isStaff = isStaff;
    }
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
    public String getPhoned() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
}
