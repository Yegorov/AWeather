package com.yegorov.modelweather;

import com.yegorov.util.CalendarUtil;
import com.yegorov.util.NoValidDateException;

/**
 * Moon properties
 *
 * @author yegorov0725@yandex.ru
 * @version 1.0
 * @see com.yegorov.modelweather.DataDay
 */
public class Moon {
    private MoonPhase moonPhase;
    private Zodiac moonZodiac;
    private double moonAge;
    private double moonDistanceInEarthRadii;
    private double moonEclipticLatitude;
    private double moonEclipticLongitude;

    public Moon() {
        this.moonPhase = null;
        this.moonZodiac = null;
        this.moonAge = 0.0;
        this.moonDistanceInEarthRadii = 0.0;
        this.moonEclipticLatitude = 0.0;
        this.moonEclipticLongitude = 0.0;
    }

    // calculate Moon Parameters
    public void init(int day, int month, int year) throws NoValidDateException {
        moonPosition(year, month, day); // changes class properties (instead of setter)
    }

    public void setParameters(MoonBuilder moonBuilder) {
        this.moonPhase = moonBuilder.moonPhase;
        this.moonZodiac = moonBuilder.moonZodiac;
        this.moonAge = moonBuilder.moonAge;
        this.moonDistanceInEarthRadii = moonBuilder.moonDistanceInEarthRadii;
        this.moonEclipticLatitude = moonBuilder.moonEclipticLatitude;
        this.moonEclipticLongitude = moonBuilder.moonEclipticLongitude;
    }

    public static class MoonBuilder {
        private MoonPhase moonPhase;
        private Zodiac moonZodiac;
        private double moonAge;
        private double moonDistanceInEarthRadii;
        private double moonEclipticLatitude;
        private double moonEclipticLongitude;

        public MoonBuilder(MoonPhase moonPhase, Zodiac moonZodiac, double moonAge) {
            this.moonPhase = moonPhase;
            this.moonZodiac = moonZodiac;
            this.moonAge = moonAge;
        }

        public MoonBuilder setMoonDistanceInEarthRadii(double moonDistanceInEarthRadii) {
            this.moonDistanceInEarthRadii = moonDistanceInEarthRadii;
            return this;
        }

        public MoonBuilder setMoonEclipticLatitude(double moonEclipticLatitude) {
            this.moonEclipticLatitude = moonEclipticLatitude;
            return this;
        }

        public MoonBuilder setMoonEclipticLongitude(double moonEclipticLongitude) {
            this.moonEclipticLongitude = moonEclipticLongitude;
            return this;
        }

        public MoonBuilder build() {
            return this;
        }
    }

    public MoonPhase getMoonPhase() {
        return moonPhase;
    }

    public Zodiac getMoonZodiac() {
        return moonZodiac;
    }

    public double getMoonAge() {
        return moonAge;
    }

    public double getMoonDistanceInEarthRadii() {
        return moonDistanceInEarthRadii;
    }

    public double getMoonEclipticLatitude() {
        return moonEclipticLatitude;
    }

    public double getMoonEclipticLongitude() {
        return moonEclipticLongitude;
    }

