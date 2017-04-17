package com.hifx.mapper.cache;


import com.hifx.mapper.MultiFunction;
import com.hifx.mapper.algo.DistanceCalculator;
import com.hifx.mapper.data.Location;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A simplistic cache using a {@link ConcurrentHashMap}. There's no eviction
 * policy, it just fills up until reaching the specified capacity (or
 * close enough at least, bounds check is not atomic :)
 */
public class CHMCache implements ICache {
    private static final int DEFAULT_CAPACITY = 4096;
    private final int capacity;
    private final ConcurrentHashMap<String, Location> cache;
    private boolean cacheFull = false;

    public CHMCache() {
        this(DEFAULT_CAPACITY);
    }

    public CHMCache(int capacity) {
        this.capacity = capacity;
        this.cache = new ConcurrentHashMap<String, Location>(capacity);
    }

    public Location get(double latitude, double longitude, MultiFunction<Double, Double, Location> load) throws IOException {
        String key = DistanceCalculator.getIndex(latitude, longitude);
        Location value = cache.get(key);
        if (value == null) {
            value = load.apply(latitude, longitude);
            if (value == null) {
                value = Location.getUnknown();
            }
            //bounds check is not atomic :(
            if (!cacheFull) {
                if (cache.size() < capacity) {
                    cache.put(key, value);
                } else {
                    cacheFull = true;
                }
            }
        }
        return value;
    }
}
