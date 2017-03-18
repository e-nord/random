package com.norddev.memugps;

import org.opensextant.geodesy.*;

import java.io.IOException;
import java.util.List;
import java.util.ListIterator;

public class Runner {

    private static final boolean DRY_RUN = false;
    private static final int TARGET_SPEED_MPH = 6;
    private static final long MS_DELAY = 500;

    private int mCurrentDestinationIndex;
    private MEMuDevice mDevice;
    private List<GeoPoint> mDestinations;
    private volatile boolean mIsRunning;

    public Runner(MEMuDevice device, List<GeoPoint> destinations) {
        mDevice = device;
        mDestinations = destinations;
    }

    public void start() throws IOException, InterruptedException {
        mCurrentDestinationIndex = findNearestDestinationIndex(mDevice.getCurrentLocation(), mDestinations);
        ListIterator<GeoPoint> iter = mDestinations.listIterator(mCurrentDestinationIndex);
        mIsRunning = true;
        while (iter.hasNext() && mIsRunning) {
            GeoPoint destination = iter.next();
            System.out.println("Moving to destination: " + destination);
            moveToDestination(mDevice, destination, TARGET_SPEED_MPH, MS_DELAY);
        }
    }

    public void stop(){
        mIsRunning = false;
    }

    private static int findNearestDestinationIndex(GeoPoint startPoint, List<GeoPoint> destinations) throws IOException, InterruptedException {
        GeoPoint nearest = findNearestDestination(startPoint, destinations);
        int startIndex = destinations.indexOf(nearest);
        System.out.println("Nearest point is: " + nearest + "(index=" + startIndex + ")");
        if(startIndex == destinations.size() - 1){
            System.out.println("Nearest point is at the end of the route. Starting at the beginning...");
            startIndex = 0;
        }
        return startIndex;
    }

    private static void moveToDestination(MEMuDevice device, GeoPoint destination, double speedMPH, long updateIntervalMs) throws IOException, InterruptedException {
        GeoPoint currentLocation = device.getCurrentLocation();
        System.out.println("Current location is " + currentLocation);
        Route outRoute = Route.generate(currentLocation, destination, speedMPH, updateIntervalMs);
        for (GeoPoint point : outRoute.getPath()) {
            System.out.println("Moving to " + point);
            if(!DRY_RUN) {
                device.setCurrentLocation(point);
            }
            Thread.sleep(updateIntervalMs);
        }
    }

    private static GeoPoint findNearestDestination(GeoPoint geoPoint, List<GeoPoint> destinations) {
        Geodetic2DPoint currentPoint = new Geodetic2DPoint(new Longitude(geoPoint.getLon(), Angle.DEGREES), new Latitude(geoPoint.getLat(), Angle.DEGREES));
        GeoPoint nearest = null;
        double distance = 0;
        for (GeoPoint destination : destinations) {
            Geodetic2DPoint destPoint = new Geodetic2DPoint(new Longitude(destination.getLon(), Angle.DEGREES), new Latitude(destination.getLat(), Angle.DEGREES));
            Geodetic2DArc arc = new Geodetic2DArc(currentPoint, destPoint);
            if (nearest == null || arc.getDistanceInMeters() < distance) {
                nearest = destination;
                distance = arc.getDistanceInMeters();
            }
        }
        return nearest;
    }

}
