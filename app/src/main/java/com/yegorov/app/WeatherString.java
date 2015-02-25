package com.yegorov.app;

import android.os.Parcel;
import android.os.Parcelable;

public class WeatherString implements Parcelable {
    public final static String TAG = "WeatherString";

    private String airTemp;
    private String barPressure;
    private String humidity;
    private String windSpeed;
    private String windDirection;

    public WeatherString() {
        this.airTemp = "Unknown";
        this.barPressure = "Unknown";
        this.humidity = "Unknown";
        this.windSpeed = "Unknown";
        this.windDirection = "Unknown";
    }

    public String getAirTemp() {
        return airTemp;
    }

    public void setAirTemp(String airTemp) {
        this.airTemp = airTemp;
    }

    public String getBarPressure() {
        return barPressure;
    }

    public void setBarPressure(String barPressure) {
        this.barPressure = barPressure;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(airTemp);
        dest.writeString(barPressure);
        dest.writeString(humidity);
        dest.writeString(windSpeed);
        dest.writeString(windDirection);
    }

    public static final Creator<WeatherString> CREATOR = new Creator<WeatherString>() {
        @Override
        public WeatherString createFromParcel(Parcel source) {
            return new WeatherString(source);
        }

        @Override
        public WeatherString[] newArray(int size) {
            return new WeatherString[size];
        }
    };

    private WeatherString(Parcel in) {
        this.airTemp = in.readString();
        this.barPressure = in.readString();
        this.humidity = in.readString();
        this.windSpeed = in.readString();
        this.windDirection = in.readString();
    }

    @Override
    public String toString() {
        return airTemp + ", " + barPressure + ", \nHumidity: " + humidity + ", \nWind: " + windSpeed + ", " + windDirection;
    }
}
