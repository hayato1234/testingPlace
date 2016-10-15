package com.example.hayatomoritani.weatherapp.data;

import org.json.JSONObject;

/**
 * Created by hayatomoritani on 6/30/16.
 */
public class Item implements JSONPopulater {

    private Condition condition;

    public Condition getCondition() {
        return condition;
    }

    @Override
    public void populate(JSONObject data) {

        condition = new Condition();
        condition.populate(data.optJSONObject("condition"));

    }
}
