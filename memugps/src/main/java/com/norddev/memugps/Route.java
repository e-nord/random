package com.norddev.memugps;

import org.opensextant.geodesy.*;

import java.util.LinkedList;
import java.util.List;

public class Route {

    private static final double FEET_PER_MILE = 5280;
    private static final double SECONDS_PER_HOUR = 3600;

    private final List<GeoPoint> mPath;

    public Route(List<GeoPoint> path) {
        this.mPath = path;
    }

    public List<GeoPoint> getPath() {
        return mPath;
    }

    public static Route generate(GeoPoint start, GeoPoint end, double mph, long delayBetweenPointsMs) {
        Geodetic2DPoint startPoint = new Geodetic2DPoint(new Longitude(start.getLon(), Angle.DEGREES), new Latitude(start.getLat(), Angle.DEGREES));
        Geodetic2DPoint endPoint = new Geodetic2DPoint(new Longitude(end.getLon(), Angle.DEGREES), new Latitude(end.getLat(), Angle.DEGREES));
        Geodetic2DArc arc = new Geodetic2DArc(startPoint, endPoint);
        double distanceFt = arc.getDistanceInMeters() / 0.305;

        double feetPerSec = (mph * FEET_PER_MILE / SECONDS_PER_HOUR);
        double seconds = distanceFt / feetPerSec;
        double totalLatDelta = end.getLat() - start.getLat();
        double totalLonDelta = end.getLon() - start.getLon();
        double totalPoints = (1000 / delayBetweenPointsMs) * seconds;
        double latDelta = totalLatDelta / totalPoints;
        double lonDelta = totalLonDelta / totalPoints;

        System.out.println(mph + "MPH -> " + feetPerSec + "FPS (distanceFt=" + distanceFt + " seconds=" + seconds + ")");
        System.out.println(totalPoints + " points in route (latDelta=" + latDelta + " lonDelta=" + lonDelta + ")");

        List<GeoPoint> points = new LinkedList<>();
        points.add(start);
        GeoPoint prev = start;
        for (int i = 0; i < totalPoints; i++) {
            GeoPoint point = prev.addLat(latDelta).addLon(lonDelta);
            points.add(point);
            prev = point;
        }
        points.add(end);
        return new Route(points);
    }
}
