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
    NewMoon,

    /**
     * The young moon - the first appearance of the moon in the sky after the new moon in the form of a narrow crescent.
     */
    WaxingCrescent,

    /**
     * First quarter - a condition where the illuminated half of the moon.
     */
    FirstQuarter,

    /**
     * The waxing moon.
     */
    WaxingGibbous,

    /**
     * Full Moon - a condition where the whole moon lit entirely.
     */
    FullMoon,

    /**
     * Waning moon.
     */
    WaningGibbous,

    /**
     * Last quarter - a condition where the back half of the moon is illuminated.
     */
    ThirdQuarter,

    /**
     * Old moon.
     */
    WaningCrescent,
}
