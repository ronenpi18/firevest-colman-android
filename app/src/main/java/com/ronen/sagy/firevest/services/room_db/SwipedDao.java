package com.ronen.sagy.firevest.services.room_db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Query;

import com.ronen.sagy.firevest.entities.SwipedList;

import java.util.List;

@Dao
public interface SwipedDao {
    @Query("SELECT * FROM swipedlist")
    List<SwipedList> getAll();

    @Query("INSERT INTO swipedlist (uidEmail,firebaseUid, username ,lastMsg)" +
            " VALUES(:uidEmail, :firebaseUid, :username, :lastMsg )")
    void insertAll(String uidEmail, String firebaseUid,String username, String lastMsg);

    @Query("UPDATE swipedlist SET lastMsg=:lastMsg WHERE uidEmail=:uidEmail")
    void updateLastMsg(String uidEmail, String lastMsg);

    @Query("SELECT * FROM swipedlist WHERE uidEmail=:uidEmail")
    SwipedList get(String uidEmail);

    @Delete
    void delete(SwipedList swipedList);
}
