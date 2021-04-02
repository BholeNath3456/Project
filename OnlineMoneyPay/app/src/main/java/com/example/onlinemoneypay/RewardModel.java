package com.example.onlinemoneypay;

import com.google.firebase.Timestamp;

import java.util.Date;

public class RewardModel {
private String type;
private String lowerLimit;
private String upperLimit;
private String discOramt;
private String coupenBody;
private Timestamp timestamp;
private  String rewardID;

    public RewardModel(String type, String lowerLimit, String upperLimit, String discOramt, String coupenBody, Timestamp timestamp, String rewardID) {
        this.type = type;
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
        this.discOramt = discOramt;
        this.coupenBody = coupenBody;
        this.timestamp = timestamp;
        this.rewardID = rewardID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(String lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    public String getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(String upperLimit) {
        this.upperLimit = upperLimit;
    }

    public String getDiscOramt() {
        return discOramt;
    }

    public void setDiscOramt(String discOramt) {
        this.discOramt = discOramt;
    }

    public String getCoupenBody() {
        return coupenBody;
    }

    public void setCoupenBody(String coupenBody) {
        this.coupenBody = coupenBody;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getRewardID() {
        return rewardID;
    }

    public void setRewardID(String rewardID) {
        this.rewardID = rewardID;
    }
}
