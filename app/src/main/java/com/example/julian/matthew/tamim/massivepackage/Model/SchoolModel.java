package com.example.julian.matthew.tamim.massivepackage.Model;

/**
 * Created by Julian on 25/11/2015.
 */
public class SchoolModel {
    private int id;
    private char schoolType;
    private String website;
    private String schoolName;
    private String location;
    private String ward;

    public int getId() {
        return id;
    }

    public char getSchoolType() {
        return schoolType;
    }

    public void setSchoolType(char schoolType) {
        this.schoolType = schoolType;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }
}
