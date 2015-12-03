package com.example.julian.matthew.tamim.massivepackage.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Julian Limited on 25/11/2015.
 */
public class CrimeModel implements Parcelable {
    private int id;
    private String category;
    private String location_type;
    private String latitude;
    private String longitude;
    private String month;
    private String street_name;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeString(category);
        out.writeString(location_type);
        out.writeString(latitude);
        out.writeString(longitude);
        out.writeString(month);
        out.writeString(street_name);
    }

    public static final Parcelable.Creator<CrimeModel> CREATOR = new Parcelable.Creator<CrimeModel>() {
        public CrimeModel createFromParcel(Parcel in) {
            return new CrimeModel(in);
        }

        public CrimeModel[] newArray(int size) {
            return new CrimeModel[size];
        }
    };

    public CrimeModel() {

    }

    private CrimeModel(Parcel in) {
        id = in.readInt();
        category = in.readString();
        location_type = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        month = in.readString();
        street_name = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation_type() {
        return location_type;
    }

    public void setLocation_type(String location_type) {
        this.location_type = location_type;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getStreet_name() {
        return street_name;
    }

    public void setStreet_name(String street_name) {
        this.street_name = street_name;
    }
}
