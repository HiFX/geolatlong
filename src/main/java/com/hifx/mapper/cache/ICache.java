package com.hifx.mapper.cache;

import com.hifx.mapper.MultiFunction;
import com.hifx.mapper.data.Location;

import java.io.IOException;



public interface ICache {
    Location get(double latitude, double longitude, MultiFunction<Double, Double, Location> load) throws IOException;
}
