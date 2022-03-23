package com.example.landdserver.Model;

public class Rating {
    private String userPhone;
    private String rating;
    private String comment;
    private String MonId;
    private String MonName;

    public Rating(){}

    public Rating(String userPhone, String monId, String rating, String comment , String MonName) {
        this.userPhone = userPhone;
        this.rating = rating;
        this.comment = comment;
        this.MonId = monId;
        this.MonName=MonName;
    }


    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getMonId() {
        return MonId;
    }
    public void setMonId(String monId) {
        this.MonId=monId;
    }
    public String getMonName() {
        return MonName;
    }
    public void setMonName(String monName) {
        this.MonName=monName;
    }
}
