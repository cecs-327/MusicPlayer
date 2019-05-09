package dfs;

import java.io.IOException;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public interface MapReduceInterface {
	public void map(String key, JsonElement value, FileMapObject fileMapObject,
	String file) throws IOException;
	public String reduce(String key, List<JsonElement> values) throws Exception;
}
