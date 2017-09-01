package com.joelzhu.miweather;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        JZMiWeather miWeather = (JZMiWeather) findViewById(R.id.miweather);
        Adapter adapter = new Adapter();
        miWeather.setAdapter(adapter);
    }

    private class Adapter extends WeatherAdapter {
        private List<WeatherModel> data;

        private Adapter() {
            data = new ArrayList<>();
            data.add(new WeatherModel("01时", 20, WeatherModel.WeatherType.Sunny));
            data.add(new WeatherModel("02时", 21, WeatherModel.WeatherType.Cloudy));
            data.add(new WeatherModel("03时", 21, WeatherModel.WeatherType.Cloudy));
            data.add(new WeatherModel("04时", 22, WeatherModel.WeatherType.Cloudy));
            data.add(new WeatherModel("05时", 22, WeatherModel.WeatherType.Cloudy));
            data.add(new WeatherModel("06时", 22, WeatherModel.WeatherType.Cloudy));
            data.add(new WeatherModel("07时", 23, WeatherModel.WeatherType.Cloudy));
            data.add(new WeatherModel("08时", 23, WeatherModel.WeatherType.Sunny));
            data.add(new WeatherModel("09时", 25, WeatherModel.WeatherType.Sunny));
            data.add(new WeatherModel("10时", 25, WeatherModel.WeatherType.Sunny));
            data.add(new WeatherModel("11时", 26, WeatherModel.WeatherType.Sunny));
            data.add(new WeatherModel("12时", 27, WeatherModel.WeatherType.Sunny));
            data.add(new WeatherModel("13时", 27, WeatherModel.WeatherType.Sunny));
            data.add(new WeatherModel("14时", 28, WeatherModel.WeatherType.Sunny));
            data.add(new WeatherModel("15时", 27, WeatherModel.WeatherType.Sunny));
            data.add(new WeatherModel("16时", 27, WeatherModel.WeatherType.Sunny));
            data.add(new WeatherModel("17时", 26, WeatherModel.WeatherType.Overcast));
            data.add(new WeatherModel("18时", 26, WeatherModel.WeatherType.Overcast));
            data.add(new WeatherModel("19时", 26, WeatherModel.WeatherType.Overcast));
            data.add(new WeatherModel("20时", 25, WeatherModel.WeatherType.Overcast));
            data.add(new WeatherModel("21时", 23, WeatherModel.WeatherType.Thunderstorm));
            data.add(new WeatherModel("22时", 23, WeatherModel.WeatherType.Thunderstorm));
            data.add(new WeatherModel("23时", 22, WeatherModel.WeatherType.Thunderstorm));
            data.add(new WeatherModel("00时", 22, WeatherModel.WeatherType.Thunderstorm));
            data.add(new WeatherModel("01时", 21, WeatherModel.WeatherType.Thunderstorm));
            data.add(new WeatherModel("02时", 21, WeatherModel.WeatherType.Thunderstorm));
            data.add(new WeatherModel("03时", 21, WeatherModel.WeatherType.Rain));
            data.add(new WeatherModel("04时", 21, WeatherModel.WeatherType.Rain));
            data.add(new WeatherModel("05时", 22, WeatherModel.WeatherType.Rain));
            data.add(new WeatherModel("06时", 22, WeatherModel.WeatherType.Rain));
            data.add(new WeatherModel("07时", 22, WeatherModel.WeatherType.Rain));
            data.add(new WeatherModel("08时", 22, WeatherModel.WeatherType.Rain));
            data.add(new WeatherModel("09时", 22, WeatherModel.WeatherType.Rain));
            data.add(new WeatherModel("10时", 23, WeatherModel.WeatherType.Rain));
            data.add(new WeatherModel("11时", 24, WeatherModel.WeatherType.Rain));
            data.add(new WeatherModel("12时", 24, WeatherModel.WeatherType.Rain));
            data.add(new WeatherModel("13时", 25, WeatherModel.WeatherType.Rain));
            data.add(new WeatherModel("14时", 25, WeatherModel.WeatherType.Rain));
            data.add(new WeatherModel("15时", 25, WeatherModel.WeatherType.Rain));
            data.add(new WeatherModel("16时", 24, WeatherModel.WeatherType.Rain));
            data.add(new WeatherModel("17时", 24, WeatherModel.WeatherType.Rain));
            data.add(new WeatherModel("18时", 24, WeatherModel.WeatherType.Rain));
            data.add(new WeatherModel("19时", 23, WeatherModel.WeatherType.Rain));
            data.add(new WeatherModel("20时", 23, WeatherModel.WeatherType.Rain));
            data.add(new WeatherModel("21时", 23, WeatherModel.WeatherType.Cloudy));
            data.add(new WeatherModel("22时", 22, WeatherModel.WeatherType.Cloudy));
            data.add(new WeatherModel("23时", 22, WeatherModel.WeatherType.Cloudy));
        }

        @Override
        int getCount() {
            return data.size();
        }

        @Override
        List<WeatherModel> getData() {
            return data;
        }
    }
}