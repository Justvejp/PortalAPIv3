package com.example.PortalAPIv3.Logic;

public class HeaterTime {

    int year;
    int month;
    int weekOfYear;
    String dayString;
    int day;
    int hour;
    int minute;

    public HeaterTime(int year, int month, int weekOfYear, String dayString, int hour, int minute) {
        this.year = year;
        this.month = month;
        this.weekOfYear = weekOfYear;
        this.dayString = dayString;
        this.hour = hour;
        this.minute = minute;
        setDay();
    }

    public void setDay() {
        switch (dayString) {
            case "SUNDAY" -> this.day = 1;
            case "MONDAY" -> this.day = 2;
            case "TUESDAY" -> this.day = 3;
            case "WEDNESDAY" -> this.day = 4;
            case "THURSDAY" -> this.day = 5;
            case "FRIDAY" -> this.day = 6;
            case "SATURDAY" -> this.day = 7;
        }
    }

    public int getYear() {
        return this.year;
    }

    public int getMonth() {
        return this.month;
    }

    public int getDay() {
        return this.day;
    }

    public int getHour() {
        return this.hour;
    }

    public int getMinute() {
        return this.minute;
    }

    public int getWeekOfYear() {
        return this.weekOfYear;
    }

    public String toString() {
        return "" + this.year + this.month + this.day + this.hour + this.minute;
    }
}
