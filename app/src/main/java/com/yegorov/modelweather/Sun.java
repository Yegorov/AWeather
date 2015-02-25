package com.yegorov.modelweather;

import com.yegorov.util.CalendarUtil;
import com.yegorov.util.NoValidDateException;
import com.yegorov.util.Time;

/**
 * Sun properties
 *
 * @author yegorov0725@yandex.ru
 * @version 1.0
 * @see com.yegorov.modelweather.DataDay
 */
public class Sun {
    /**
     * Time of sunrise
     */
    private Time sunRise;

    /**
     * Time of sunset
     */
    private Time sunSet;

    public Sun() {
        this.sunRise = null;
        this.sunSet = null;
    }

    public void init(int day, int month, int year,
                     double latitude, double longitude, double localOffset) throws NoValidDateException {
        calculateSunRiseAndSet(year, month, day, latitude, longitude, localOffset, Zenith.offical);
    }

    public void setParameters(Time sunRise, Time sunSet) {
        this.sunRise = sunRise;
        this.sunSet = sunSet;
    }


    public void calculateSunRiseAndSet(int year, int month, int day,
                                       double latitude, double longitude, double localOffset,
                                       double zenith) throws NoValidDateException {

        if(!CalendarUtil.isDayOfMonth(year, month, day))
            throw new NoValidDateException("Date is no valid: " + day + "/" + month + "/" + year);


        double tSunRise, tSunSet;
        double mSunRise, mSunSet;
        double lSunRise, lSunSet;
        double raSunRise, raSunSet;

        double Lquadrant, RAquadrant;

        double sinDecSunRise, cosDecSunRise, sinDecSunSet, cosDecSunSet;

        double cosHSunRise, cosHSunSet;

        double HSunRise, HSunSet;

        double N, N1, N2, N3;

        double lngHour;

        //1. first calculate the day of the year
        N1 = Math.floor(275 * month / 9);
        N2 = Math.floor((month + 9) / 12);
        N3 = (1 + Math.floor((year - 4 * Math.floor(year / 4) + 2) / 3));
        N = N1 - (N2 * N3) + day - 30;


        //2. convert the longitude to hour value and calculate an approximate time
        lngHour = longitude / 15.0;
        tSunRise = N + ((6.0  - lngHour) / 24.0);
        tSunSet  = N + ((18.0 - lngHour) / 24.0);

        //3. calculate the Sun's mean anomaly
        mSunRise = (0.9856 * tSunRise) - 3.289;
        mSunSet  = (0.9856 * tSunSet)  - 3.289;

        //4. calculate the Sun's true longitude
        //        [Note throughout the arguments of the trig functions
        //        (sin, tan) are in degrees. It will likely be necessary to
        //        convert to radians. eg sin(170.626 deg) =sin(170.626*pi/180 radians)=0.16287]
        lSunRise = mSunRise + (1.916 * Math.sin(Math.toRadians(mSunRise))) + (0.020 * Math.sin(Math.toRadians(2 * mSunRise))) + 282.634;
        lSunSet  = mSunSet  + (1.916 * Math.sin(Math.toRadians(mSunSet)))  + (0.020 * Math.sin(Math.toRadians(2 * mSunSet)))  + 282.634;
        //NOTE: L potentially needs to be adjusted into the range [0,360) by adding/subtracting 360

        if(lSunRise < 0) {
            while (!(0 <= lSunRise && lSunRise < 360))
                lSunRise += 360;
        }
        else if(lSunRise >= 360) {
            while (!(0 <= lSunRise && lSunRise < 360))
                lSunRise -= 360;
        }

        if(lSunSet < 0) {
            while (!(0 <= lSunSet && lSunSet < 360))
                lSunSet += 360;
        }
        else if(lSunSet >= 360) {
            while (!(0 <= lSunSet && lSunSet < 360))
                lSunSet -= 360;
        }



        //5a. calculate the Sun's right ascension
        raSunRise = Math.toDegrees(Math.atan(0.91764 * Math.tan(Math.toRadians(lSunRise))));
        raSunSet  = Math.toDegrees(Math.atan(0.91764 * Math.tan(Math.toRadians(lSunSet))));
        //NOTE: RA potentially needs to be adjusted into the range [0,360) by adding/subtracting 360
/*
        if(raSunRise < 0) {
            while (!(0 <= raSunRise && raSunRise < 360))
                raSunRise += 360;
        }
        else if(raSunRise >= 360) {
            while (!(0 <= raSunRise && raSunRise < 360))
                raSunRise -= 360;
        }

        if(raSunSet < 0) {
            while (!(0 <= raSunSet && raSunSet < 360))
                raSunSet += 360;
        }
        else if(raSunSet >= 360) {
            while (!(0 <= raSunSet && raSunSet < 360))
                raSunSet -= 360;
        }
*/
        //5b. right ascension value needs to be in the same quadrant as L
        Lquadrant  = (Math.floor(lSunRise  / 90.0)) * 90.0;
        RAquadrant = (Math.floor(raSunRise / 90.0)) * 90.0;
        raSunRise = raSunRise + (Lquadrant - RAquadrant);

        Lquadrant  = (Math.floor(lSunSet  / 90.0)) * 90.0;
        RAquadrant = (Math.floor(raSunSet / 90.0)) * 90.0;
        raSunSet = raSunSet + (Lquadrant - RAquadrant);

        //5c. right ascension value needs to be converted into hours
        raSunRise = raSunRise / 15.0;
        raSunSet  = raSunSet  / 15.0;

        //6. calculate the Sun's declination
        sinDecSunRise = 0.39782 * Math.sin(Math.toRadians(lSunRise));
        cosDecSunRise = Math.cos(Math.asin(sinDecSunRise));

        sinDecSunSet = 0.39782 * Math.sin(Math.toRadians(lSunSet));
        cosDecSunSet = Math.cos(Math.asin(sinDecSunSet));


        //7a. calculate the Sun's local hour angle

        cosHSunRise = (Math.cos(Math.toRadians(zenith)) - (sinDecSunRise * Math.sin(Math.toRadians(latitude)))) / (cosDecSunRise * Math.cos(Math.toRadians(latitude)));

        cosHSunSet  = (Math.cos(Math.toRadians(zenith)) - (sinDecSunSet  * Math.sin(Math.toRadians(latitude)))) / (cosDecSunSet  * Math.cos(Math.toRadians(latitude)));
        /*
        if (cosH >  1)
            the sun never rises on this location (on the specified date)
        if (cosH < -1)
            the sun never sets on this location (on the specified date)
        */


        //7b. finish calculating H and convert into hours

        //if rising time is desired:
        HSunRise = 360.0 - Math.toDegrees(Math.acos(cosHSunRise));
        //if setting time is desired:
        HSunSet = Math.toDegrees(Math.acos(cosHSunSet));

        HSunRise = HSunRise / 15.0;

        HSunSet = HSunSet / 15.0;

        //8. calculate local mean time of rising/setting
        double TSunRise,      TSunSet,
               UTSunRise,     UTSunSet,
               localTSunRise, localTSunSet;


        TSunRise = HSunRise + raSunRise - (0.06571 * tSunRise) - 6.622;

        TSunSet  = HSunSet  + raSunSet  - (0.06571 * tSunSet)  - 6.622;


        //9. adjust back to UTC

        UTSunRise = TSunRise - lngHour;

        UTSunSet = TSunSet - lngHour;
        //NOTE: UT potentially needs to be adjusted into the range [0,24) by adding/subtracting 24
        if(UTSunRise < 0) {
            while (!(0 <= UTSunRise && UTSunRise < 24))
                UTSunRise += 24;
        }
        else if(UTSunRise >= 24) {
            while (!(0 <= UTSunRise && UTSunRise < 24))
                UTSunRise -= 24;
        }

        if(UTSunSet < 0) {
            while (!(0 <= UTSunSet && UTSunSet < 24))
                UTSunSet += 24;
        }
        else if(UTSunSet >= 24) {
            while (!(0 <= UTSunSet && UTSunSet < 24))
                UTSunSet -= 24;
        }




        //10. convert UT value to local time zone of latitude/longitude

        localTSunRise = UTSunRise + localOffset;

        localTSunSet  = UTSunSet  + localOffset;


        if(localTSunRise < 0) {
            while (!(0 <= localTSunRise && localTSunRise < 24))
                localTSunRise += 24;
        }
        else if(localTSunRise >= 24) {
            while (!(0 <= localTSunRise && localTSunRise < 24))
                localTSunRise -= 24;
        }

        if(localTSunSet < 0) {
            while (!(0 <= localTSunSet && localTSunSet < 24))
                localTSunSet += 24;
        }
        else if(localTSunSet >= 24) {
            while (!(0 <= localTSunSet && localTSunSet < 24))
                localTSunSet -= 24;
        }


        //System.out.println(localTSunRise + "    " + localTSunSet);

        this.sunRise = Time.valueOfHour(localTSunRise);
        this.sunSet  = Time.valueOfHour(localTSunSet);


    }

