package com.dicoding.cinemaindonesiansubmission4.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Database implements Parcelable {

    private int id;
    private float rating;
    private String title, date, popularity, description, image;

    public Database() {
    }

    public Database(int id, float rating, String title, String date, String popularity, String description, String image) {
        this.id = id;
        this.rating = rating;
        this.title = title;
        this.date = date;
        this.popularity = popularity;
        this.description = description;
        this.image = image;
    }

    // getter
    public int getId() {
        return id;
    }

    public float getRating() {
        return rating;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getPopularity() {
        return popularity;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    // setter
    public void setId(int id) {
        this.id = id;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeFloat(rating);
        dest.writeString(title);
        dest.writeString(date);
        dest.writeString(popularity);
        dest.writeString(description);
        dest.writeString(image);
    }

    protected Database(Parcel in) {
        id = in.readInt();
        rating = in.readFloat();
        title = in.readString();
        date = in.readString();
        popularity = in.readString();
        description = in.readString();
        image = in.readString();
    }

    public static final Creator<Database> CREATOR = new Creator<Database>() {
        @Override
        public Database createFromParcel(Parcel in) {
            return new Database(in);
        }

        @Override
        public Database[] newArray(int size) {
            return new Database[size];
        }
    };
}
