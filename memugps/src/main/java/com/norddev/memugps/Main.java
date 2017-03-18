package com.norddev.memugps;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

public class Main {

    private static final String DESTINATIONS = "52.216265,20.980861\n";
/*
    private static final String DESTINATIONS =
            "-122.3426181,47.6517367,21\n" +
                    "-122.3428059,47.6485638,8\n" +
                    "-122.3403597,47.648188,8\n" +
                    "-122.3381066,47.6469231,8\n" +
                    "-122.3379993,47.6466774,7\n" +
                    "-122.3366261,47.6461425,11\n" +
                    "-122.335521,47.6461425,12\n" +
                    "-122.3344052,47.6464533,12\n" +
                    "-122.3340297,47.6466701,13\n" +
                    "-122.3331285,47.6466918,12\n" +
                    "-122.3330855,47.6455931,7\n" +
                    "-122.3342335,47.6446824,11\n" +
                    "-122.3357892,47.6446607,15\n" +
                    "-122.3368728,47.6450944,9\n" +
                    "-122.336669,47.645904,10\n" +
                    "-122.3359609,47.6451595,13\n" +
                    "-122.3353279,47.6450799,14\n" +
                    "-122.3344266,47.6449715,12\n" +
                    "-122.3336327,47.6454847,8\n" +
                    "-122.3350596,47.645463,13\n" +
                    "-122.3357999,47.645557,13\n" +
                    "-122.3359179,47.6458028,13\n" +
                    "-122.3346734,47.6458678,10\n" +
                    "-122.3337293,47.6459545,10\n" +
                    "-122.3328388,47.6474869,11\n" +
                    "-122.3313475,47.6498648,8\n" +
                    "-122.3273563,47.6533629,9\n" +
                    "-122.3234081,47.653941,5\n" +
                    "-122.3213911,47.6544903,5\n" +
                    "-122.3174429,47.6537965,11\n" +
                    "-122.3128939,47.6531027,24\n" +
                    "-122.3081732,47.6507899,27\n" +
                    "-122.3054695,47.6505876,26\n" +
                    "-122.3111773,47.6583638,59\n" +
                    "-122.3066282,47.6581325,61\n" +
                    "-122.3060703,47.660127,61\n" +
                    "-122.3028517,47.6588841,40\n" +
                    "-122.3008776,47.6602426,11\n" +
                    "-122.3007488,47.6638266,9\n" +
                    "-122.2962427,47.6642602,13\n" +
                    "-122.296114,47.6615433,10\n" +
                    "-122.2997189,47.6613121,8\n" +
                    "-122.3001051,47.6632197,8\n" +
                    "-122.2975302,47.663422,8\n" +
                    "-122.2971869,47.6621503,9\n" +
                    "-122.2984314,47.6620925,8";
*/

    private static List<GeoPoint> getDestinations() throws IOException {
        List<GeoPoint> destinations = new LinkedList<>();
        BufferedReader reader = new BufferedReader(new StringReader(DESTINATIONS));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            double lon = Double.parseDouble(parts[0]);
            double lat = Double.parseDouble(parts[1]);
            destinations.add(new GeoPoint(lat, lon));
        }
        return destinations;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        MEMuDevice device = new MEMuDevice();
        device.connect();
        File routeFile = new File("memugps/route.kml");
        if(!routeFile.exists()){
            System.err.println("Route file does not exist: " + routeFile.getAbsolutePath());
            System.exit(1);
        }
        KMLDestinationParser parser = new KMLDestinationParser(routeFile);
        List<GeoPoint> destinations = parser.parse(); //getDestinations();
        Runner runner = new Runner(device, destinations);
        runner.start();
        System.exit(0);
    }

}