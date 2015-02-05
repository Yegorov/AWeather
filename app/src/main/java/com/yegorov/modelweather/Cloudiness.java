package com.yegorov.modelweather;

/**
 * Cloudiness
 *
 * @author yegorov0725@yandex.ru
 * @version 1.0
 */
public enum Cloudiness {

    Clear(new String[] { "Clear", "clear weather", "partly cloudy", "sunny weather" }),


    VariableCloudy(new String[] { "Variable cloudy" }),


    MostlyCloudy(new String[] { "Mostly cloudy", "cloudy weather with clearings" }),


    Cloudy(new String[] { "Cloudy", "considerable cloudiness", "overcast" }),


    Indeterminately(new String[] { "" });


    private String[] str;

    /**
     * Private constructor
     * @param str - array of strings describing cloudiness
     */
    private Cloudiness(String[] str) {
        this.str = str;
    }

    /**
     * String value cloudiness
     * @return String
     */
    public String toStr() {
        return str[0];
    }

    /**
     * Concatenation string array that describes cloudiness
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
     * String value cloudiness
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