package com.a65apps.yuhnin.lesson1.pojo;

import android.net.Uri;
import androidx.annotation.NonNull;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PersonModelAdvanced {
    private int id;
    private final String displayName;
    private final String description;
    private final Date dateBirthday;
    private Uri photoUri;

    public Uri getImageUri() {
        return this.photoUri;
    }



    public PersonModelAdvanced(int id, @NonNull String displayName, @NonNull String description,
                               Uri photoUri, String dateBirthday) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
        this.photoUri = photoUri;
        Date tempDateBirthDay = null;
        try {
            tempDateBirthDay = new SimpleDateFormat("dd-MM-yyyy").parse(dateBirthday);
        } catch (Exception e) {
            tempDateBirthDay = null;
        }
        this.dateBirthday = tempDateBirthDay;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return this.displayName;
    }


    public String getDescription() {
        return this.description;
    }

    public Date getDateBirthday() {
        return this.dateBirthday;
    }

    public String getStringBirthday() {
        if (dateBirthday != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            return simpleDateFormat.format(dateBirthday);
        } else {
            return "";
        }
    }

    public long getValueBirthday() {
        return dateBirthday == null ? -1 : dateBirthday.getTime();
    }
}
