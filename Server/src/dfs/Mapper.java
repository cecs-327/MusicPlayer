package dfs;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Mapper implements MapReduceInterface {

	@Override
	public void map(String key, JsonElement value, DFS context, String file) throws IOException {
		System.out.println("\n\nAt map phase\n\n");
		System.out.println(value.toString());
//		System.out.println(value.toString().substring(0, 500));
		//let newKey be the song title in value
		//let newValue be a subset of value
//		System.out.println("Reforming json");
//		Map<String, Object> jsonMap = new HashMap<String, Object>();
//		Set<Entry<String, JsonElement>> entrySet = value.entrySet();
//		for(Map.Entry<String, JsonElement> entry : entrySet) {
//			if(entry.getKey().equals("artist")) {
//				System.out.println(entry.getValue());
//			}else if(entry.getKey().equals("song")) {
//				System.out.println(entry.getValue());
//			}else if(entry.getKey().equals("file")) {
//				System.out.println(entry.getValue());
//			}
//		}
		
		// The new values can have the items of interest
		// Song title, year of release, duration, artist and album
		
		//TODO values must be appropriately filled
//		String newKey = "";
//		JsonObject newValue = null;
//		context.fileMapObject.emit(newKey, newValue);
	}

	@Override
	public void reduce(String key, JsonElement values, DFS context, String file) throws IOException {
//		sort(values);
//		context.fileMapObject.emit(key, values);
	}
	
	private JsonObject sort(JsonObject valuesUnsorted) {
		//TODO sort
		return null;
	}
}
