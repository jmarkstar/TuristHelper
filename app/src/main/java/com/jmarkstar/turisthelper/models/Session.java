package com.jmarkstar.turisthelper.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/** Foursquare user's entity
 * Created by jmarkstar on 27/08/2016.
 */
public class Session extends RealmObject {

    @PrimaryKey
    private String id;
    private String fourSquareId;
    private String fullName;
    private String photo;
    private String email;
    private String token;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFourSquareId() {
        return fourSquareId;
    }

    public void setFourSquareId(String fourSquareId) {
        this.fourSquareId = fourSquareId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "Session{" +
                "id='" + id + '\'' +
                ", fourSquareId='" + fourSquareId + '\'' +
                ", fullName='" + fullName + '\'' +
                ", photo='" + photo + '\'' +
                ", email='" + email + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
