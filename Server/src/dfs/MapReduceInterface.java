package dfs;

import java.io.IOException;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public interface MapReduceInterface {
public void map(String key, JsonElement value, DFS context,
String file) throws IOException;
public void reduce(String key, List<JsonElement> values, DFS context,
String file, String pageId) throws Exception;
}
