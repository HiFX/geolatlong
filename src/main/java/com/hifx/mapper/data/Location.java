package com.hifx.mapper.data;


import java.io.Serializable;

/**
 * Location class is used to represent a city identified by a lat, long pair
 */
public class Location implements Serializable {

    private double latitude;
    private double longitude;
    private String countryCode;
    private String country;
    private String city;

    @Override
    public String toString() {
        return "Location{" + "latitude=" + latitude + ", longitude=" + longitude + ", countryCode='" + countryCode + '\'' + ", country='" + country + '\''
            + ", city='" + city + '\'' + '}';
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}

