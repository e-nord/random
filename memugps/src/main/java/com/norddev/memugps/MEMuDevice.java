package com.norddev.memugps;

import java.io.IOException;

public class MEMuDevice {

    private static final String ADB_PATH = "C:\\Program Files\\Microvirt\\MEmu\\adb.exe";
    private static final String PROP_KEY_LAT = "microvirtd.gps.latitude";
    private static final String PROP_KEY_LON = "microvirtd.gps.longitude";
    private static final String CMD_SETPROP = "setprop";
    private static final String CMD_GETPROP = "getprop";

    private final Adb mAdb;

    public MEMuDevice() {
        Adb.ENABLE_ECHO_COMMAND = false;
        mAdb = new Adb(ADB_PATH);
    }

    public void connect() throws IOException, InterruptedException {
        if(mAdb.devices().isEmpty()){
            mAdb.connect("127.0.0.1").exec();
        }
    }

    public GeoPoint getCurrentLocation() throws IOException, InterruptedException {
        String[] latArgs = {CMD_GETPROP, PROP_KEY_LAT};
        String[] lonArgs = {CMD_GETPROP, PROP_KEY_LON};
        double lat = Double.parseDouble(mAdb.shell(latArgs).exec());
        double lon = Double.parseDouble(mAdb.shell(lonArgs).exec());
        return new GeoPoint(lat, lon);
    }

    public void setCurrentLocation(GeoPoint point) throws IOException, InterruptedException {
        String[] latArgs = {CMD_SETPROP, PROP_KEY_LAT, String.format("%.6f", point.getLat())};
        String[] lonArgs = {CMD_SETPROP, PROP_KEY_LON, String.format("%.6f", point.getLon())};
        mAdb.shell(latArgs).exec();
        mAdb.shell(lonArgs).exec();
    }

}
