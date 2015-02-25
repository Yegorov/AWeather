package com.yegorov.util;

/**
 * Class that describes the time in 24 hour format
 *
 * @author yegorov0725@yandex.ru
 * @version 1.0
 * @see com.yegorov.modelweather.DataDay
 */
public class Time {
    /**
     * The maximum value of hour
     */
    public static final int MAX_HOUR = 23;

    /**
     * The maximum value of minutes
     */
    public static final int MAX_MINUTE = 59;

    /**
     * The maximum value of seconds
     */
    public static final int MAX_SECOND = 59;

    /**
     * The minimum value of all the characteristics of the time
     */
    public static final int MIN_TIME = 0;

    /**
     * The number of seconds in a minute
     */
    public static final int SECONDS_IN_MINUTE = 60;

    /**
     * The number of minutes in an hour
     */
    public static final int MINUTES_IN_HOUR = 60;

    /**
     * Number of hours in the day
     */
    public static final int HOURS_IN_DAY = 24;

    /**
     * Hours
     */
    private int hours;

    /**
     * Minutes
     */
    private int minutes;

    /**
     * Seconds
     */
    private int seconds;

    /**
     * Private constructor
     * @param hours from 0 to 23
     * @param minutes from 0 to 59
     * @param seconds from 0 to 59
     */
    private Time(int hours, int minutes, int seconds) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    /**
     * Factory Method
     * @param hours часы от 0 до 23
     * @param minutes минуты от 0 до 59
     * @param seconds секунды от 0 до 59
     * @return Новый объект класса <b>Time</b> или если не верно указаны парметры - null
     */
    public static Time newInstance(int hours, int minutes, int seconds) throws ErrorTimeException {
        if(  hours   < MIN_TIME || hours   > MAX_HOUR   ||
             minutes < MIN_TIME || minutes > MAX_MINUTE ||
             seconds < MIN_TIME || seconds > MAX_SECOND)
            throw new ErrorTimeException("No valid Time");
        return new Time(hours, minutes, seconds);
    }

    /**
     * Converts in a second format hh:mm:ss
     * Privat method (no check)
     * @param seconds Number of seconds
     * @return New object classes <b>Time</b>
     */
    private static Time valueOf(int seconds) {
        int hours = seconds / (SECONDS_IN_MINUTE * MINUTES_IN_HOUR);
        seconds -= SECONDS_IN_MINUTE * MINUTES_IN_HOUR * hours;
        int minutes = seconds / SECONDS_IN_MINUTE;
        seconds -= SECONDS_IN_MINUTE * minutes;
        return new Time(hours, minutes, seconds);
    }

    /**
     * Converts in a second format hh:mm:ss
     * @param seconds Number of seconds
     * @return New object classes <b>Time</b> or null
     */
    public static Time valueOfSeconds(int seconds) {
        if(seconds < 0) return null;
        int hours = seconds / (SECONDS_IN_MINUTE * MINUTES_IN_HOUR);
        seconds -= SECONDS_IN_MINUTE * MINUTES_IN_HOUR * hours;
        int minutes = seconds / SECONDS_IN_MINUTE;
        seconds -= SECONDS_IN_MINUTE * minutes;

        hours %= HOURS_IN_DAY;
        minutes %= MINUTES_IN_HOUR;

        return new Time(hours, minutes, seconds);
    }

    /**
     * Converts in a minutes format hh:mm
     * @param minutes Number of minutes
     * @return New object classes <b>Time</b> or null
     */
    public static Time valueOfMinutes(int minutes) {
        if(minutes < 0) return null;
        int hours = minutes / MINUTES_IN_HOUR;
        minutes -= MINUTES_IN_HOUR * hours;
        hours %= HOURS_IN_DAY;
        return new Time(hours, minutes, 0);
    }

    /**
     * Converts double hour to format hh:mm
     *
     * @param hour
     * @return Time
     */
    public static Time valueOfHour(double hour) {
        int h, m;
        double min;
        h = (int)hour;
        min = hour - h;
        m = (int)(min * MINUTES_IN_HOUR);
        return new Time(h, m, 0);
    }

    /**
     * Time frame
     * @param time1 - first time
     * @param time2 - second time
     * @return New object classes <b>Time</b> - time frame
     */
    public static Time duration(Time time1, Time time2) {
        if(time1 == null || time2 == null) return null;
        int allSeconds = Math.abs(time1.getAllSeconds() - time2.getAllSeconds());
        return valueOf(allSeconds);
    }

    /**
     * @return Hours 0-23
     */
    public int getHours() {
        return hours;
    }

    /**
     * Set hours
     * @param hours from 0 to 23
     * @throws ErrorTimeException
     */
    public void setHours(int hours) throws ErrorTimeException {
        if(hours < MIN_TIME || hours > MAX_HOUR)
            throw new ErrorTimeException("Hours less than 0 or more than 23");
        this.hours = hours;
    }

    /**
     * @return Minutes 0-59
     */
    public int getMinutes() {
        return minutes;
    }

    /**
     * Set minutes
     * @param minutes from 0 to 59
     * @throws ErrorTimeException
     */
    public void setMinutes(int minutes) throws ErrorTimeException {
        if(minutes < MIN_TIME || minutes > MAX_MINUTE)
            throw new ErrorTimeException("Minutes less than 0 or more than 59");
        this.minutes = minutes;
    }

    /**
     * @return Seconds 0-59
     */
    public int getSeconds() {
        return seconds;
    }

    /**
     * Set seconds
     * @param seconds from 0 to 59
     * @throws ErrorTimeException
     */
    public void setSeconds(int seconds) throws ErrorTimeException {
        if(seconds < MIN_TIME || seconds > MAX_SECOND)
            throw new ErrorTimeException("Seconds less than 0 or more than 59");
        this.seconds = seconds;
    }

    /**
     * Time format, no seconds
     * @return String format hh:mm (06:45)
     */
    public String toStringFormatHHMM() {
        return String.format("%02d:%02d", hours, minutes);
    }

    /**
     * All number of seconds. For example, 01:10:05 = 3665 s
     * @return Number of seconds (int)
     */
    public int getAllSeconds() {
        return seconds + SECONDS_IN_MINUTE * (minutes + MINUTES_IN_HOUR * hours);
    }

    /**
     * All number of minutes. For example, 01:10:05 = 70 m
     * @return Количество минут (int)
     */
    public int getAllMinutes() {
        return minutes + MINUTES_IN_HOUR * hours;
    }



    /**
     * @deprecated test method, please no use
     *
     */
    public String toStringFormatplus1() {
        seconds++;

        if(seconds > MAX_SECOND) {
            minutes++;
        }

        if(minutes > MAX_MINUTE) {
            hours++;
        }

        seconds = (byte)(seconds % SECONDS_IN_MINUTE);
        minutes = (byte)(minutes % MINUTES_IN_HOUR);
        hours = (byte)(hours % HOURS_IN_DAY);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public String toString(char s) {
        return String.format("%02d" + s + "%02d" + s + "%02d", hours, minutes, seconds);
    }
}
