package com.joelzhu.miweather;

class WeatherModel {
    // 当前时间
    private String time;

    // 当前温度
    private int temperature;

    // 当前天气
    private WeatherType weatherType;

    public WeatherModel(String time, int temperature, WeatherType weatherType) {
        this.time = time;
        this.temperature = temperature;
        this.weatherType = weatherType;
    }

    public String getTime() {
        return time;
    }

    public int getTemperature() {
        return temperature;
    }

    public WeatherType getWeatherType() {
        return weatherType;
    }

    /**
     * 天气类型
     */
    enum WeatherType {
        // 晴
        Sunny,
        // 多云
        Cloudy,
        // 阴天
        Overcast,
        // 雨
        Rain,
        // 雷阵雨
        Thunderstorm,
        // 雪
        Snow
    }
}