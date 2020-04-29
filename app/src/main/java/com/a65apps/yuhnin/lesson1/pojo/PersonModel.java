package com.a65apps.yuhnin.lesson1.pojo;

public class PersonModel {
    private long id;
    private String firstName;
    private String secondName;
    private String thirdName;
    private String description;

    public int getImageResource() {
        return imageResource;
    }

    private int imageResource;

    public PersonModel(long id, String firstName, String secondName,
                       String thirdName, String description, int imageResource) {
        this.id = id;
        this.firstName = firstName == null ? "" : firstName;
        this.secondName = secondName == null ? "" : secondName;
        this.thirdName = thirdName == null ? "" : thirdName;
        this.description = description == null ? "" : description;
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

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getThirdName() {
        return thirdName;
    }

    public void setThirdName(String thirdName) {
        this.thirdName = thirdName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
