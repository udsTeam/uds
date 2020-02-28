package com.maha.uds.Model;

import java.util.List;

public class OrderModel {

    private String motherID;
    private String babysitterID;
    private String babyID;
    private String paymentID;
    private String chatID;
    private String dailyReportID;
    private String orderStatus;
    private int totalHours;
    private double price;
    private List<ScheduleModel> scheduleList;

    public OrderModel() {
    }


    public OrderModel(String motherID, String babysitterID, String babyID, String paymentID, String chatID, String dailyReportID, String orderStatus, int totalHours, double price, List<ScheduleModel> scheduleList) {
        this.motherID = motherID;
        this.babysitterID = babysitterID;
        this.babyID = babyID;
        this.paymentID = paymentID;
        this.chatID = chatID;
        this.dailyReportID = dailyReportID;
        this.orderStatus = orderStatus;
        this.totalHours = totalHours;
        this.price = price;
        this.scheduleList = scheduleList;
    }

    public String getMotherID() {
        return motherID;
    }

    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    public String getBabysitterID() {
        return babysitterID;
    }

    public void setBabysitterID(String babysitterID) {
        this.babysitterID = babysitterID;
    }

    public String getBabyID() {
        return babyID;
    }

    public void setBabyID(String babyID) {
        this.babyID = babyID;
    }

    public String getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    public String getChatID() {
        return chatID;
    }

    public void setChatID(String chatID) {
        this.chatID = chatID;
    }

    public String getDailyReportID() {
        return dailyReportID;
    }

    public void setDailyReportID(String dailyReportID) {
        this.dailyReportID = dailyReportID;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(int totalHours) {
        this.totalHours = totalHours;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<ScheduleModel> getScheduleList() {
        return scheduleList;
    }

    public void setScheduleList(List<ScheduleModel> scheduleList) {
        this.scheduleList = scheduleList;
    }
}
