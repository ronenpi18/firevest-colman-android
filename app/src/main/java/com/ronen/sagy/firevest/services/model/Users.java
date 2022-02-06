package com.ronen.sagy.firevest.services.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@Entity
public class Users implements Serializable {

    @ColumnInfo(name = "id")
    private String id;

    @SerializedName("username")
    @Expose
    @ColumnInfo(name = "user_name")
    private String username;

    @PrimaryKey
    @ColumnInfo(name = "email_id")
    @NonNull
    private String emailId;

    @ColumnInfo(name = "timestamp")
    private String timestamp;

    @Expose
    @SerializedName("fieldOfWork")
    @ColumnInfo(name = "field_of_work")
    private String fieldOfWork;

    @SerializedName("imageUrl")
    @Expose
    @ColumnInfo(name = "image_url")
    private String imageUrl;

    @ColumnInfo(name = "bio")
    private String bio;
    @ColumnInfo(name = "status")
    private String status;
    @ColumnInfo(name = "type_of_user")
    private String typeOfUser;
    @ColumnInfo(name = "investment_stage_or_capital")
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

