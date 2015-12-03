package com.example.julian.matthew.tamim.massivepackage.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Julian on 25/11/2015.
 */
public class SchoolModel implements Parcelable {
    private int id;
    private char schoolType;
    private String website;
    private String schoolName;
    private String location;
    private String ward;
    private LatLng coordinates;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeInt(schoolType);
        out.writeString(website);
        out.writeString(schoolName);
        out.writeString(location);
        out.writeString(ward);
        out.writeParcelable(coordinates, 0);

    }

    private SchoolModel(Parcel in) {
        id = in.readInt();
        schoolType = (char) in.readInt();
        website = in.readString();
        schoolName = in.readString();
        location = in.readString();
        ward = in.readString();
        coordinates = in.readParcelable(LatLng.class.getClassLoader());
    }

    public static final Parcelable.Creator<SchoolModel> CREATOR = new Parcelable.Creator<SchoolModel>() {
        public SchoolModel createFromParcel(Parcel in) {
            return new SchoolModel(in);
        }

        public SchoolModel[] newArray(int size) {
            return new SchoolModel[size];
        }
    };

    public SchoolModel() {

    }



    public LatLng getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(LatLng coordinates) {
        this.coordinates = coordinates;
    }

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
