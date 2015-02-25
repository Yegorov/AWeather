package com.yegorov.util;

/**
 * Some functions for time/date
 *
 * @author yegorov0725@yandex.ru
 * @version 1.0
 * @see com.yegorov.modelweather.DataDay
 */
public class CalendarUtil {

    /**
     * Check for valid date
     * @param year
     * @param month
     * @param day
     * @return is valid date
     */
    public static boolean isDayOfMonth(int year, int month, int day) {

        int daysOfMonth;

        if((month < 1) || (12 < month))           // invalid month
            return false;

        switch (month) {                          // get days in this month
            case 4 : case 6 : case 9 : case 11 :  // Apr, Jun, Sep, Nov
                daysOfMonth = 30;
                break;
            case 2 :
                daysOfMonth = 28;                 // Feb normal
                if(isLeapYear(year)) {
                    daysOfMonth = 29;             // Feb leap year
                }
                break;
            default:
                daysOfMonth = 31;                 // other months
                break;
        }

        return (0 < day) && (day <= daysOfMonth);
    }

    /**
     * Determinate leap year
     * @param year
     * @return true - is leap year, else - false
     */
    public static boolean isLeapYear(int year) {
        boolean leapYear = false;

        if((year % 4 == 0) && (!((year % 100 == 0) && (year % 400 != 0))))
            leapYear = true;

        return leapYear;
    }
}

