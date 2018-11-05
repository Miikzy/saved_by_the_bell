package com.example.zaki_berouk.savedbythebell.model;

public class Event {

    private String name;
    private String date;
    private String location;
    private String descr;
    private String departureTime;

    public Event(String name, String date, String location, String descr) {
        this.name = name;
        this.date = date;
        this.location = location;
        this.descr = descr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }
}
