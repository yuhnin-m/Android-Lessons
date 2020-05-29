package com.a65apps.yuhnin.lesson1.pojo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PersonModel {
    private int id;
    private final String firstName;
    private final String secondName;
    private final String thirdName;
    private final String description;
    private final Date dateBirthday;

    public int getImageResource() {
        return imageResource;
    }

    private int imageResource;

    public PersonModel(int id, @NonNull String firstName, @NonNull String secondName,
                       @NonNull String thirdName, @NonNull String description, int imageResource, String dateBirthday) throws ParseException {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.thirdName = thirdName;
        this.description = description;
        this.imageResource = imageResource;
        this.dateBirthday = new SimpleDateFormat("dd-MM-yyyy").parse(dateBirthday);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        String fullname = secondName;
        if (!firstName.isEmpty()) fullname += " " + firstName;
        if (!thirdName.isEmpty()) fullname += " " + thirdName;
        return fullname;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getSecondName() {
        return this.secondName;
    }

    public String getThirdName() {
        return this.thirdName;
    }

    public String getDescription() {
        return this.description;
    }

    public Date getDateBirthday() {
        return this.dateBirthday;
    }

    public String getStringBirthday() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return simpleDateFormat.format(dateBirthday);
    }

    public long getValueBirthday() {
        return dateBirthday.getTime();
    }
}
