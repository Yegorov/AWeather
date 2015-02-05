package com.yegorov.modelweather;

import com.yegorov.units.PressureUnits;
import com.yegorov.units.SpeedUnits;
import com.yegorov.units.TemperatureUnits;

/**
 * The main characteristics of weather
 * More info: http://www.meteoinfo.ru/forecasts/forcterminology
 * For the construction of the object is implemented Builder
 *
 * @author yegorov0725@yandex.ru
 * @version 1.0
 * @see com.yegorov.modelweather.DataDay
 */
public class Weather {
    /**
     * Air temperature
     * @see com.yegorov.modelweather.Temperature
     */
    private Temperature airTemperature;

    /**
     *  Barometric pressure
     *  @see com.yegorov.modelweather.Pressure
     */
    private Pressure barometricPressure;

    /**
     *  Wind speed
     *  @see com.yegorov.modelweather.Speed
     */
    private Speed windSpeed;

    /**
     *  Wind direction
     *  @see com.yegorov.modelweather.WindDirection
     */
    private WindDirection windDirection;

    /**
     *  Humidity, %
     */
    private double humidity;

    /**
     *  Cloudiness
     *  @see com.yegorov.modelweather.Cloudiness
     */
    private Cloudiness cloudiness;

    /**
     *  Precipitation
     *  @see com.yegorov.modelweather.Precipitation
     */
    private Precipitation precipitation;


    /**
     * Weather Builder (pattern)
     */
    public static class WeatherBuilder {
        private Temperature airTemperature;
        private Pressure barometricPressure;
        private Speed windSpeed;
        private WindDirection windDirection;
        private double humidity;
        private Cloudiness cloudiness;
        private Precipitation precipitation;

        public WeatherBuilder() {
            airTemperature = new Temperature();
            barometricPressure = new Pressure();
            windSpeed = new Speed();
            windDirection = WindDirection.Indeterminately;
            humidity = 0.0;
            cloudiness = Cloudiness.Indeterminately;
            precipitation = Precipitation.Indeterminately;
        }

        public WeatherBuilder airTemperature(double value, TemperatureUnits unit) {
            airTemperature.setTemperature(value, unit);
            return this;
        }

        public WeatherBuilder barometricPressure(double value, PressureUnits unit) {
            barometricPressure.setPressure(value, unit);
            return this;
        }

        public WeatherBuilder windSpeed(double value, SpeedUnits unit) {
            windSpeed.setSpeed(value, unit);
            return this;
        }

        public WeatherBuilder windDirection(WindDirection value) {
            windDirection = value;
            return this;
        }

        public WeatherBuilder humidity(double value) {
            humidity = value;
            return this;
        }

        public WeatherBuilder cloudiness(Cloudiness value) {
            cloudiness = value;
            return this;
        }

        public WeatherBuilder precipitation(Precipitation value) {
            precipitation = value;
            return this;
        }

        public Weather build() {
            return new Weather(this);
        }

    }

    /**
     * Private constructor
     * @param weatherBuilder The object of the weather data obtained from builder
     */
    private Weather(WeatherBuilder weatherBuilder) {
        this.airTemperature = weatherBuilder.airTemperature;
        this.barometricPressure = weatherBuilder.barometricPressure;
        this.windSpeed = weatherBuilder.windSpeed;
        this.windDirection = weatherBuilder.windDirection;
        this.humidity = weatherBuilder.humidity;
        this.cloudiness = weatherBuilder.cloudiness;
        this.precipitation = weatherBuilder.precipitation;
    }

    private Weather(Temperature airTemperature, Pressure barometricPressure,
                   Speed windSpeed, WindDirection windDirection,
                   double humidity, Cloudiness cloudiness, Precipitation precipitation) {
        this.airTemperature = airTemperature;
        this.barometricPressure = barometricPressure;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
        this.humidity = humidity;
        this.cloudiness = cloudiness;
        this.precipitation = precipitation;
    }

    /**
     * @deprecated Implemented for compatibility, use Builder.
     * @return new weather object
     */
    public static Weather newInstance(Temperature airTemperature, Pressure barometricPressure,
                                      Speed windSpeed, WindDirection windDirection,
                                      double humidity, Cloudiness cloudiness, Precipitation precipitation) {
        return new Weather(airTemperature, barometricPressure,
                           windSpeed, windDirection,
                           humidity, cloudiness, precipitation);
    }

    public double getTemperatureValue() {
        return airTemperature.getTemp();
    }

    public TemperatureUnits getTemperatureUnit() {
        return airTemperature.getUnitTemp();
    }

    public void setTemperature(Temperature airTemperature) {
        this.airTemperature = airTemperature;
    }

    public double getBarometricPressureValue() {
        return barometricPressure.getPressure();
    }

    public PressureUnits getBarometricPressureUnit() {
        return barometricPressure.getPressureUnit();
    }

    public void setBarometricPressure(Pressure barometricPressure) {
        this.barometricPressure = barometricPressure;
    }

    public double getWindSpeedValue() {
        return windSpeed.getSpeed();
    }

    public SpeedUnits getWindSpeedUnit() {
        return windSpeed.getSpeedUnit();
    }

    public void setWindSpeed(Speed windSpeed) {
        this.windSpeed = windSpeed;
    }

    public WindDirection getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(WindDirection windDirection) {
        this.windDirection = windDirection;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public Cloudiness getCloudiness() {
        return cloudiness;
    }

    public void setCloudiness(Cloudiness cloudiness) {
        this.cloudiness = cloudiness;
    }

    public Precipitation getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(Precipitation precipitation) {
        this.precipitation = precipitation;
    }


    public String airTempToStr() {
        return airTemperature.toString();
    }

    public String barPressToStr() {
        return barometricPressure.toString();
    }

    public String windSpeedToStr() {
        return windSpeed.toString();
    }

    public String windDirectionToStr() {
        return windDirection.toStr();
    }

    public String humidityToStr() {
        return String.format("%.1f", humidity) + "%";
    }

    public String cloudinessToStr() {
        return cloudiness.toStr();
    }

    public String precipitationToStr() {
        return precipitation.toStr();
    }

    @Override
    public String toString() {
        return  airTempToStr() + ", " + barPressToStr() +
                ",\nHumidity: " + humidityToStr() +
                ",\nWind: " + windSpeedToStr() + ", " + windDirectionToStr() +
                "\n" + precipitationToStr() +
                "\n" + cloudinessToStr();
    }
}
