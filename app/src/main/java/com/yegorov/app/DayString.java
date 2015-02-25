package com.yegorov.app;


import android.os.Parcel;
import android.os.Parcelable;

public class DayString implements Parcelable{
    public final static String TAG = "DayString";

    private String date;
    private String sunRise;
    private String sunSet;
    private String phaseMoon;

    public DayString() {
        this.date = "Unknown";
        this.sunRise = "Unknown";
        this.sunSet = "Unknown";
        this.phaseMoon = "Unknown";
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSunRise() {
        return sunRise;
    }

    public void setSunRise(String sunRise) {
        this.sunRise = sunRise;
    }

    public String getPhaseMoon() {
        return phaseMoon;
    }

    public void setPhaseMoon(String phaseMoon) {
        this.phaseMoon = phaseMoon;
    }

    public String getSunSet() {
        return sunSet;
    }

    public void setSunSet(String sunSet) {
        this.sunSet = sunSet;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(sunRise);
        dest.writeString(sunSet);
        dest.writeString(phaseMoon);
    }

    public static final Creator<DayString> CREATOR = new Creator<DayString>() {
        @Override
        public DayString createFromParcel(Parcel source) {
            return new DayString(source);
        }

        @Override
        public DayString[] newArray(int size) {
            return new DayString[size];
        }
    };

    private DayString(Parcel in) {
        this.date = in.readString();
        this.sunRise = in.readString();
        this.sunSet = in.readString();
        this.phaseMoon = in.readString();
    }
}
