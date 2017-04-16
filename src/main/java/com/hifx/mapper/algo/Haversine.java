package com.hifx.mapper.algo;

/**
 * ...
 */
public class Haversine extends DistanceCalculator {
    /**
     * Gets the great circle distance in meters between two geographical points, using
     * the <a href="http://en.wikipedia.org/wiki/Haversine_formula">haversine formula</a>.
     *
     * @param latitude1  the latitude of the first point
     * @param longitude1 the longitude of the first point
     * @param latitude2  the latitude of the second point
     * @param longitude2 the longitude of the second point
     * @return the distance, in meters, between the two points
     */
    public double getDistance(double latitude1, double longitude1, double latitude2, double longitude2) {

        // Approximated average radius of earth in meters
        // some calculations also use 6371.0 km ( authalic radius extracted from surface area )
        double RADIUS = 6372.8 * 1000;

        double dLat = Math.toRadians(latitude2 - latitude1);
        double dLon = Math.toRadians(longitude2 - longitude1);

        latitude1 = Math.toRadians(latitude1);
        latitude2 = Math.toRadians(latitude2);

        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(latitude1) * Math.cos(latitude2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return RADIUS * c;
    }
}
