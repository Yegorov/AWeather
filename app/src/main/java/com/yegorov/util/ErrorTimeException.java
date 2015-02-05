package com.yegorov.util;

/**
 * Exception class, when you specify a new time
 *
 * @author yegorov0725@yandex.ru
 * @version 1.0
 * @see com.yegorov.util.Time
 * @see com.yegorov.modelweather.DataDay
 */
public class ErrorTimeException extends Exception {
    public ErrorTimeException(String message) {
        super(message);
    }
}
