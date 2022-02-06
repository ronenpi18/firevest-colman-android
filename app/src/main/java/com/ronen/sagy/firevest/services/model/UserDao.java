package com.ronen.sagy.firevest.services.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users")
    List<Users> getAll();

    @Query("SELECT * FROM users WHERE id IN (:userIds)")
    List<Users> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM users WHERE user_name LIKE :first " +
            "LIMIT 1")
    Users findByName(String first);

    @Insert
    void insertAll(Users... users);

    @Insert
    void saveUser(Users user);

    @Delete
    void delete(Users user);
}
