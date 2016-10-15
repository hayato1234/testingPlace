package com.example.hayatomoritani.weatherapp.service;

import com.example.hayatomoritani.weatherapp.data.Channel;

/**
 * Created by hayatomoritani on 6/30/16.
 */
public interface WeatherServiceCallback {
    void serviceSuccess(Channel channel);
    void serviceFailure(Exception e);
}
