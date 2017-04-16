package com.hifx.mapper.cache;

import com.hifx.mapper.MultiFunction;
import com.hifx.mapper.data.Location;

import java.io.IOException;


public class NoCache implements ICache {
    public Location get(double latitude, double longitude, MultiFunction<Double, Double, Location> load) throws IOException {
        return load.apply(latitude, longitude);
    }
}
