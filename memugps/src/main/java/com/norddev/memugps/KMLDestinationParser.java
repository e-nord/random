package com.norddev.memugps;

import com.sun.media.sound.InvalidFormatException;
import de.micromata.opengis.kml.v_2_2_0.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class KMLDestinationParser {

    private final File mFile;

    public KMLDestinationParser(File file) {
        mFile = file;
    }

    public List<GeoPoint> parse() throws IOException {
        List<GeoPoint> points = new LinkedList<>();
        Path path = Paths.get(mFile.toURI());
        String content = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        content = content.replaceAll("http://earth.google.com/kml/2.2", "http://www.opengis.net/kml/2.2");
        Kml kml = Kml.unmarshal(content);
        if(kml != null) {
            Document document = (Document) kml.getFeature();
            List<Feature> features = document.getFeature();
            for (Feature feature : features) {
                if (feature.getName().equals("Fly-Over Tour")) {
                    Placemark placemark = (Placemark) feature;
                    LineString lineString = (LineString) placemark.getGeometry();
                    for (Coordinate coordinate : lineString.getCoordinates()) {
                        points.add(new GeoPoint(coordinate.getLatitude(), coordinate.getLongitude()));
                    }
                }
            }
        } else {
            throw new InvalidFormatException();
        }
        return points;
    }

}
