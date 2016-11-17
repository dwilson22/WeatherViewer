package ca.danieljameswilson.weatherviewer;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class MainActivity extends AppCompatActivity {
private ImageButton searchButton;
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
            WeatherFragment f = new WeatherFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.weatherFragment,f).commit();

        }
    };
}
