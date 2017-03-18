package com.norddev.memugps;

public class GeoPoint {
    private final double lat;
    private final double lon;

    public GeoPoint(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public GeoPoint addLat(double latToAdd) {
        return new GeoPoint(lat + latToAdd, lon);
    }

    public GeoPoint addLon(double lonToAdd) {
        return new GeoPoint(lat, lon + lonToAdd);
    }

    @Override
    public String toString() {
        return String.format("GeoPoint{lat=%.6f lon=%.6f}", lat, lon);
    }
}