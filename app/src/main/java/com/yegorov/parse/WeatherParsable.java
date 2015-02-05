package com.yegorov.parse;


import com.yegorov.modelweather.*;
import com.yegorov.units.PressureUnits;
import com.yegorov.units.SpeedUnits;
import com.yegorov.units.TemperatureUnits;

import java.util.HashMap;

/**
 * Interface parse
 */
public interface WeatherParsable {
    public Weather stringParse(String arg) throws NoValidWeatherData;
}

/*
 * Implementation analysis of weather forecast
 */
class InmartWeatherParse implements WeatherParsable {

    /*
     * url: http://inmart.ua/android_weather.php
     * Example return - 90~736~17.4~1.3~0
     *                  humidity~barPressure~airTemp~windSpeed~windDirection
     */

    private HashMap<Integer, WindDirection> windDirectionInmartWeather;

    public InmartWeatherParse() {
        windDirectionInmartWeather = new HashMap<Integer, WindDirection>(WindDirection.values().length);
        windDirectionInmartWeather.put(0, WindDirection.S);
        windDirectionInmartWeather.put(1, WindDirection.SW);
        windDirectionInmartWeather.put(2, WindDirection.SW);
        windDirectionInmartWeather.put(3, WindDirection.SW);
        windDirectionInmartWeather.put(4, WindDirection.W);
        windDirectionInmartWeather.put(5, WindDirection.NW);
        windDirectionInmartWeather.put(6, WindDirection.NW);
        windDirectionInmartWeather.put(7, WindDirection.NW);
        windDirectionInmartWeather.put(8, WindDirection.N);
        windDirectionInmartWeather.put(9, WindDirection.NE);
        windDirectionInmartWeather.put(10, WindDirection.NE);
        windDirectionInmartWeather.put(11, WindDirection.NE);
        windDirectionInmartWeather.put(12, WindDirection.E);
        windDirectionInmartWeather.put(13, WindDirection.SE);
        windDirectionInmartWeather.put(14, WindDirection.SE);
    }

    @Override
    public Weather stringParse(String arg) throws NoValidWeatherData {
        Weather weather;

        String[] weatherProperties = arg.split("~");

        WindDirection windDirection = WindDirection.Indeterminately;
        int windDirectionKey = Integer.parseInt(weatherProperties[4]);
        if(windDirectionInmartWeather.containsKey(windDirectionKey))
            windDirection = windDirectionInmartWeather.get(windDirectionKey);

        double humidity = 0.0, barPressure = 0.0, airTemp = 0.0, windSpeed = 0.0;

        try {
            humidity = Double.parseDouble(weatherProperties[0]);
            barPressure = Double.parseDouble(weatherProperties[1]);
            airTemp = Double.parseDouble(weatherProperties[2]);
            windSpeed = Double.parseDouble(weatherProperties[3]);

        }catch (NumberFormatException e)
        {
            throw new NoValidWeatherData("No valid weather data: " + e.getMessage().toString());
        }

        weather = new Weather.WeatherBuilder()
                    .humidity(humidity)
                    .barometricPressure(barPressure, PressureUnits.mm_Hg)
                    .airTemperature(airTemp, TemperatureUnits.C)
                    .windSpeed(windSpeed, SpeedUnits.m_s)
                    .windDirection(windDirection)
                    .build();

        return weather;

        /* All propetry
        return new Weather.WeatherBuilder().barometricPressure()
                .airTemperature()
                .cloudiness(Cloudiness.Cloudy)
                .humidity(30)
                .precipitation(Precipitation.Dry)
                .windDirection(WindDirection.S)
                .windSpeed()
                .build();
        */
    }

}

class GismeteoWeatherParse implements WeatherParsable {
    // TODO
    //
    @Override
    public Weather stringParse(String arg) {
        return new Weather.WeatherBuilder()
                .barometricPressure(840, PressureUnits.mm_Hg)
                .airTemperature(20.5, TemperatureUnits.F)
                .cloudiness(Cloudiness.Cloudy)
                .humidity(100)
                .precipitation(Precipitation.Dry)
                .windDirection(WindDirection.S)
                .windSpeed(8.4, SpeedUnits.ft_s)
                .build();
    }

}

class YandexWeatherParse implements WeatherParsable {
    // TODO
    // xml
    // http://mobile.yamobile.yandex.net/yamobile/get?screen_h=1184&cellid=null,null,null,null,-115&model=HTC+-+4.2.2+-+API+17+-+720x1280&gzip=&verupdatemode=1&app_version=3.03&app_platform=android&os_version=4.2.2&wifi=01:80:c2:00:00:03,-55&screen_w=720&clid=1866962&widgettype=3&manufacturer=HTC&uuid=fc269a2e58fa52dceb307a37c07e5249&allparts
    @Override
    public Weather stringParse(String arg) {
        return null;
    }

}

class SinoptikWeatherParse implements WeatherParsable {
    //TODO
    // http://informers.sinoptik.ua/js3.php?title=4&wind=3&cities=303005769&lang=ru
    // Example
    /*
    LoadInformer({
    date: '10.06.14, днем',
    body: [{
        "temp": "+23°",
        "wt": null,
        "ws": "4 \u043c\/\u0441, <\/span>",
        "p": "731 \u043c\u043c",
        "rh": "40%",
        "img": "d300"
    }]
})
     */
    @Override
    public Weather stringParse(String arg) {
        return null;
    }

}
// http://openweathermap.org/city/707753