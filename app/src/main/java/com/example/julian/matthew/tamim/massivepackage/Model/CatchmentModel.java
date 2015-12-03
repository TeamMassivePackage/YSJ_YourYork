package com.example.julian.matthew.tamim.massivepackage.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by julian.moses on 01/12/15.
 */
public class CatchmentModel implements Parcelable {
    private int id;
    private String schoolName;
    private String website;
    private List<Coordinates> coordinatesList;
    private char schoolType;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeString(schoolName);
        out.writeString(website);
        out.writeTypedList(coordinatesList);
        out.writeInt(schoolType);
    }

    public static final Parcelable.Creator<CatchmentModel> CREATOR = new Parcelable.Creator<CatchmentModel>() {
        public CatchmentModel createFromParcel(Parcel in) {
            return new CatchmentModel(in);
        }

        public CatchmentModel[] newArray(int size) {
            return new CatchmentModel[size];
        }
    };

    public CatchmentModel() {

    }

    private CatchmentModel(Parcel in) {
        id = in.readInt();
        schoolName = in.readString();
        website = in.readString();
        in.readTypedList(coordinatesList,CatchmentModel.Coordinates.CREATOR);
        schoolType =(char) in.readInt();
    }


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

    public static class Coordinates implements Parcelable{
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            out.writeDouble(lat);
            out.writeDouble(lng);
        }

        public static final Parcelable.Creator<Coordinates> CREATOR = new Parcelable.Creator<Coordinates>() {
            public Coordinates createFromParcel(Parcel in) {
                return new Coordinates(in);
            }

            public Coordinates[] newArray(int size) {
                return new Coordinates[size];
            }
        };

        public Coordinates() {

        }

        private Coordinates(Parcel in) {
            lat = in.readDouble();
            lng = in.readDouble();
        }
    }
}
