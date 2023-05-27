package com.meass.cash_capital;

public class BloodBankModel {
    String name,time,feeentry,feeprice,roomid,timeuuid,uuid,day,month,year,hours,min;

    public BloodBankModel() {
    }

    public BloodBankModel(String name, String time, String feeentry, String feeprice, String roomid, String timeuuid,
                          String uuid, String day, String month, String year, String hours, String min) {
        this.name = name;
        this.time = time;
        this.feeentry = feeentry;
        this.feeprice = feeprice;
        this.roomid = roomid;
        this.timeuuid = timeuuid;
        this.uuid = uuid;
        this.day = day;
        this.month = month;
        this.year = year;
        this.hours = hours;
        this.min = min;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFeeentry() {
        return feeentry;
    }

    public void setFeeentry(String feeentry) {
        this.feeentry = feeentry;
    }

    public String getFeeprice() {
        return feeprice;
    }

    public void setFeeprice(String feeprice) {
        this.feeprice = feeprice;
    }

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }

    public String getTimeuuid() {
        return timeuuid;
    }

    public void setTimeuuid(String timeuuid) {
        this.timeuuid = timeuuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }
}
