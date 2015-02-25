package com.yegorov.modelweather;

/**
 * Data that does not change during the day
 *
 * @author yegorov0725@yandex.ru
 * @version 1.0
 * @see com.yegorov.util.Time
 */
public class DataDay {
    // TODO Class that controls parameters moon and sun (see as Weather class)

    private int day;

    private int month;

    private int year;

    /**
     * Sun's parameters
     * @see com.yegorov.modelweather.Sun
     */
    private Sun sun;

    /**
     *  Moon's parameters
     *  @see com.yegorov.modelweather.Moon
     */
    private Moon moon;

    /**
     * Private constructor
     * @param sun Sun's parameters
     * @param moon Moon's parameters
     */
    private DataDay(Sun sun, Moon moon) {
        this.sun = sun;
        this.moon = moon;
    }

    private DataDay(int day, int month, int year) {
        this.sun = new Sun();
        this.moon = new Moon();

        this.day = day;
        this.month = month;
        this.year = year;
    }

    /**
     * Factory Method
     * @param sun Sun's parameters
     * @param moon Moon's parameters
     * @return New object <b>DataDay</b>, which contains data that are constant for one day
     */
    public static DataDay newInstance(Sun sun, Moon moon) {
        return new DataDay(sun, moon);
    }

    public static DataDay newInstance(int day, int month, int year) {
        return new DataDay(day, month, year);
    }

    /**
     * @return Sun's parameters (sunrise and sunset)
     * @see com.yegorov.util.Time
     */
    public Sun getSun() {
        return sun;
    }

    /**
     * @param sun Time of sunrise
     * @see com.yegorov.util.Time
     */
    public void setSun(Sun sun) {
        this.sun = sun;
    }

    /**
     * @return Moon's parameters
     * @see com.yegorov.modelweather.Moon
     */
    public Moon getMoon() {
        return moon;
    }

    /**
     * @param moon Moon's parameters
     * @see com.yegorov.modelweather.Moon
     */
    public void setMoon(Moon moon) {
        this.moon = moon;
    }
}
