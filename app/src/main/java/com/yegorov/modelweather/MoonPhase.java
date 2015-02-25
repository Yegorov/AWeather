package com.yegorov.modelweather;

/**
 * Moon phase
 *
 * @author yegorov0725@yandex.ru
 * @version 1.0
 * @see com.yegorov.modelweather.Weather
 */

public enum MoonPhase {
    /**
     * New Moon - state, when the moon is not visible.
     */
    NewMoon("New Moon"),

    /**
     * The young moon - the first appearance of the moon in the sky after the new moon in the form of a narrow crescent.
     */
    WaxingCrescent("Waxing Crescent"),

    /**
     * First quarter - a condition where the illuminated half of the moon.
     */
    FirstQuarter("First Quarter"),

    /**
     * The waxing moon.
     */
    WaxingGibbous("Waxing Gibbous"),

    /**
     * Full Moon - a condition where the whole moon lit entirely.
     */
    FullMoon("Full Moon"),

    /**
     * Waning moon.
     */
    WaningGibbous("Waning Gibbous"),

    /**
     * Last quarter - a condition where the back half of the moon is illuminated.
     */
    ThirdQuarter("Third Quarter"),

    /**
     * Old moon.
     */
    WaningCrescent("Waning Crescent");

    private String str;

    private MoonPhase(String str) {
        this.str = str;
    }

    public String toStr() {
        return str;
    }
}
