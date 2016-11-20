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

    public WeatherDetails getWeather() throws ApiException{

        JSONObject json = makeRequest();

        WeatherDetails details = parseJSON(json);

        return details;
    }

    private JSONObject makeRequest() throws ApiException{

        HttpURLConnection urlConnection;
        URL url;
        JSONObject json;
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

            json =(JSONObject) new JSONTokener(response.toString()).nextValue();


        }catch(IOException|JSONException e){
            Log.e(MainActivity.LOG_KEY,"ERROR NULL RETURN: "+e.getClass().getName()+ " : "+ e.getMessage());
            throw new ApiException("Unable to process Request");
        }

        return json;
    }

    private WeatherDetails parseJSON(JSONObject json) throws ApiException{
        if(json == null){
            Log.d(MainActivity.LOG_KEY,"json: json object is null");
            return null;
        }
        WeatherDetails details;
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
            int tmrHigh = forecast.getJSONObject(1).getInt("high");
            int tmrLow = forecast.getJSONObject(1).getInt("low");

            details = new WeatherDetails(currentTemp, high, low, desc, city,tmrHigh,tmrLow);

        }catch(JSONException e) {
            Log.e(MainActivity.LOG_KEY, e.getClass().getName()+ " : "+ e.getMessage());
            throw new ApiException("Unable to parse Request");

        }
        return details;
    }
}
