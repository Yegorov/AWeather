package com.yegorov.util;

/**
 * Exception class
 *
 * @author yegorov0725@yandex.ru
 * @version 1.0
 * @see com.yegorov.modelweather
 * @see com.yegorov.modelweather.DataDay
 */
public class NoValidDateException extends Exception {
    public NoValidDateException(String message) {
        super(message);
    }
}
