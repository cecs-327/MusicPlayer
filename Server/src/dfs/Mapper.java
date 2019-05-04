package dfs;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Mapper implements MapReduceInterface {
	
	@Override
	public void map(String key, JsonElement value, DFS context, String file) throws IOException {
		//let newKey be the song title in value
		//let newValue be a subset of value
		String artistName = "";
		String songTitle = "";
		String hottness = "0.0";
		String fileName = "../mp3/bensound-energy.mp3";
		
		if(!value.getAsJsonObject().get("artist").getAsJsonObject().get("name").isJsonNull())
			artistName = value.getAsJsonObject().get("artist").getAsJsonObject().get("name").getAsString();
		if(!value.getAsJsonObject().get("song").getAsJsonObject().get("title").isJsonNull())
			songTitle = value.getAsJsonObject().get("song").getAsJsonObject().get("title").getAsString();
		if(!value.getAsJsonObject().get("song").getAsJsonObject().get("hotttnesss").isJsonNull())
			hottness = value.getAsJsonObject().get("song").getAsJsonObject().get("hotttnesss").getAsString();
		if(!value.getAsJsonObject().get("file").isJsonNull())
			fileName = value.getAsJsonObject().get("file").getAsString();
		
		String jsonObject = "{\n\t\"artistName\": \"" + artistName + "\",\n\t\"hottness\": \"" + hottness+ "\",\n\t\"fileName\": \""+ fileName + "\"\n}";
		System.out.println(songTitle + ":" + jsonObject);
		if(!songTitle.equals("")) {
			JsonParser parser = new JsonParser();
			JsonObject jsonObj = (JsonObject)parser.parse(jsonObject);
			context.fileMapObject.emit(songTitle, jsonObj);
		}
			
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
