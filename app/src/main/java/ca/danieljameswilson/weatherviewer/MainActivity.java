package ca.danieljameswilson.weatherviewer;

import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.support.v4.view.AsyncLayoutInflater;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements OnWeatherRequestCompleted{
    private ImageButton searchButton;
    private String city;
    public final static String LOG_KEY = "WeatherLog";
    public final static String WEATHER_KEY = "weatherDetails";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchButton = (ImageButton) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(onSearchButton);
    }

    public View.OnClickListener onSearchButton = new View.OnClickListener(){

        @Override
            public void onClick(View v) {

            EditText citySearch = (EditText) findViewById(R.id.searchText);
            city = citySearch.getText().toString();
            WeatherRequest request = new WeatherRequest(MainActivity.this);
            request.execute(city);

        }
    };

    @Override
    public void onTaskCompleted(WeatherDetails weather) {
        Bundle args = new Bundle();
        args.putParcelable(WEATHER_KEY, weather);
        WeatherFragment f = new WeatherFragment();
        f.setArguments(args);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.weatherFragment,f).commit();
    }

    public static class WeatherRequest extends AsyncTask<String, Void, WeatherDetails> {

        private String city;
        private OnWeatherRequestCompleted listener;

        public WeatherRequest (OnWeatherRequestCompleted listener){
            this.listener = listener;
        }

        @Override
        protected WeatherDetails doInBackground(String... params) {
            city = params[0];

            YahooApiManager apiManager = new YahooApiManager(city);
            WeatherDetails details = apiManager.getWeather();
            return details;
        }

        @Override
        protected void onPostExecute(WeatherDetails weatherData){
           super.onPostExecute(weatherData);

            listener.onTaskCompleted(weatherData);
        }
    }
}
