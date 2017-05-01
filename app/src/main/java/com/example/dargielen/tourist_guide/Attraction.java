package com.example.dargielen.tourist_guide;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dargielen on 24.03.2017.
 */

public class Attraction implements Parcelable {

    private String name, address, short_description, long_description, image;
    private double longitude, latitude;
    //private List<String> photo;

    public Attraction(String name, String address, String short_description, String long_description, String image, Double longitude, Double latitude){
        this.name = name;
        this.address = address;
        this.short_description = short_description;
        this.long_description = long_description;
        this.image = image;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    //setters i getters
    public void setName(String name) {
        this.name = name;
    }
    public  void setAdress(String adress) {
        this.address = adress;
    }
    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }
    public void setLong_description(String long_description) {
        this.long_description = long_description;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public void setImage(String image) {
        this.image = image;
    }
    /*public void setPhoto(String new_photo) {
        this.photo.add(new_photo);
    }*/

    String getName() {
        return this.name;
    }
    String getAddress() {
        return this.address;
    }
    String getShort_description() {
        return this.short_description;
    }
    String getLong_description() {
        return this.long_description;
    }
    String getImage() {
        return this.image;
    }
    double getLongitude() {
        return this.longitude;
    }
    double getLatitude() {
        return this.latitude;
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
        pc.writeString(address);
        pc.writeString(short_description);
        pc.writeString(long_description);
        pc.writeDouble(longitude);
        pc.writeDouble(latitude);
        pc.writeString(image);
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
        address = pc.readString();
        short_description = pc.readString();
        long_description = pc.readString();
        longitude = pc.readDouble();
        latitude = pc.readDouble();
        image = pc.readString();
    }

}
