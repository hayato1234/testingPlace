package com.example.hayatomoritani.weatherapp.data;

import org.json.JSONObject;

/**
 * Created by hayatomoritani on 6/30/16.
 */
public class Channel implements JSONPopulater {

    private Item item;
    private Units units;

    public Item getItem() {
        return item;
    }

    public Units getUnits() {
        return units;
    }

    @Override
    public void populate(JSONObject data) {
        units = new Units();
        units.populate(data.optJSONObject("units"));

        item = new Item();
        item.populate(data.optJSONObject("item"));

    }
}
