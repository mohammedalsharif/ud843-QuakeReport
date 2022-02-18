package com.examples.quakereport;

public class Earthquake {
    private double mag;
    private String place;
    private Long date;
    private String url;

    public Earthquake(double mag, String place, Long date,String url) {
        this.mag = mag;
        this.place = place;
        this.date = date;
        this.url=url;
    }

    public double getMag() {
        return mag;
    }

    public void setMag(double mag) {
        this.mag = mag;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
