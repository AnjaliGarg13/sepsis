package com.nsut.spotsepsis.models;

import android.content.Intent;

public class Recommendation {
    private Integer imageURL;
//    private String imageURL;
    private String title;
    private String shortDescription;
    private String tag;
    private String redirectTo;

    public Recommendation(Integer imageURL, String title, String shortDescription, String tag, String redirectTo) {
        this.imageURL = imageURL;
        this.title = title;
        this.shortDescription = shortDescription;
        this.tag = tag;
        this.redirectTo = redirectTo;
    }

    public Integer getImageURL() {
        return imageURL;
    }

    public void setImageURL(Integer imageURL) {
        this.imageURL = imageURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getRedirectTo() {
        return redirectTo;
    }

    public void setRedirectTo(String redirectTo) {
        this.redirectTo = redirectTo;
    }

    @Override
    public String toString() {
        return "Recommendation{" +
                "imageURL='" + imageURL + '\'' +
                ", title='" + title + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                ", tag='" + tag + '\'' +
                ", redirectTo='" + redirectTo + '\'' +
                '}';
    }
}
