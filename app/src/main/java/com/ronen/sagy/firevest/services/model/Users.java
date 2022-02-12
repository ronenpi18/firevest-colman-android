package com.ronen.sagy.firevest.services.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Users implements Serializable {

    private String id;

    @SerializedName("username")
    @Expose
    private String username;

    @NonNull
    private String emailId;

    private String timestamp;

    @Expose
    @SerializedName("fieldOfWork")
    private String fieldOfWork;

    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;

    private String bio;
    private String status;
    private String typeOfUser;
    private String investmentStageOrCapital;


    public Users() {

    }

    public Users(String id, String username, String emailId, String timestamp, String imageUrl,
                 String bio, String status, String typeOfUser, String investmentStageOrCapital,
                 String fieldOfWork) {
        this.id = id;
        this.username = username;
        this.emailId = emailId;
        this.timestamp = timestamp;
        this.imageUrl = imageUrl;
        this.bio = bio;
        this.status = status;
        this.typeOfUser = typeOfUser;
        this.investmentStageOrCapital = investmentStageOrCapital;
        this.fieldOfWork = fieldOfWork;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setInvestmentStageOrCapital(String investmentStageOrCapital) {
        this.investmentStageOrCapital = investmentStageOrCapital;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTypeOfUser() {
        return typeOfUser;
    }

    public void setTypeOfUser(String typeOfUser) {
        this.typeOfUser = typeOfUser;
    }

    public String getInvestmentStageOrCapital() {
        return investmentStageOrCapital;
    }

    public String getFieldOfWork() {
        return fieldOfWork;
    }

    public void setFieldOfWork(String fieldOfWork) {
        this.fieldOfWork = fieldOfWork;
    }
}

