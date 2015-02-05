package com.yegorov.modelweather;

/**
 * Precipitation
 *
 * @author yegorov0725@yandex.ru
 * @version 1.0
 */
public enum Precipitation {

    Dry(new String[] { "Dry", "no precipitation", "dry weather" }),


    Drizzle(new String[] { "Drizzle", "light rain", "light rain",
                           "drizzling rain", "precipitation" }),


    Rain(new String[] { "Rain", "sleet", "rain and snow",
                        "snow, rolling in the rain",
                        "rain, rolling in the snow" }),


    HeavyRain(new String[] {"Heavy rain", "downpour", "heavy rainfall", "strong wet snow",
                            "heavy snow, rolling in the rain", "heavy rain, rolling in the snow" }),


    VeryHeavyRain(new String[] { "Very heavy rain", "very heavy rainfall",
                                 "very heavy snow, rolling in the rain",
                                 "very heavy rain, rolling in the snow" }),


    LightSnow(new String[] {"Light snow", "Some snow" }),


    Snow(new String[] {"Snow", "snowfall"}),


    HeavySnow(new String[] { "Heavy snow", "heavy snowfall" }),


    VeryHeavySnow(new String[] { "Very heavy snow", "very heavy snowfall" } ),


    Indeterminately(new String[] { "" });

    private String[] str;

    /**
     * Private constructor
     * @param str - array of strings describing precipitation
     */
    private Precipitation(String[] str) {
        this.str = str;
    }

    /**
     * String value precipitation
     * @return String
     */
    public String toStr() {
        return str[0];
    }

    /**
     * Concatenation string array that describes precipitation
     * @return String
     */
    public String toStrAll() {
        StringBuilder sb = new StringBuilder(str[0]);
        for(int i = 1; i < str.length; ++i) {
            sb.append(" ");
            sb.append(str[i]);
        }
        return sb.toString();
    }

    /**
     * String value precipitation
     * @param i - number string value
     * @return String
     */
    public String getStr(int i) {
        if(i < 0 || i >= str.length) {
            return toStr();
        }
        return str[i];
    }
}