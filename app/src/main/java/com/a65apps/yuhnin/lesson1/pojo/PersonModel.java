package com.a65apps.yuhnin.lesson1.pojo;

import androidx.annotation.NonNull;

public class PersonModel {
    private long id;
    private final String firstName;
    private final String secondName;
    private final String thirdName;
    private final String description;

    public int getImageResource() {
        return imageResource;
    }

    private int imageResource;

    public PersonModel(long id, @NonNull String firstName, @NonNull String secondName,
                       @NonNull String thirdName, @NonNull String description, int imageResource) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.thirdName = thirdName;
        this.description = description;
        this.imageResource = imageResource;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFullName() {
        String fullname = secondName;
        if (!firstName.isEmpty()) fullname += " " + firstName;
        if (!thirdName.isEmpty()) fullname += " " + thirdName;
        return fullname;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getThirdName() {
        return thirdName;
    }

    public String getDescription() {
        return description;
    }
}
