package com.yegorov.units;

/**
 * Speed units
 *
 * @author yegorov0725@yandex.ru
 * @version 1.0
 */
public enum SpeedUnits {

    /**
     * Meter per second
     */
    m_s("m/s"),

    /**
     * Kilometres per hour
     */
    km_h("km/h"),

    /**
     * Foot per second
     */
    ft_s("ft/s"),

    /**
     * Miles per hour
     */
    mph("mph"),

    /**
     * Knot
     */
    knot("knot");

    private String str;

    private SpeedUnits(String str) {
        this.str = str;
    }

    public String toStr() {
        return str;
    }

}
