package com.yegorov.modelweather;

import com.yegorov.units.TemperatureUnits;

/**
 * Class describing the temperature
 *
 * @author yegorov0725@yandex.ru
 * @version 1.0
 * @see com.yegorov.modelweather.Weather
 */
public class Temperature {

    // temperature value
    private double temp;

    private TemperatureUnits unitTemp;

    public Temperature() {
        this.temp = 0.0;
        this.unitTemp = TemperatureUnits.C;
    }

    public Temperature(double temp, TemperatureUnits unitTemp) {
        setTemperature(temp, unitTemp);
    }

    public double getTemp() {
        return temp;
    }

    public TemperatureUnits getUnitTemp() {
        return unitTemp;
    }

    public void setTemperature(double temp, TemperatureUnits unitTemp) {
        this.temp = temp;
        this.unitTemp = unitTemp;
    }

    @Override
    public String toString() {
        return String.format("%.1f", temp) + " \u00B0" + unitTemp.toStr();
    }
}
