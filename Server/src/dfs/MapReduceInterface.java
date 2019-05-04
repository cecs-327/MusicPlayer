package dfs;

import java.io.IOException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public interface MapReduceInterface {
public void map(String key, JsonElement value, DFS context,
String file) throws IOException;
public void reduce(String key, JsonElement values, DFS context,
String file) throws IOException;
}
