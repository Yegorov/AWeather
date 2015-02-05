package com.yegorov.modelweather;

import com.yegorov.units.PressureUnits;

/**
 * Class that describes the pressure
 *
 * @author yegorov0725@yandex.ru
 * @version 1.0
 * @see com.yegorov.modelweather.Weather
 */
public class Pressure {

    // pressure value
    private double pressure;

    private PressureUnits pressureUnit;

    public Pressure() {
        this.pressure = 0.0;
        this.pressureUnit = PressureUnits.mm_Hg;
    }

    public Pressure(double pressure, PressureUnits pressureUnit) {
        setPressure(pressure, pressureUnit);
    }

    public double getPressure() {
        return pressure;
    }

    public PressureUnits getPressureUnit() {
        return pressureUnit;
    }

    public void setPressure(double pressure, PressureUnits pressureUnit) {
        this.pressure = pressure;
        this.pressureUnit = pressureUnit;
    }

    @Override
    public String toString() {
        String strFormat = "%.2f";
        switch (pressureUnit) {
            case mm_Hg:
                strFormat = "%.0f";
                break;
            case atm:
                strFormat = "%.2f";
                break;
            //TODO The format of display pressure, according to the unit of measurement
        }

        return String.format(strFormat, pressure) + " " + pressureUnit.toStr();
    }
}
