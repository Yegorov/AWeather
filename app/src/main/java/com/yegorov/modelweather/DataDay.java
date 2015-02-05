package com.yegorov.modelweather;

import com.yegorov.util.Time;

/**
 * Data that does not change during the day
 *
 * @author yegorov0725@yandex.ru
 * @version 1.0
 * @see com.yegorov.util.Time
 */
public class DataDay {
    /**
     * Time of sunrise
     */
    private Time sunRise;

    /**
     * Time of sunset
     */
    private Time sunSet;

    /**
     *  Moon phase
     *  @see com.yegorov.modelweather.MoonPhase
     */
    private MoonPhase moonPhase;

    /**
     * Private constructor
     * @param sunRise Time of sunrise
     * @param sunSet Time of sunset
     * @param moonPhase Moon phase
     */
    private DataDay(Time sunRise, Time sunSet, MoonPhase moonPhase) {
        this.sunRise = sunRise;
        this.sunSet = sunSet;
        this.moonPhase = moonPhase;
    }

    /**
     * Factory Method
     * @param sunRise Time of sunrise
     * @param sunSet Time of sunset
     * @param moonPhase Moon phase
     * @return New object <b>DataDay</b>, which contains data that are constant for one day
     */
    public static DataDay newInstance(Time sunRise, Time sunSet, MoonPhase moonPhase) {
        return new DataDay(sunRise, sunSet, moonPhase);
    }

    /**
     * @return <b>Time</b> of sunrise
     * @see com.yegorov.util.Time
     */
    public Time getSunRise() {
        return sunRise;
    }

    /**
     * @param sunRise Time of sunrise
     * @see com.yegorov.util.Time
     */
    public void setSunRise(Time sunRise) {
        this.sunRise = sunRise;
    }

    /**
     * @return <b>Time</b> of sunset
     * @see com.yegorov.util.Time
     */
    public Time getSunSet() {
        return sunSet;
    }

    /**
     * @param sunSet Time of sunset
     * @see com.yegorov.util.Time
     */
    public void setSunSet(Time sunSet) {
        this.sunSet = sunSet;
    }

    /**
     * @return <b>MoonPhase</b> - Moon phase
     * @see com.yegorov.modelweather.MoonPhase
     */
    public MoonPhase getMoonPhase() {
        return moonPhase;
    }

    /**
     * @param moonPhase Moon phase
     * @see com.yegorov.modelweather.MoonPhase
     */
    public void setMoonPhase(MoonPhase moonPhase) {
        this.moonPhase = moonPhase;
    }
}