    public String toStrSunRise() {
        if(sunRise == null)
            return "Unknown";
        return sunRise.toString();
    }

    public String toStrSunSet() {
        if(sunSet == null)
            return "Unknown";
        return sunSet.toString();
    }

    public String toStrSunRiseHHMM() {
        if(sunRise == null)
            return "Unknown";
        return sunRise.toStringFormatHHMM();
    }

    public String toStrSunSetHHMM() {
        if(sunSet == null)
            return "Unknown";
        return sunSet.toStringFormatHHMM();
    }

    public int getSunRiseHour() {
        return sunRise.getHours();
    }

    public int getSunRiseMinutes() {
        return sunRise.getMinutes();
    }

    public int getSunRiseSeconds() {
        return sunRise.getSeconds();
    }

    public int getSunSetHour() {
        return sunSet.getHours();
    }

    public int getSunSetMinutes() {
        return sunSet.getMinutes();
    }

    public int getSunSetSeconds() {
        return sunSet.getSeconds();
    }

    public static class Zenith {
                                                // degrees
        public static final double offical      = 90.8333333333;
        public static final double civil        = 96;
        public static final double nautical     = 102;
        public static final double astronomical = 108;

    }
}
/*
http://williams.best.vwh.net/sunrise_sunset_example.htm
Sunrise/Sunset Algorithm Example

        Source:
        Almanac for Computers, 1990
        published by Nautical Almanac Office
        United States Naval Observatory
        Washington, DC 20392

        Inputs:
        day, month, year:      date of sunrise/sunset
        latitude, longitude:   location for sunrise/sunset
        zenith:                Sun's zenith for sunrise/sunset
        offical      = 90 degrees 50'
        civil        = 96 degrees
        nautical     = 102 degrees
        astronomical = 108 degrees

        NOTE: longitude is positive for East and negative for West

        Worked example (from book):
        June 25, 1990:	25, 6, 1990
        Wayne, NJ:      40.9, -74.3
        Office zenith:  90 50' cos(zenith) = -0.01454


        1. first calculate the day of the year

        N1 = floor(275 * month / 9)
        N2 = floor((month + 9) / 12)
        N3 = (1 + floor((year - 4 * floor(year / 4) + 2) / 3))
        N = N1 - (N2 * N3) + day - 30

        Example:
        N1 = 183
        N2 = 1
        N3 = 1 + floor((1990 - 4 * 497 + 2) / 3)
        = 1 + floor((1990 - 1988 + 2) / 3)
        = 1 + floor((1990 - 1988 + 2) / 3)
        = 1 + floor(4 / 3)
        = 2
        N = 183 - 2 + 25 - 30 = 176

        2. convert the longitude to hour value and calculate an approximate time

        lngHour = longitude / 15

        if rising time is desired:
        t = N + ((6 - lngHour) / 24)
        if setting time is desired:
        t = N + ((18 - lngHour) / 24)

        Example:
        lngHour = -74.3 / 15 = -4.953
        t = 176 + ((6 - -4.953) / 24)
        = 176.456

        3. calculate the Sun's mean anomaly

        M = (0.9856 * t) - 3.289

        Example:
        M = (0.9856 * 176.456) - 3.289
        = 170.626

        4. calculate the Sun's true longitude
        [Note throughout the arguments of the trig functions
        (sin, tan) are in degrees. It will likely be necessary to
        convert to radians. eg sin(170.626 deg) =sin(170.626*pi/180
        radians)=0.16287]

        L = M + (1.916 * sin(M)) + (0.020 * sin(2 * M)) + 282.634
        NOTE: L potentially needs to be adjusted into the range [0,360) by adding/subtracting 360

        Example:
        L = 170.626 + (1.916 * sin(170.626)) + (0.020 * sin(2 * 170.626)) + 282.634
        = 170.626 + (1.916 * 0.16287) + (0.020 * -0.32141) + 282.634
        = 170.626 + 0.31206 + -0.0064282 + 282.634
        = 453.566 - 360
        = 93.566

        5a. calculate the Sun's right ascension

        RA = atan(0.91764 * tan(L))
        NOTE: RA potentially needs to be adjusted into the range [0,360) by adding/subtracting 360

        Example:
        RA = atan(0.91764 * -16.046)
        = atan(0.91764 * -16.046)
        = atan(-14.722)
        = -86.11412

        5b. right ascension value needs to be in the same quadrant as L

        Lquadrant  = (floor( L/90)) * 90
        RAquadrant = (floor(RA/90)) * 90
        RA = RA + (Lquadrant - RAquadrant)

        Example:
        Lquadrant  = (floor(93.566/90)) * 90
        = 90
        RAquadrant = (floor(-86.11412/90)) * 90
        = -90
        RA = -86.11412 + (90 - -90)
        = -86.11412 + 180
        = 93.886

        5c. right ascension value needs to be converted into hours

        RA = RA / 15

        Example:
        RA = 93.886 / 15
        = 6.259

        6. calculate the Sun's declination

        sinDec = 0.39782 * sin(L)
        cosDec = cos(asin(sinDec))

        Example:
        sinDec = 0.39782 * sin(93.566)
        = 0.39782 * 0.99806
        = 0.39705
        cosDec = cos(asin(0.39705))
        = cos(asin(0.39705))
        = cos(23.394)
        = 0.91780

        7a. calculate the Sun's local hour angle

        cosH = (cos(zenith) - (sinDec * sin(latitude))) / (cosDec * cos(latitude))

        if (cosH >  1)
        the sun never rises on this location (on the specified date)
        if (cosH < -1)
        the sun never sets on this location (on the specified date)

        Example:
        cosH = (-0.01454 - (0.39705 * sin(40.9))) / (0.91780 * cos(40.9))
        = (-0.01454 - (0.39705 * 0.65474)) / (0.91780 * 0.75585)
        = (-0.01454 - 0.25996) / 0.69372
        = -0.2745 / 0.69372
        = -0.39570

        7b. finish calculating H and convert into hours

        if if rising time is desired:
        H = 360 - acos(cosH)
        if setting time is desired:
        H = acos(cosH)

        H = H / 15

        Example:
        H = 360 - acos(-0.39570)
        = 360 - 113.310   [ note result of acos converted to degrees]
        = 246.690
        H = 246.690 / 15
        = 16.446

        8. calculate local mean time of rising/setting

        T = H + RA - (0.06571 * t) - 6.622

        Example:
        T = 16.446 + 6.259 - (0.06571 * 176.456) - 6.622
        = 16.446 + 6.259 - 11.595 - 6.622
        = 4.488

        9. adjust back to UTC

        UT = T - lngHour
        NOTE: UT potentially needs to be adjusted into the range [0,24) by adding/subtracting 24

        Example:
        UT = 4.488 - -4.953
        = 9.441
        = 9h 26m

        10. convert UT value to local time zone of latitude/longitude

        localT = UT + localOffset

        Example:
        localT = 9h 26m + -4
        = 5h 26m
        = 5:26 am EDT
*/