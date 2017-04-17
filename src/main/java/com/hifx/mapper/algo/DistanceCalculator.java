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
     * getNeighbourIndexes returns all compound index of the neighbour for the given location bases on the level.
     * level == 1: is special case return centre node and 8 neighbour square.
     * level > 1: will return all the neighbour nodes those are 'level' distance away from the center node And exclude all the nodes which lies in the 'level' - 1.
     *
     * @param latitude        of the loc
     * @param longitude       of the loc
     * @param level(distance) number of nodes away from the own node. Exclude all the nodes in (level-1) level.
     * @return String[] indexes
     */

    public static String[] getNeighbourIndexes(double latitude, double longitude, int level) {
        int innerCubeWidth = 1;

        //initialize the index array based on the level.
        String[] result;
        if (level == 0) {
            result = new String[1];
        } else if (level == 1) {
            //special case: include the center point with the result.
            result = new String[9];
            result[8] = getIndex(latitude, longitude);
        } else if (level > 1) {
            result = new String[level * 8];
            innerCubeWidth = 2 * (level - 1) + 1;
        } else {
            return null;
        }

        //assign the neighbour indexes to the result array.
        int resultInd = 0;
        int additionalIncr = 0;
        for (int latInd = (-1 * level); latInd <= level; latInd++) {
            for (int lonInd = (-1 * level); lonInd <= level; lonInd = lonInd + 1 + additionalIncr) {
                result[resultInd] = getIndex((int)latitude + latInd, (int)longitude + lonInd);
                resultInd++;
            }
            if (latInd == (level - 1)) {
                additionalIncr = 0;
                //do not skip anything on the last row.
            } else {
                //skip the all the index those lies in the inner cube.
                additionalIncr = innerCubeWidth;
            }
        }
        return result;
    }

}
