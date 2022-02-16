package com.ronen.sagy.firevest.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class SwipedList {
    @PrimaryKey
    @NonNull
    public String uidEmail;

    @ColumnInfo(name = "username")
    public String username;

    @ColumnInfo(name = "firebaseUid")
    public String firebaseUid;

    @ColumnInfo(name = "lastMsg")
    public String lastMsg;

}
