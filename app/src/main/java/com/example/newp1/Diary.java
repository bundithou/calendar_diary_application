package com.example.newp1;


public class Diary {
    private String user;
    private String date;
    private String title;
    private String info;
    private String imageResource;

    public Diary(String user, String date, String title, String info, String imageResource) {
        this.user = user;
        this.date = date;
        this.title = title;
        this.info = info;
        this.imageResource = imageResource;
    }

    public String getUser() {
        return user;
    }

    public String getDate() {
        return date;
    }

    String getTitle() {
        return title;
    }

    String getInfo() {
        return info;
    }

    public String getImageResource() {
        return imageResource;
    }
}
