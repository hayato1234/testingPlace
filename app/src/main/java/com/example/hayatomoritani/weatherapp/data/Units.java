package com.example.hayatomoritani.weatherapp.data;

import org.json.JSONObject;

/**
 * Created by hayatomoritani on 6/30/16.
 */
public class Units implements JSONPopulater {

    private String temperature;

    public String getTemperature() {
        return temperature;
    }

    @Override
    public void populate(JSONObject data) {
        temperature = data.optString("temperature");
    }
}
