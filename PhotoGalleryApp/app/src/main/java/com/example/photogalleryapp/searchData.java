package com.example.photogalleryapp;

public class searchData {

    private String captionSearch = "";
    private String timeSearch = "";

    // Getter
    public String getCaption() {
        return captionSearch;
    }

    // Setter
    public void setCaption(String newCaption) {
        this.captionSearch = newCaption;
    }

    // Getter
    public String getTime() {
        return timeSearch;
    }

    // Setter
    public void setTime(String newTime) {
        this.timeSearch = newTime;
    }
}
