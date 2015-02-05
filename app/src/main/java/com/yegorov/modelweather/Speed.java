package com.yegorov.modelweather;

import com.yegorov.units.SpeedUnits;

/**
 * Class that describes the speed
 *
 * @author yegorov0725@yandex.ru
 * @version 1.0
 * @see com.yegorov.modelweather.Weather
 */
public class Speed {

    // speed value
    private double speed;

    private SpeedUnits speedUnit;

    public Speed() {
        this.speed = 0.0;
        this.speedUnit = SpeedUnits.m_s;
    }

    public Speed(double speed, SpeedUnits speedUnit) {
        setSpeed(speed, speedUnit);
    }

    public double getSpeed() {
        return speed;
    }

    public SpeedUnits getSpeedUnit() {
        return speedUnit;
    }

    public void setSpeed(double speed, SpeedUnits speedUnit) {
        this.speed = speed;
        this.speedUnit = speedUnit;
    }

    @Override
    public String toString() {

        String strFormat = "%.2f";
        //TODO The format of display speed, according to the unit of measurement
        /*
        switch (speedUnit) {
            case m_s:
                strFormat = "%.2f";
                break;
            case km_h:
                strFormat = "%.2f";
                break;

        }
        */
        return String.format(strFormat, speed) + " " + speedUnit.toStr();
    }
}
