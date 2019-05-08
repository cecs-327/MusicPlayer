package services;
import java.io.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import gson.Songs;
import gson.searchReturn;

import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.*;
import java.math.BigDecimal;


public class SearchServices {

    private String songsPath;
    private JsonArray firstWordMatch;
    private JsonArray someWordMatch;
    private JsonArray substringMatch;
    private JsonArray notRelevant;
    JsonArray finalResults;
    private int optionMenu;
    private JsonArray musicFile;

    public SearchServices() {
        songsPath = pathHolder.songPath;
    }


    public static void getSortedListPractice(JsonArray array)
    {
    	
    	for(int i =0; i < array.size();i++)
    	{
    		JsonObject jsonObject = array.get(i).getAsJsonObject();
    		try
    		{
    			jsonObject.getAsJsonObject("song").get("hotttnesss").getAsFloat();


    		}catch(Exception e)
    		{
    			System.out.println("null as value");
    		}
    	}
    }


    public static JsonArray getSortedList(JsonArray array){
        List<JsonObject> list = new ArrayList<JsonObject>();
        for (int i = 0; i < array.size(); i++) {
                list.add(array.get(i).getAsJsonObject());
        }
        try
        {
        	Collections.sort(list, new SortBasedOnHotness());
        }
        catch(IllegalArgumentException i )
        {

        }
        JsonArray resultArray = new JsonArray();
        for(int x = 0; x < list.size(); x++)
        {
        	resultArray.add(list.get(x));
        }

        return resultArray;
    }



    public boolean compareInputToSome(String[] values, String userInput) {
        boolean flag = false;
        for(int i = 0; i < values.length; i++) {
            if(values[i].equalsIgnoreCase(userInput))
            {

                flag = true;
            }
        }
        return flag;
    }

    public void addToFinalList(JsonArray expandArray, int IL) {
    	expandArray = getSortedList(expandArray);
        for(int i = 0; i < IL && expandArray.size() != 0 && expandArray.size() >  i; i++) {
            finalResults.add(expandArray.get(i));
        }
    }

    public void makeListBigger() {
        int increaseListSize = 0;
        while(finalResults.size() < 5 || optionMenu < 4) {
            increaseListSize = 5 - finalResults.size();
            if(optionMenu == 0) {
                addToFinalList(firstWordMatch, increaseListSize);
            }
            else if(optionMenu == 1) {
                addToFinalList(someWordMatch, increaseListSize);
            }
            else if(optionMenu == 2) {
                addToFinalList(substringMatch, increaseListSize);
            }
            else if(optionMenu == 3) {
                addToFinalList(notRelevant, increaseListSize);
            }
            optionMenu += 1;
        }
    }
    //return List<Songs>
    public  JsonArray getSongs() {
    	JsonParser parser = new JsonParser();
    
    	try {
    		
    		//TODO: CHANGE THIS TO READ FROM pathHolder
    		 
    		String jsonfile = new String(Files.readAllBytes(Paths.get("D:\\CSULB\\presemt\\327\\MusicPlayer\\Server\\src\\dumpyMusic.json")));
            musicFile = parser.parse(jsonfile).getAsJsonArray();
    		
//    		String jsonfile = pathHolder.readFile(pathHolder.newMusicFile);
//    		//System.out.print("\nLast part of JsonFile:\n" + jsonfile.substring(jsonfile.length() - 200, jsonfile.length()));
//            musicFile = parser.parse(jsonfile).getAsJsonArray();
//            System.out.println("Successful print");
        } catch (Exception e)
        {
            System.out.println(e);
        }
    	return musicFile;
    }

