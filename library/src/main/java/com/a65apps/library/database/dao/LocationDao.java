package com.a65apps.library.database.dao;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.a65apps.library.models.LocationModel;

import java.util.List;
@Dao
public interface LocationDao {
    @Query("SELECT * FROM LocationModel")
    @NonNull
    List<LocationModel> getAll();

    @Query("SELECT * FROM LocationModel WHERE person_id = :id")
    @NonNull
    LocationModel getByPersonId(@NonNull String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLocation(@NonNull LocationModel location);

    @Query("SELECT EXISTS(SELECT * FROM LocationModel WHERE person_id = :id)")
    int isExists(@NonNull String id);

    @Delete
    void delete(LocationModel locationModel);
}
