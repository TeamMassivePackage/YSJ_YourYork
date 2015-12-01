package com.example.julian.matthew.tamim.massivepackage.Model;

import java.util.List;

/**
 * Created by julian.moses on 01/12/15.
 */
public class CatchmentModel {
    private int id;
    private String schoolName;
    private String website;
    private List<Coordinates> coordinatesList;
    private char schoolType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public List<Coordinates> getCoordinatesList() {
        return coordinatesList;
    }

    public void setCoordinatesList(List<Coordinates> coordinatesList) {
        this.coordinatesList = coordinatesList;
    }

    public char getSchoolType() {
        return schoolType;
    }

    public void setSchoolType(char schoolType) {
        this.schoolType = schoolType;
    }

    public static class Coordinates{
        private Double lat;
        private Double lng;

        public Double getLat() {
            return lat;
        }

        public void setLat(Double lat) {
            this.lat = lat;
        }

        public Double getLng() {
            return lng;
        }

        public void setLng(Double lng) {
            this.lng = lng;
        }
    }
}
