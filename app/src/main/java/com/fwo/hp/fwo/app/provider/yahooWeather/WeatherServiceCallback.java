package com.fwo.hp.fwo.app.provider.yahooWeather;


import com.fwo.hp.fwo.app.model.Channel;

/**
 * Created by ana.j.saragossa on 20/04/2015.
 */
public interface WeatherServiceCallback {
    void serviceSuccess(Channel channel);
    void serviceFailure(Exception exception);
}
