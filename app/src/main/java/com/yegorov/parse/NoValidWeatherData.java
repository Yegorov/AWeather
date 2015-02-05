package com.yegorov.parse;

/**
 * Exception class when receiving of incorrect weather data
 */
public class NoValidWeatherData extends Exception {
    public NoValidWeatherData(String message) {
        super(message);
    }
}