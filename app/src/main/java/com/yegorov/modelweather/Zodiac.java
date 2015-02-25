package com.yegorov.modelweather;

/**
 * Zodiac
 *
 * @author yegorov0725@yandex.ru
 * @version 1.0
 * @see com.yegorov.modelweather.Weather
 */
public enum Zodiac {
    /**
     * 1
     */
    Aries("Aries", '\u2648'),

    /**
     * 2
     */
    Taurus("Taurus", '\u2649'),

    /**
     * 3
     */
    Gemini("Gemini", '\u264A'),

    /**
     * 4
     */
    Cancer("Cancer", '\u264B'),

    /**
     * 5
     */
    Leo("Leo", '\u264C'),

    /**
     * 6
     */
    Virgo("Virgo", '\u264D'),

    /**
     * 7
     */
    Libra("Libra", '\u264E'),

    /**
     * 8
     */
    Scorpius("Scorpius", '\u264F'),

    /**
     * 9
     */
    Sagittarius("Sagittarius", '\u2650'),

    /**
     * 10
     */
    Capricornus("Capricornus", '\u2651'),

    /**
     * 11
     */
    Aquarius("Aquarius", '\u2652'),

    /**
     * 12
     */
    Pisces("Pisces", '\u2653');

    private String str;
    private char unicodeSign;

    private Zodiac(String str, char unicodeSign) {
        this.str = str;
        this.unicodeSign = unicodeSign;
    }

    public String toStr() {
        return str;
    }

    public char toSign() {
        return unicodeSign;
    }
}
