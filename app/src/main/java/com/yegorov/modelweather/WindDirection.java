package com.yegorov.modelweather;

/**
 * Wind Direction (compass points)
 *
 * @author yegorov0725@yandex.ru
 * @version 1.0
 */
public enum WindDirection {

    N("North"),

    E("East"),

    S("South"),

    W("West"),

    С("Calm"),

    NE("Northeast"),

    SE("Southeast"),

    SW("Southwest"),

    NW("Northwest"),

    Indeterminately("");
    // possible even north-north-east and etc.


    private String str;

    private WindDirection(String str) {
        this.str = str;
    }

    public String toStr() {
        return str;
    }

}
