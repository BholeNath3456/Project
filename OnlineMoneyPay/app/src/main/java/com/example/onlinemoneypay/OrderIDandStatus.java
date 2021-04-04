package com.example.onlinemoneypay;

public class OrderIDandStatus {
    private String orderID;
    private String orderStatus;

    public OrderIDandStatus(String orderID, String orderStatus) {
        this.orderID = orderID;
        this.orderStatus = orderStatus;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
