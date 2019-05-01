package gson;
import java.io.*;

public class Song {
    public float key;
    public double mode_confidence; 
    public double artist_mbtags_count;
    public double key_confidence;
    public double tatums_start;
    public int year;
    public double duration;
    public double hotttnesss;
    public double beats_start;
    public double time_signature_confidence;
    public String title; 
    public double bars_confidence;
    public String id;
    public double bars_start;
    public String artist_mbtags; 
    public double start_of_fade_out;
    public double tempo; 
    public double end_of_fade_in;
    public double beats_confidence;
    public double tatums_confidence;
    public double mode;
    public double time_signature;
    public double loudness;

    public Song(float k, double m, double a, double key, double tat, int y, double d, double h,
    double b, double time, String ttl, double bars, String i, double bs, String am, double sf, double tem,
    double eof, double bc, double tatums, double mode, double ts, double loud) {
        key = k;
        mode_confidence = m;
        artist_mbtags_count = a;
        key_confidence = key;
        tatums_confidence = tat;
        year = y;
        duration = d;
        hotttnesss = h;
        beats_start = b;
        time_signature_confidence = time;
        title = ttl;
        bars_confidence = bars;
        id = i;
        bars_start = bs;
        artist_mbtags = am;
        start_of_fade_out = sf;
        tempo = tem;
        end_of_fade_in = eof;
        beats_confidence = bc;
        tatums_confidence = tatums;
        this.mode = mode;
        time_signature = ts;
        loudness = loud;
    }

    public Song() {}

    public double getHottness() {
        return hotttnesss;
    }

    public String getTitle() {
        return title;
    }

    public String getID() {
        return id;
    }
}