    private void moonPosition(int year, int month, int day) throws NoValidDateException {

        double ag;                              // Moon's age
        double di;                              // Moon's distance in earth radii
        double la;                              // Moon's ecliptic latitude
        double lo;                              // Moon's ecliptic longitude

        int yy, mm, k1, k2, k3, jd;
        double ip, dp, np, rp;

        if (!CalendarUtil.isDayOfMonth(year, month, day)) {
            // Error
            //System.out.println("Error");
            //return;
            throw new NoValidDateException("Date is no valid: " + day + "/" + month + "/" + year);
        }
        // calculate the Julian date at 12h UT
        yy = (int) (year - Math.floor((12 - month) / 10.0 ));
        mm = month + 9;
        if (mm >= 12)
            mm = mm - 12;

        k1 = (int) Math.floor(365.25 * (yy + 4712.0));
        k2 = (int) Math.floor(30.6 * mm + 0.5 );
        k3 = (int) Math.floor(Math.floor((yy / 100.0) + 49.0) * 0.75) - 38;

        jd = k1 + k2 + day + 59;            // for dates in Julian calendar
        if(jd > 2299160)
            jd = jd - k3;                   // for Gregorian calendar

        // calculate moon's age in days
        ip = normalize( ( jd - 2451550.1 ) / 29.530588853 );
        ag = ip * 29.53;

        if      (ag <  1.84566) this.moonPhase = MoonPhase.NewMoon;           //phase = "NEW";
        else if (ag <  5.53699) this.moonPhase = MoonPhase.WaningCrescent;    //phase = "Waxing crescent";
        else if (ag <  9.22831) this.moonPhase = MoonPhase.FirstQuarter;      //phase = "First quarter";
        else if (ag < 12.91963) this.moonPhase = MoonPhase.WaxingGibbous;     //phase = "Waxing gibbous";
        else if (ag < 16.61096) this.moonPhase = MoonPhase.FullMoon;          //phase = "FULL";
        else if (ag < 20.30228) this.moonPhase = MoonPhase.WaningGibbous;     //phase = "Waning gibbous";
        else if (ag < 23.99361) this.moonPhase = MoonPhase.ThirdQuarter;      //phase = "Last quarter";
        else if (ag < 27.68493) this.moonPhase = MoonPhase.WaningCrescent;    //phase = "Waning crescent";
        else                    this.moonPhase = MoonPhase.NewMoon;           //phase = "NEW";

        ip = ip * 2 * Math.PI;  // Convert phase to radians

        // calculate moon's distance
        dp = 2 * Math.PI * normalize((jd - 2451562.2) / 27.55454988);
        di = 60.4 - 3.3 * Math.cos(dp) - 0.6 * Math.cos(2 * ip - dp) - 0.5 * Math.cos(2 * ip);

        // calculate moon's ecliptic latitude
        np = 2 * Math.PI * normalize((jd - 2451565.2) / 27.212220817);
        la = 5.1 * Math.sin(np);

        // calculate moon's ecliptic longitude
        rp = normalize((jd - 2451555.8) / 27.321582241);
        lo = 360 * rp + 6.3 * Math.sin(dp) + 1.3 * Math.sin(2 * ip - dp) + 0.7 * Math.sin(2 * ip);

        if      (lo <  33.18) this.moonZodiac = Zodiac.Pisces;        // zodiac = "Pisces";
        else if (lo <  51.16) this.moonZodiac = Zodiac.Aries;         // zodiac = "Aries";
        else if (lo <  93.44) this.moonZodiac = Zodiac.Taurus;        // zodiac = "Taurus";
        else if (lo < 119.48) this.moonZodiac = Zodiac.Gemini;        // zodiac = "Gemini";
        else if (lo < 135.30) this.moonZodiac = Zodiac.Cancer;        // zodiac = "Cancer";
        else if (lo < 173.34) this.moonZodiac = Zodiac.Leo;           // zodiac = "Leo";
        else if (lo < 224.17) this.moonZodiac = Zodiac.Virgo;         // zodiac = "Virgo";
        else if (lo < 242.57) this.moonZodiac = Zodiac.Libra;         // zodiac = "Libra";
        else if (lo < 271.26) this.moonZodiac = Zodiac.Scorpius;      // zodiac = "Scorpio";
        else if (lo < 302.49) this.moonZodiac = Zodiac.Sagittarius;   // zodiac = "Sagittarius";
        else if (lo < 311.72) this.moonZodiac = Zodiac.Capricornus;   // zodiac = "Capricorn";
        else if (lo < 348.58) this.moonZodiac = Zodiac.Aquarius;      // zodiac = "Aquarius";
        else                  this.moonZodiac = Zodiac.Pisces;        // zodiac = "Pisces";


        this.moonAge = round2(ag);
        this.moonDistanceInEarthRadii = round2(di);
        this.moonEclipticLatitude = round2(la);
        this.moonEclipticLongitude = round2(lo);

        // display results
        /*
        System.out.println("phase         = " + phase);
        System.out.println("age           = " + round2( ag ) + " days");
        System.out.println("distance      = " + round2( di ) + " earth radii");
        System.out.println("ecliptic");
        System.out.println("latitude      = " + round2( la ) + '°');
        System.out.println("longitude     = " + round2( lo ) + '°');
        System.out.println("constellation = " + zodiac);
        */

    }

