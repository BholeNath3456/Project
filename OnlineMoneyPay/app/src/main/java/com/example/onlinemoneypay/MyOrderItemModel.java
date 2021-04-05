package com.example.onlinemoneypay;

public class MyOrderItemModel {
    private String productImage;
//    private int rating;
    private String productTitle;
    private String deliveryStatus;
    private String orderID;

    public MyOrderItemModel(String productImage, String productTitle, String deliveryStatus, String orderID) {
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.deliveryStatus = deliveryStatus;
        this.orderID = orderID;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }
}