   // return ArrayList<Songs>
    public String searchSong(String userInput, String remoteMethod)
    {
    	finalResults = new JsonArray();
        firstWordMatch = new JsonArray();
        someWordMatch = new JsonArray();
        substringMatch = new JsonArray();
        notRelevant = new JsonArray();
        optionMenu = 0;

    	//getSongs();
        
    	for(int i = 0; i < finalResults.size(); i++) {
    		finalResults.remove(0);
    	}
    	musicFile = getSongs();
    	Set<String>key = musicFile.get(0).getAsJsonObject().keySet();
    	String songTitles = key.iterator().next();
    	String artistName = musicFile.get(0).getAsJsonObject().get(songTitles).getAsJsonArray().get(0).getAsJsonObject().get("artistName").getAsString();
    	int sizeOfJsonArray = musicFile.get(0).getAsJsonObject().get(songTitles).getAsJsonArray().size();
    	String[] titleArray = key.iterator().next().split(" ");
        for(int i = 0; i < musicFile.size(); i++) {
        	Set<String>songTitleSet = musicFile.get(i).getAsJsonObject().keySet();
        	String songTitle = songTitleSet.iterator().next();
        	int jsonArraySize = musicFile.get(i).getAsJsonObject().get(songTitle).getAsJsonArray().size();
        	
        	
        	
        	for(int x = 0; x < jsonArraySize; x++)
        	{
	            
	            String[] wordsInSongTitle = songTitle.split(" ");
	            JsonObject songObject = new JsonObject();
	            if((userInput.length() == 1) && ((userInput.equalsIgnoreCase(songTitle.substring(0,1)))))
	            {
	            	songObject.add(songTitle, musicFile.get(i).getAsJsonObject().get(songTitle).getAsJsonArray().get(x).getAsJsonObject());
	                finalResults.add(songObject);
	
	            }
	            else if(userInput.equalsIgnoreCase(songTitle))
	            {
	            	songObject.add(songTitle, musicFile.get(i).getAsJsonObject().get(songTitle).getAsJsonArray().get(x).getAsJsonObject());
	                finalResults.add(songObject);
	
	            }
	            else if(wordsInSongTitle[0].toLowerCase() == userInput.toLowerCase()) {
	            	songObject.add(songTitle, musicFile.get(i).getAsJsonObject().get(songTitle).getAsJsonArray().get(x).getAsJsonObject());
	                finalResults.add(songObject);
	
	            }
	            else if(compareInputToSome(wordsInSongTitle, userInput))
	            {
	            	songObject.add(songTitle, musicFile.get(i).getAsJsonObject().get(songTitle).getAsJsonArray().get(x).getAsJsonObject());
	                someWordMatch.add(songObject);
	                
	            }
	            else {
	            	songObject.add(songTitle, musicFile.get(i).getAsJsonObject().get(songTitle).getAsJsonArray().get(x).getAsJsonObject());
	                notRelevant.add(songObject);
	               
	            }
        	}
        }
        
       finalResults = getSortedList(finalResults);
      

        if(finalResults.size() > 5) {
            int cutDown = finalResults.size() - 5;
            for(int i = 0; i < cutDown; i++) {
                int index = finalResults.size() - 1;
                finalResults.remove(index);
            }
        }
        else {
            makeListBigger();
        }




        //create the structure to return the json
        JsonArray finalizeArray = new JsonArray();
        
        for(int i = 0;i < finalResults.size();i++)
        {
        	JsonObject singleResult = new JsonObject();
        	Set<String>finalKey = finalResults.get(i).getAsJsonObject().keySet();
        	String songTitle = finalKey.iterator().next();
        	singleResult.addProperty("file", finalResults.get(i).getAsJsonObject().get(songTitle).getAsJsonObject().get("fileName").getAsString());
        	singleResult.addProperty("artist", finalResults.get(i).getAsJsonObject().get(songTitle).getAsJsonObject().get("artistName").getAsString());
        	singleResult.addProperty("title", songTitle);
        	finalizeArray.add(singleResult);
        }
        List<Songs> fs = new ArrayList<Songs>();
        
        for(int i = 0; i < finalResults.size(); i++) {
        	Set<String>finalKey = finalResults.get(i).getAsJsonObject().keySet();
        	String songTitle = finalKey.iterator().next();
        	Songs s = new Songs(songTitle, 
        			finalResults.get(i).getAsJsonObject().get(songTitle).getAsJsonObject().get("artistName").getAsString(), 
        			finalResults.get(i).getAsJsonObject().get(songTitle).getAsJsonObject().get("fileName").getAsString());
        	fs.add(s);
        }
        
        Gson g = new Gson();
        boolean successfulRegister = true;
        searchReturn sr = new searchReturn(fs, successfulRegister, remoteMethod);
        String JSONoutput = g.toJson(sr);
		
		return JSONoutput;

    }

    public static void main(String[] args) {
        // Instance of the search
    	SearchServices search = new SearchServices();
//        search.searchSong("dream");
        System.out.println(search.searchSong("fire", "remote"));

    }

}