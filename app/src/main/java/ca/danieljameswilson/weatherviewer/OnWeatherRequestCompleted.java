package ca.danieljameswilson.weatherviewer;

/**
 * Created by Daniel on 2016-11-16.
 */

public interface OnWeatherRequestCompleted {
    void onTaskCompleted(WeatherDetails weather);
}
