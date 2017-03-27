package com.example.dargielen.tourist_guide;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dargielen on 24.03.2017.
 */

public class Attraction implements Parcelable {

    private String name, adress, short_description, long_description;
    private Long longitude, latitude;
    //private List<String> photo;

    public Attraction(){};

    //setters i getters
    public void setName(String name) {
        this.name = name;
    }
    public  void setAdress(String adress) {
        this.adress = adress;
    }
    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }
    public void setLong_description(String long_description) {
        this.long_description = long_description;
    }
    public void setLongitude(Long longitude) {
        this.longitude = longitude;
    }
    public void setLatitude(Long latitude) {
        this.latitude = latitude;
    }
    /*public void setPhoto(String new_photo) {
        this.photo.add(new_photo);
    }*/

    String getName() {
        return this.name;
    }
    String getAdress() {
        return this.adress;
    }
    String getShort_description() {
        return this.short_description;
    }
    String getLong_description() {
        return this.long_description;
    }
    /*List<String> getPhoto() {
        return this.photo;
    }*/

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel pc, int flags) {
        pc.writeString(name);
        pc.writeString(adress);
        pc.writeString(short_description);
        pc.writeString(long_description);
        pc.writeLong(longitude);
        pc.writeLong(latitude);
        //pc.writeArray(photo<String>);
    }

    public static final Parcelable.Creator<Attraction> CREATOR = new Parcelable.Creator<Attraction>() {
        public Attraction createFromParcel(Parcel pc) {
            return new Attraction(pc);
        }
        public Attraction[] newArray(int size) {
            return new Attraction[size];
        }
    };

    public Attraction(Parcel pc) {
        name = pc.readString();
        adress = pc.readString();
        short_description = pc.readString();
        long_description = pc.readString();
        longitude = pc.readLong();
        latitude = pc.readLong();
    }

}
