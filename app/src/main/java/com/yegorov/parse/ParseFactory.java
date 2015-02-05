package com.yegorov.parse;

import java.util.HashMap;

/**
 * The class that contains a set of objects to parse the weather forecast from provider
 * Singleton with Lazy initialisation
 * @see com.yegorov.parse.WeatherParsable
 */
public class ParseFactory  {
    public static ParseFactory parseFactory = null;

    private HashMap<ForecastProvider, WeatherParsable> objectParse;

    private ParseFactory() {
        objectParse = new HashMap<ForecastProvider, WeatherParsable>(ForecastProvider.values().length);
        objectParse.put(ForecastProvider.InmartWeather, null);
        objectParse.put(ForecastProvider.Gismeteo, null);
        objectParse.put(ForecastProvider.Sinoptik, null);
        objectParse.put(ForecastProvider.YandexWeather, null);
    }

    public WeatherParsable getParserInstance(ForecastProvider fp) {
        if(objectParse.get(fp) == null) {
            switch (fp) {
                case InmartWeather:
                    objectParse.put(fp, new InmartWeatherParse());
                    break;
                case Gismeteo:
                    objectParse.put(fp, new GismeteoWeatherParse());
                    break;
                case Sinoptik:
                    objectParse.put(fp, new SinoptikWeatherParse());
                    break;
                case YandexWeather:
                    objectParse.put(fp, new YandexWeatherParse());
                    break;
            }
        }
        return objectParse.get(fp);
    }
    public static ParseFactory getParseFactory() {
        if(parseFactory == null)
            parseFactory = new ParseFactory();
        return parseFactory;
    }
}
