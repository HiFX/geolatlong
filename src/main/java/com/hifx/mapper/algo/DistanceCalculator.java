package com.hifx.mapper.algo;

import java.text.DecimalFormat;

/**
 * DistanceCalculator provides the abstract design for algos that implements the
 * calculation of distance between two locations identified by a lat+long pair
 */
public abstract class DistanceCalculator {

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

    public abstract double getDistance(double latitude1, double longitude1, double latitude2, double longitude2);

    /**
     * getIndex returns a compound index for given location.
     *
     * @param latitude  lat of the loc
     * @param longitude long of the loc
     * @return String Index
     */
    public static String getIndex(double latitude, double longitude) {
        String pattern = "000";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);

        String latStr = decimalFormat.format((int) (latitude));
        String lonStr = decimalFormat.format((int) (longitude));
        return latStr + ":" + lonStr;
    }


    /**
     * getIndexesAround returns an compound index for given location and 8 other squares around it.
     *
     * @param latitude  of the loc
     * @param longitude of the loc
     * @return String[] indexes
     */

    public static String[] getIndexesAround(double latitude, double longitude) {
        String[] result = new String[9];

        for (int latInd = -1; latInd <= 1; latInd++)
            for (int lonInd = -1; lonInd <= 1; lonInd++) {
                result[(latInd + 1) * 3 + (lonInd + 1)] = getIndex(latitude + ((double) latInd), longitude + ((double) lonInd));
            }

        return result;
    }
}
