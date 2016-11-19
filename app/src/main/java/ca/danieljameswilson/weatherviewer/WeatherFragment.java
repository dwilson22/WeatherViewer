package ca.danieljameswilson.weatherviewer;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment {


    public WeatherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        WeatherDetails details = getArguments().getParcelable(MainActivity.WEATHER_KEY);
        TextView cityTextView = (TextView) view.findViewById(R.id.cityTextView);
        TextView currentTempView = (TextView) view.findViewById(R.id.currantTemp);
        TextView highTempView = (TextView) view.findViewById(R.id.highTemp);
        TextView lowTempView = (TextView) view.findViewById(R.id.lowTemp);
        TextView descTempView = (TextView) view.findViewById(R.id.weatherDesc);
        if(details != null) {
            cityTextView.setText(details.getCity());
            currentTempView.setText(String.format(getResources().getString(R.string.weatherTemp),details.getCurrantTemp()));
            highTempView.setText(String.format(getResources().getString(R.string.weatherTemp),details.getHigh()));
            lowTempView.setText(String.format(getResources().getString(R.string.weatherTemp),details.getLow()));
            descTempView.setText(details.getDescription());
        }else{
            Log.d(MainActivity.LOG_KEY, "Weatherdetails returned null");
        }
        return view;
    }

}
