package com.yegorov.units;

/**
 * Temperature Units
 *
 * @author yegorov0725@yandex.ru
 * @version 1.0
 */

public enum TemperatureUnits {

    /**
     * The degree Celsius
     */
    C("C"),

    /**
     * Fahrenheit
     */
    F("F"),

    /**
     * Kelvin
     */
    K("K");

    private String str;

    private TemperatureUnits(String str) {
        this.str = str;
    }

    public String toStr() {
        return str;
    }

}
