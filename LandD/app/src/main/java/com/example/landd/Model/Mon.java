package com.example.landd.Model;

public class Mon {
    private String Name;
    private String Image;
    private String Description;
    private String Price;
    private String Discount;
    private String MenuId;

    public Mon(){}

    public Mon(String Name, String Description, String Price, String Discount, String MenuId, String Image) {
        this.Name = Name;
        this.Image = Image;
        this.Description = Description;
        this.Price = Price;
        this.Discount = Discount;
        this.MenuId = MenuId;
    }

    // Getter Methods

    public String getName() {
        return Name;
    }

    public String getImage() {
        return Image;
    }

    public String getDescription() {
        return Description;
    }

    public String getPrice() {
        return Price;
    }

    public String getDiscount() {
        return Discount;
    }

    public String getMenuId() {
        return MenuId;
    }

    // Setter Methods

    public void setName(String Name) {
        this.Name = Name;
    }

    public void setImage(String Image) {
        this.Image = Image;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public void setPrice(String Price) {
        this.Price = Price;
    }

    public void setDiscount(String Discount) {
        this.Discount = Discount;
    }

    public void setMenuId(String MenuId) {
        this.MenuId = MenuId;
    }
}
