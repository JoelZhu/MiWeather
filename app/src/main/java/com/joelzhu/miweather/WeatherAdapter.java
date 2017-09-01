package com.joelzhu.miweather;

import java.util.List;

public abstract class WeatherAdapter {
    private int maxTemp = -1, minTemp = -1;

    abstract int getCount();

    abstract List<WeatherModel> getData();

    /**
     * 返回最大最小温度
     *
     * @return 最大最小温度数组([0]最大温度, [1]最小温度)
     */
    public int[] getMaxMinTemperature() {
        if (maxTemp == -1 || minTemp == -1) {
            maxTemp = 0;
            minTemp = getData().get(0).getTemperature();
            for (WeatherModel model : getData()) {
                if (maxTemp < model.getTemperature())
                    maxTemp = model.getTemperature();
                if (minTemp > model.getTemperature())
                    minTemp = model.getTemperature();
            }
        }
        return new int[]{maxTemp, minTemp};
    }
}