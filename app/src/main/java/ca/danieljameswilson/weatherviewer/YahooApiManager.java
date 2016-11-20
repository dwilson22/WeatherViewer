package ca.danieljameswilson.weatherviewer;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Daniel on 2016-11-20.
 */
public class YahooApiManager {
    private String city;

    public YahooApiManager(String c){
        city = c;
    }

    public WeatherDetails getWeather(){

        JSONObject json = makeRequest();

        WeatherDetails details = parseJSON(json);

        return details;
    }

    private JSONObject makeRequest(){

        HttpURLConnection urlConnection;
        URL url;
        String yql = String.format("select * from weather.forecast where u=\"c\" and woeid in (select woeid from geo.places(1) where text=\"%s\")", city);


        try {
            yql = URLEncoder.encode(yql, "utf-8");
            StringBuilder urlString = new StringBuilder();
            urlString.append("https://query.yahooapis.com/v1/public/yql?");
            urlString.append("q=").append(yql);
            urlString.append("&format=").append("json");
Log.d(MainActivity.LOG_KEY,urlString.toString());
            url = new URL(urlString.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputSteam = urlConnection.getInputStream();
            BufferedReader bReader = new BufferedReader(new InputStreamReader(inputSteam));

            String temp;
            StringBuilder response = new StringBuilder();
            while((temp = bReader.readLine())!= null){
                response.append(temp);
            }

            JSONObject json =(JSONObject) new JSONTokener(response.toString()).nextValue();
            return json;

        }catch(IOException|JSONException e){
            Log.e(MainActivity.LOG_KEY,"ERROR NULL RETURN: "+e.getClass().getName());
            return null;
        }
    }

    private WeatherDetails parseJSON(JSONObject json){
        try {
            JSONObject item = json.getJSONObject("query")
                    .getJSONObject("results")
                    .getJSONObject("channel")
                    .getJSONObject("item");
            JSONObject condition = item.getJSONObject("condition");
            int currentTemp = condition.getInt("temp");
            String desc = condition.getString("text");

            JSONArray forecast = item.getJSONArray("forecast");
            int high = forecast.getJSONObject(0).getInt("high");
            int low = forecast.getJSONObject(0).getInt("low");

            WeatherDetails details = new WeatherDetails(currentTemp, high, low, desc, city);
            return details;
        }catch(JSONException e) {
            Log.e(MainActivity.LOG_KEY, e.getClass().getName());
            return null;
        }
    }
}
