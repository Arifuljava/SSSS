package com.meass.cash_capital;

public class SendingModel {
    String name,time,dateandtime,taxid,convertname,ammount,fee,total,toid;

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

    public String getDateandtime() {
        return dateandtime;
    }

    public void setDateandtime(String dateandtime) {
        this.dateandtime = dateandtime;
    }

    public String getTaxid() {
        return taxid;
    }

    public void setTaxid(String taxid) {
        this.taxid = taxid;
    }

    public String getConvertname() {
        return convertname;
    }

    public void setConvertname(String convertname) {
        this.convertname = convertname;
    }

    public String getAmmount() {
        return ammount;
    }

    public void setAmmount(String ammount) {
        this.ammount = ammount;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getToid() {
        return toid;
    }

    public void setToid(String toid) {
        this.toid = toid;
    }

    public SendingModel(String name, String time, String dateandtime, String taxid,
                        String convertname, String ammount, String fee, String total, String toid) {
        this.name = name;
        this.time = time;
        this.dateandtime = dateandtime;
        this.taxid = taxid;
        this.convertname = convertname;
        this.ammount = ammount;
        this.fee = fee;
        this.total = total;
        this.toid = toid;
    }

    public SendingModel() {
    }
}
