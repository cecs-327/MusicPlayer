package gson;
import java.io.*;

public class Release {
    private double id;
    private String name;

    public Release(double i, String n) {
        id = i;
        name = n;
    }

    public Release() {}

    public double getID() {
        return id;
    }
}