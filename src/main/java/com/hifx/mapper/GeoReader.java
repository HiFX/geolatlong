package com.hifx.mapper;

import com.hifx.mapper.algo.DistanceCalculator;
import com.hifx.mapper.algo.Haversine;
import com.hifx.mapper.algo.Vincenty;
import com.hifx.mapper.cache.CHMCache;
import com.hifx.mapper.cache.ICache;
import com.hifx.mapper.cache.NoCache;
import com.hifx.mapper.data.Location;
import org.simpleflatmapper.csv.CsvParser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GeoReader is the reader class responsible for parsing the latitude, longitude, city, country CSV file
 * and provides functions for lookup
 */
public class GeoReader {
    private ICache cache;
    private DistanceCalculator calculator;
    private Map<String, List<Location>> cities = new HashMap<String, List<Location>>();

    private GeoReader(Builder builder) throws IOException {
        this(builder.mapFile, builder.cache, builder.calculator);
    }

    /**
     * Constructs a GeoReader for the latlog to city map
     *
     * @param mapFile the lat+long to city map
     * @param cache   backing cache instance
     * @throws IOException if there is an error opening or reading from the file.
     */
    private GeoReader(File mapFile, ICache cache, DistanceCalculator calculator) throws IOException {
        this.cache = cache;
        this.calculator = calculator;
        CsvParser.separator(',').
            mapTo(Location.class).
            headers("latitude", "longitude", "countryCode", "country", "city").
            stream(mapFile, (s) -> {
                s.forEach((city) -> {
                    String index = DistanceCalculator.getIndex(city.getLatitude(), city.getLongitude());
                    List<Location> sameIndexCities = cities.get(index);
                    if (sameIndexCities == null)
                        sameIndexCities = new ArrayList<>();
                    sameIndexCities.add(city);
                    cities.put(index, sameIndexCities);
                });
                return null;
            });
    }

    // get fetches the location correspoinding to the latitide and longitude
    public Location get(double latitude, double longitude) throws IOException {
        return cache.get(latitude, longitude, this::lookup);
    }

    //lookup performs the lookup on the hashmap for lat+long if there is no cache involved
    public Location lookup(double latitude, double longitude) {
        Location result = null;
        String[] indexes = DistanceCalculator.getIndexesAround(latitude, longitude);

        for (String index : indexes) {
            List<Location> citiesForIndex = cities.get(index);
            if (citiesForIndex != null) {
                for (Location city : citiesForIndex) {
                    if (result == null)
                        result = city;
                    else {
                        double resultDistance = calculator.getDistance(latitude, longitude, result.getLatitude(), result.getLongitude());
                        double cityDistance = calculator.getDistance(latitude, longitude, city.getLatitude(), city.getLongitude());
                        if (cityDistance < resultDistance)
                            //city is closer than earlier result
                            result = city;
                    }
                }
            }
        }
        return result;
    }



    /**
     * <p>
     * Constructs a Builder for the GeoReader.
     * </p>
     * <p>
     * {@code Builder} creates instances of {@code GeoReader}
     * from values set by the methods.
     * </p>
     * <p>
     * Only the values set in the {@code Builder} constructor are required.
     * </p>
     */
    public static final class Builder {
        File mapFile;
        DistanceCalculator calculator = new Vincenty();
        //default map file
        String pathName = "data/latlong.cities.csv";
        ICache cache = new NoCache();

        /**
         * The file passed to it must be a valid lat+long->city csv file
         *
         * @param database the lat+long->city csv file to use.
         */
        public Builder(File database) {
            this.mapFile = database;
        }

        public Builder() {
            ClassLoader classLoader = getClass().getClassLoader();
            this.mapFile = new File(classLoader.getResource(pathName).getFile());
        }

        /**
         * add caching to the reader
         *
         * @param cache the backing cache instance
         * @return Builder object
         */
        public Builder withCache(ICache cache) {
            this.cache = cache;
            return this;
        }

        /**
         * withDefaultCache creates an instance of CHM cache and assigns it to the GeoReader
         *
         * @param capacity number of items to cache
         * @return Builder object
         */
        public Builder withDefaultCache(int capacity) {
            this.cache = new CHMCache(capacity);
            return this;
        }

        /**
         * use Haversine formula for faster, perhaps less accurate results
         *
         * @return Builder object
         */
        public Builder faster() {
            this.calculator = new Haversine();
            return this;
        }

        /**
         * @return an instance of {@code DatabaseReader} created from the
         * fields set on this builder.
         * @throws IOException if there is an error reading the database
         */
        public GeoReader build() throws IOException {
            return new GeoReader(this);
        }
    }
}
