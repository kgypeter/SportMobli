package com.example.sportmobli.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Exercise implements Parcelable {
    private String name;
    private float duration; // Duration of the exercise in seconds
    private float restTime; // Rest time after the exercise in seconds

    public Exercise(String name, float duration, float restTime) {
        this.name = name;
        this.duration = duration;
        this.restTime = restTime;
    }

    protected Exercise(Parcel in) {
        name = in.readString();
        duration = in.readFloat();
        restTime = in.readFloat();
    }

    public static final Creator<Exercise> CREATOR = new Creator<Exercise>() {
        @Override
        public Exercise createFromParcel(Parcel in) {
            return new Exercise(in);
        }

        @Override
        public Exercise[] newArray(int size) {
            return new Exercise[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public float getRestTime() {
        return restTime;
    }

    public void setRestTime(float restTime) {
        this.restTime = restTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeFloat(duration);
        dest.writeFloat(restTime);
    }

}
