package com.example.landdserver.Model;

public class Menu {
    private String Name;
    private String Image;

    public Menu(){}

    public Menu(String name, String image) {
        Name = name;
        Image = image;
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
