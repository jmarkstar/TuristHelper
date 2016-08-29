package com.jmarkstar.turisthelper.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jmarkstar on 28/08/2016.
 */
public class Venue implements Parcelable {

    private String id;
    private String name;
    private String formattedAddress;
    private String latitude;
    private String longitude;
    private String photo;
    private String url;
    private String rating;
    private String distance;

    public static final Parcelable.Creator<Venue> CREATOR = new Parcelable.Creator<Venue>(){

        @Override
        public Venue createFromParcel(Parcel parcel) {
            return new Venue(parcel);
        }

        @Override
        public Venue[] newArray(int i) {
            return new Venue[i];
        }
    };

    public Venue(){}

    protected Venue(Parcel in){
        this.id = in.readString();
        this.name = in.readString();
        this.formattedAddress = in.readString();
        this.latitude = in.readString();
        this.longitude = in.readString();
        this.photo = in.readString();
        this.url = in.readString();
        this.rating = in.readString();
        this.distance = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(formattedAddress);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeString(photo);
        dest.writeString(url);
        dest.writeString(rating);
        dest.writeString(distance);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "Venue{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", formattedAddress='" + formattedAddress + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", photo='" + photo + '\'' +
                ", url='" + url + '\'' +
                ", rating='" + rating + '\'' +
                ", distance='" + distance + '\'' +
                '}';
    }
}
