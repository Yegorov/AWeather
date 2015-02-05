package com.yegorov.units;

/**
 * Pressure measurement units
 *
 * @author yegorov0725@yandex.ru
 * @version 1.0
 */
public enum PressureUnits {
    /**
     * Millimeter of mercury
     */
    mm_Hg("mm Hg"),

    /**
     * Pascal
     */
    Pa("Pa"),

    /**
     * Bar
     */
    bar("bar"),

    /**
     * Standard atmosphere
     */
    atm("atm"),

    /**
     * Pounds per square inch
     */
    psi("psi");

    private String str;

    private PressureUnits(String str) {
        this.str = str;
    }

    public String toStr() {
        return str;
    }

}