package com.example.julian.matthew.tamim.massivepackage.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Julian Limited on 30/11/2015.
 */
public class ColdCallingModel implements Parcelable {
    private int id;
    private String zones;
    private String ward;
    private List<Coordinates> coordinatesList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getZones() {
        return zones;
    }

    public void setZones(String zones) {
        this.zones = zones;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public List<Coordinates> getCoordinatesList() {
        return coordinatesList;
    }

    public void setCoordinatesList(List<Coordinates> coordinatesList) {
        this.coordinatesList = coordinatesList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeString(zones);
        out.writeString(ward);
        out.writeTypedList(coordinatesList);
    }

    public static final Parcelable.Creator<ColdCallingModel> CREATOR = new Parcelable.Creator<ColdCallingModel>() {
        public ColdCallingModel createFromParcel(Parcel in) {
            return new ColdCallingModel(in);
        }

        public ColdCallingModel[] newArray(int size) {
            return new ColdCallingModel[size];
        }
    };

    public ColdCallingModel() {

    }

    private ColdCallingModel(Parcel in) {
        id = in.readInt();
        zones = in.readString();
        ward = in.readString();
        in.readTypedList(coordinatesList,ColdCallingModel.Coordinates.CREATOR);
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
