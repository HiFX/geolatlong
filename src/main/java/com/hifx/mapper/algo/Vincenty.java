package com.hifx.mapper.algo;

/**
 * ...
 */
public class Vincenty extends DistanceCalculator {
    /**
     * Calculates the distance using Vincenty's formulae
     * https://en.wikipedia.org/wiki/Vincenty%27s_formulae
     * We are using WGS-84 ellipsoid model here.
     * Refer http://www.movable-type.co.uk/scripts/latlong-vincenty.html for more details
     *
     * @param latitude1  the latitude of the first point
     * @param longitude1 the longitude of the first point
     * @param latitude2  the latitude of the second point
     * @param longitude2 the longitude of the second point
     * @return the distance, in meters, between the two points
     */
    public double getDistance(double latitude1, double longitude1, double latitude2, double longitude2) {
        //major axis of the ellipsoid
        double A_WGS84 = 6378137;
        //minor axis of the ellipsoid
        double B_WGS84 = 6356752.314245;
        // flattening (a−b)/a
        double F_WGS84 = 1 / 298.257223563; // WGS-84 ellipsoid params

        //difference in longitude
        double L = Math.toRadians((longitude2 - longitude1));

        //reduced latitude
        double U1 = Math.atan((1 - F_WGS84) * Math.tan(Math.toRadians(latitude1)));
        double U2 = Math.atan((1 - F_WGS84) * Math.tan(Math.toRadians(latitude2)));

        //trig identities
        double sinU1 = Math.sin(U1), cosU1 = Math.cos(U1);
        double sinU2 = Math.sin(U2), cosU2 = Math.cos(U2);

        double lambda = L, lambdaP, iterLimit = 1000, cosSqAlpha, cosSigma, sigma, sinAlpha, cos2SigmaM, sinSigma, sinLambda, cosLambda;

        //iterate until change in λ is negligible or give up after 1000 attempts
        do {
            sinLambda = Math.sin(lambda);
            cosLambda = Math.cos(lambda);

            sinSigma = Math.sqrt(
                (cosU2 * sinLambda) * (cosU2 * sinLambda) + (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda) * (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda));

            // co-incident points
            if (sinSigma == 0) {
                return 0.0;
            }
            cosSigma = sinU1 * sinU2 + cosU1 * cosU2 * cosLambda;
            sigma = Math.atan2(sinSigma, cosSigma);
            sinAlpha = cosU1 * cosU2 * sinLambda / sinSigma;
            cosSqAlpha = 1 - sinAlpha * sinAlpha;

            if (cosSqAlpha == 0.0) {
                cos2SigmaM = 0;
            } else {
                cos2SigmaM = cosSigma - 2 * sinU1 * sinU2 / cosSqAlpha;
            }

            double C = F_WGS84 / 16 * cosSqAlpha * (4 + F_WGS84 * (4 - 3 * cosSqAlpha));
            lambdaP = lambda;
            lambda = L + (1 - C) * F_WGS84 * sinAlpha * (sigma + C * sinSigma * (cos2SigmaM + C * cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM)));

        } while (Math.abs(lambda - lambdaP) > 1e-12 && --iterLimit > 0);

        // formula failed to converge
        if (iterLimit == 0)
            return Double.NaN;

        double uSq = cosSqAlpha * (A_WGS84 * A_WGS84 - B_WGS84 * B_WGS84) / (B_WGS84 * B_WGS84);
        double A = 1 + uSq / 16384 * (4096 + uSq * (-768 + uSq * (320 - 175 * uSq)));
        double B = uSq / 1024 * (256 + uSq * (-128 + uSq * (74 - 47 * uSq)));
        double deltaSigma =
            B * sinSigma * (cos2SigmaM + B / 4 * (cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM) - B / 6 * cos2SigmaM * (-3 + 4 * sinSigma * sinSigma) * (-3
                + 4 * cos2SigmaM * cos2SigmaM)));

        return B_WGS84 * A * (sigma - deltaSigma);
    }

}
