package com.example.landdserver.Model;

public class Mon {
    private String Name;
    private String Image;
    private String Description;
    private String Price;
    private String Discount;
    private String MenuId;

    public Mon(){}

    public Mon(String name, String description, String price, String discount, String menuId, String image) {
        this.Name = name;
        this.Image = image;
        this.Description = description;
        this.Price = price;
        this.Discount = discount;
        this.MenuId = menuId;
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
