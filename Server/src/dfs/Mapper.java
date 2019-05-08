package dfs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Mapper implements MapReduceInterface {
	String currentPage = "";
	List<String> str = new ArrayList<String>();

	@Override
	public void map(String key, JsonElement value, DFS context, String file) throws IOException {
		// let newKey be the song title in value
		// let newValue be a subset of value
		String artistName = "";
		String songTitle = "";
		String hottness = "0.0";
		String fileName = "../mp3/bensound-energy.mp3";

		if (!value.getAsJsonObject().get("artist").getAsJsonObject().get("name").isJsonNull())
			artistName = value.getAsJsonObject().get("artist").getAsJsonObject().get("name").getAsString()
					.replaceAll("\"", "");
		if (!value.getAsJsonObject().get("song").getAsJsonObject().get("title").isJsonNull())
			songTitle = value.getAsJsonObject().get("song").getAsJsonObject().get("title").getAsString()
					.replaceAll("\"", "");
		if (!value.getAsJsonObject().get("song").getAsJsonObject().get("hotttnesss").isJsonNull())
			hottness = value.getAsJsonObject().get("song").getAsJsonObject().get("hotttnesss").getAsString()
					.replaceAll("\"", "");
		if (!value.getAsJsonObject().get("file").isJsonNull())
			fileName = value.getAsJsonObject().get("file").getAsString().replaceAll("\"", "");

		String jsonObject = "{\n\t\"artistName\": \"" + artistName + "\",\n\t\"hottness\": \"" + hottness
				+ "\",\n\t\"fileName\": \"" + fileName + "\"\n}";
		
		
		if (!songTitle.equals("") && songTitle != null && !songTitle.isEmpty()) {
			JsonParser parser = new JsonParser();
			try {
				JsonObject jsonObj = (JsonObject) parser.parse(jsonObject);
			
				context.fileMapObject.emit(songTitle, jsonObj);
			} catch (Exception e) {
				System.out.println("Invalid json object\nStart Object\n" + jsonObject + "\nFinish Object");
			}

		}

	}

	@SuppressWarnings("null")
	@Override
	public void reduce(String key, List<JsonElement> values, DFS context, String file, String pageId) throws Exception {
		// Any Additional sorting can be done here
		/**
		 * Key: Ti Monde (LP Version) values:"artistName":"BeauSoleil" "hottness":"0"
		 * "fileName":"../mp3/bensound-sunny.mp3"
		 * 
		 * { Ti Monde (LP Version) : { artistName":"BeauSoleil", "hottness":"0",
		 * "fileName":"../mp3/bensound-sunny.mp3" } } JsonObject or Parsing it
		 * differently
		 */
		if (currentPage.equals(""))
			currentPage = pageId;
		else if (currentPage != pageId) {

			System.out.println( str.toString());
			context.appendPage(file, str.toString(), pageId);
			str = new ArrayList<String>();
		}

		System.out.println("Reduce called");
		StringBuilder data = new StringBuilder();
		data.append("{");
		int i = values.size();
		data.append("\"" + key + "\": [");
		for (JsonElement ele : values) {
			
			i--;

			data.append(ele.toString());

			if (i > 0) {
				data.append(",");
			}
		}
		
		
		data.append("]}");
	
		
		str.add(data.toString());
		

	}

	private JsonObject sort(JsonObject valuesUnsorted) {
		return null;
	}
}