    // round to 2 decimal places
    private double round2(double x) {
        return Math.round(100 * x) / 100.0;
    }

    //normalize values to range 0...1
    private double normalize(double v) {
        v = v - Math.floor(v); //v = v - (int)v;
        if(v < 0) {
            v = v + 1.0;
        }
        return v;
    }
}

/*
Original code write on Zeno (programming language).
http://web.archive.org/web/20080504163207/http://home.att.net/~srschmitt/lunarphasecalc.html

Lunar Phase Computation
by Stephen R. Schmitt
Introduction

During a lunar month (about 29.5) days,
the Moon's appearance changes through eight well-known phases that comprise a lunation.
These phases of the Moon are:

    New Moon
    Waxing Crescent
    First Quarter
    Waxing Gibbous
    Full Moon
    Waning Gibbous
    Last Quarter
    Waning Crescent

New Moon, First Quarter, Full Moon, and Last Quarter are the primary phases.
The crescent and gibbous phases are intermediate phases.
First and Last Quarters occur when the Sun and Moon are 90° degrees apart.
The First Quarter and Last Quarter phases are named this way because they occur
when the Moon is at one- and three-quarters of a complete cycle.
The phases New Moon, First Quarter, Full Moon,
and Last Quarter occur when the ecliptic longitude of the Moon differs
from that of the Sun by 0°, 90°, 180°, and 270°.

The time in days counted from the time of New Moon is called the Moon's age.

The ecliptic longitude is measured from the vernal equinox
along the ecliptic in the direction of the Sun's apparent motion through the stars.

The ecliptic latitude is positive north of the ecliptic and negative if south.
Algorithm

This program helps anyone who needs to know the Moon's phase, age, distance,
and position along the ecliptic on any date within several thousand years in the past or future.
The age of the moon in days as well as its visual phase are given.
The Moon's ecliptic longitude is calculated as well as the corresponding zodiac constellation.

The Moon's calculated position is based on the Julian Day number corresponding to the calendar date.
The date is checked for valid day of the month.


const PI : real := 3.1415926535897932385

program

    var year, month, day : int
    var tm : real := localtime

    year  := dateyear( tm )
    month := datemonth( tm )
    day   := dateday( tm )

    put "Moon on ", month, '/', day, '/', year
    moon_posit( year, month, day )

end program

%  compute moon position and phase
procedure moon_posit( Y, M, D : int )

    var AG : real                           % Moon's age
    var DI : real                           % Moon's distance in earth radii
    var LA : real                           % Moon's ecliptic latitude
    var LO : real                           % Moon's ecliptic longitude
    var Phase : string
    var Zodiac : string

    var YY, MM, K1, K2, K3, JD : int
    var IP, DP, NP, RP : real

    if not isdayofmonth( Y, M, D ) then
        put "invalid date"
        return
    end if

    % calculate the Julian date at 12h UT
    YY := Y - floor( ( 12 - M ) / 10 )
    MM := M + 9
    if (MM >= 12) then
        MM := MM - 12
    end if

    K1 := floor( 365.25 * ( YY + 4712 ) )
    K2 := floor( 30.6 * MM + 0.5 )
    K3 := floor( floor( ( YY / 100 ) + 49 ) * 0.75 ) - 38

    JD := K1 + K2 + D + 59                  % for dates in Julian calendar
    if (JD > 2299160) then
        JD := JD - K3                       % for Gregorian calendar
    end if

    % calculate moon's age in days
    IP := normalize( ( JD - 2451550.1 ) / 29.530588853 )
    AG := IP*29.53

    if    AG <  1.84566 then Phase := "NEW"
    elsif AG <  5.53699 then Phase := "Waxing crescent"
    elsif AG <  9.22831 then Phase := "First quarter"
    elsif AG < 12.91963 then Phase := "Waxing gibbous"
    elsif AG < 16.61096 then Phase := "FULL"
    elsif AG < 20.30228 then Phase := "Waning gibbous"
    elsif AG < 23.99361 then Phase := "Last quarter"
    elsif AG < 27.68493 then Phase := "Waning crescent"
    else                     Phase := "NEW"
    end if

    IP := IP*2*PI                           % Convert phase to radians

    % calculate moon's distance
    DP := 2*PI*normalize( ( JD - 2451562.2 ) / 27.55454988 )
    DI := 60.4 - 3.3*cos( DP ) - 0.6*cos( 2*IP - DP ) - 0.5*cos( 2*IP )

    % calculate moon's ecliptic latitude
    NP := 2*PI*normalize( ( JD - 2451565.2 ) / 27.212220817 )
    LA := 5.1*sin( NP )

    % calculate moon's ecliptic longitude
    RP := normalize( ( JD - 2451555.8 ) / 27.321582241 )
    LO := 360*RP + 6.3*sin( DP ) + 1.3*sin( 2*IP - DP ) + 0.7*sin( 2*IP )

    if    LO <  33.18 then Zodiac := "Pisces"
    elsif LO <  51.16 then Zodiac := "Aries"
    elsif LO <  93.44 then Zodiac := "Taurus"
    elsif LO < 119.48 then Zodiac := "Gemini"
    elsif LO < 135.30 then Zodiac := "Cancer"
    elsif LO < 173.34 then Zodiac := "Leo"
    elsif LO < 224.17 then Zodiac := "Virgo"
    elsif LO < 242.57 then Zodiac := "Libra"
    elsif LO < 271.26 then Zodiac := "Scorpio"
    elsif LO < 302.49 then Zodiac := "Sagittarius"
    elsif LO < 311.72 then Zodiac := "Capricorn"
    elsif LO < 348.58 then Zodiac := "Aquarius"
    else                   Zodiac := "Pisces"
    end if

    % display results
    put "phase         = ", Phase
    put "age           = ", round2( AG ), " days"
    put "distance      = ", round2( DI ), " earth radii"
    put "ecliptic"
    put " latitude     = ", round2( LA ), '°'
    put " longitude    = ", round2( LO ), '°'
    put "constellation = ", Zodiac

end procedure

% check for valid date
function isdayofmonth( year, month, day : int ) : boolean

    var daysofmonth : int

    if (month < 1) or (12 < month) then
        return false                        % invalid month
    end if

    case month of                           % get days in this month
    value 4,6,9,11:
        daysofmonth := 30                   % Apr, Jun, Sep, Nov
    value 2:
        daysofmonth := 28                   % Feb normal
        if year mod 4 = 0 then
            if not((year mod 100 = 0) and
                   (year mod 400 ~= 0)) then
                daysofmonth := 29           % Feb leap year
            end if
        end if
    value:
        daysofmonth := 31                   % other months
    end case

    return (0 < day) and (day <= daysofmonth)

end function

% round to 2 decimal places
function round2( x : real ) : real
    return ( round( 100*x )/100.0 )
end function

% normalize values to range 0...1
function normalize( v : real ) : real
    v := v - floor( v )
    if v < 0 then
        v := v + 1
    end if
    return v
end function

Sample output

Moon on 3/24/2004
phase         = Waxing crescent
age           = 3.31 days
distance      = 62.87 earth radii
ecliptic
 latitude     = -0.1°
 longitude    = 44.92°
constellation = Aries

*/