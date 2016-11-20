package ca.danieljameswilson.weatherviewer;

import android.app.FragmentTransaction;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.view.AsyncLayoutInflater;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements OnWeatherRequestCompleted{
    public final static String LOG_KEY = "WeatherLog";
    public final static String WEATHER_KEY = "weatherDetails";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton searchButton = (ImageButton) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(onSearchButton);
    }

    public View.OnClickListener onSearchButton = new View.OnClickListener(){

        @Override
            public void onClick(View v) {

            EditText citySearch = (EditText) findViewById(R.id.searchText);
            String city = citySearch.getText().toString();
            WeatherRequest request = new WeatherRequest(MainActivity.this, getApplicationContext());
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
        private ApiException e;
        private Context context;

        public WeatherRequest (OnWeatherRequestCompleted listener, Context context){
            this.listener = listener;
            this.context = context;
        }

        @Override
        protected WeatherDetails doInBackground(String... params) {
            city = params[0];
            WeatherDetails details =null;
            try {
                YahooApiManager apiManager = new YahooApiManager(city);
                details = apiManager.getWeather();
            }catch(ApiException e){
                Log.e(MainActivity.LOG_KEY, "Unable to process request");
                this.e =e;
            }
            return details;
        }

        @Override
        protected void onPostExecute(WeatherDetails weatherData){
           super.onPostExecute(weatherData);

            if(this.e !=null){
                CharSequence text = context.getResources().getString(R.string.city_error);
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                return;
            }

            listener.onTaskCompleted(weatherData);
        }
    }
}
