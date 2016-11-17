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
    public final static String CITY_KEY = "city";
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
    public void onTaskCompleted() {
        Bundle args = new Bundle();
        args.putString(CITY_KEY,city);
        WeatherFragment f = new WeatherFragment();
        f.setArguments(args);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.weatherFragment,f).commit();
    }

    public static class WeatherRequest extends AsyncTask<String, Void, JSONObject> {

        private OnWeatherRequestCompleted listener;

        public WeatherRequest (OnWeatherRequestCompleted listener){
            this.listener = listener;
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject weatherData){
           super.onPostExecute(weatherData);

            listener.onTaskCompleted();
        }
    }
}
