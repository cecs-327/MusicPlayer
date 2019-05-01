package gson;
import java.io.*;

public class Artist {
    private float terms_freq; 
    private String terms;
    private String name;
    private double familiarity;
    private double longitude;
    private String id;
    private String location;
    private double latitude;
    private String similar;
    private double hotttnesss;

    public Artist(float tf, String t, String n, double f, double l, String i, String loc, double lat,
    String sim, double hot) {
        terms_freq = tf;
        terms = t;
        name = n;
        familiarity = f;
        longitude = l;
        id = i;
        location = loc;
        latitude = lat;
        similar = sim;
        hotttnesss = hot;
    }

    public Artist() {}

    public String getName() {
        return name;
    }
}