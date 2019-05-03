package dfs;

import java.io.IOException;

import com.google.gson.JsonObject;

public class Mapper implements MapReduceInterface {

	@Override
	public void map(String key, JsonObject value, DFS context, String file) throws IOException {
		//let newKey be the song title in value
		//let newValue be a subset of value
		
		
		// The new values can have the items of interest
		// Song title, year of release, duration, artist and album
		
		//TODO values must be appropriately filled
		String newKey = "";
		JsonObject newValue = null;
		context.emit(newKey, newValue, file);	
	}

	@Override
	public void reduce(String key, JsonObject values, DFS context, String file) throws IOException {
		sort(values);
		context.emit(key, values, file);
	}
	
	private JsonObject sort(JsonObject valuesUnsorted) {
		//TODO sort
		return null;
		
	}
}
