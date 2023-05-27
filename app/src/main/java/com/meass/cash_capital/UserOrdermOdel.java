package com.meass.cash_capital;

public class UserOrdermOdel {
    String deliveryaddress,deliverymobile_no,placed_user_name,order_date_time,delivery_date,email,uuid,cartvalue;

    public UserOrdermOdel() {
    }

    public String getDeliveryaddress() {
        return deliveryaddress;
    }

    public void setDeliveryaddress(String deliveryaddress) {
        this.deliveryaddress = deliveryaddress;
    }

    public String getDeliverymobile_no() {
        return deliverymobile_no;
    }

    public void setDeliverymobile_no(String deliverymobile_no) {
        this.deliverymobile_no = deliverymobile_no;
    }

    public String getPlaced_user_name() {
        return placed_user_name;
    }

    public void setPlaced_user_name(String placed_user_name) {
        this.placed_user_name = placed_user_name;
    }

    public String getOrder_date_time() {
        return order_date_time;
    }

    public void setOrder_date_time(String order_date_time) {
        this.order_date_time = order_date_time;
    }

    public String getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCartvalue() {
        return cartvalue;
    }

    public void setCartvalue(String cartvalue) {
        this.cartvalue = cartvalue;
    }

    public UserOrdermOdel(String deliveryaddress, String deliverymobile_no, String placed_user_name,
                          String order_date_time, String delivery_date, String email, String uuid, String cartvalue) {
        this.deliveryaddress = deliveryaddress;
        this.deliverymobile_no = deliverymobile_no;
        this.placed_user_name = placed_user_name;
        this.order_date_time = order_date_time;
        this.delivery_date = delivery_date;
        this.email = email;
        this.uuid = uuid;
        this.cartvalue = cartvalue;
    }
}
