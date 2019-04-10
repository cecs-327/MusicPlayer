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

        for(int i = 0; i < IL && expandArray.size() != 0; i++) {
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
    public void getSongs() {
    	JsonParser parser = new JsonParser();
    	JsonArray jsonArray = new JsonArray();
    	try {
    		String jsonfile = pathHolder.readFile("songs");
    		System.out.print(jsonfile.substring(0, 200));
//    		System.out.println("Result of Read:\n" + jsonfile);
//            String jsonfile = new String(Files.readAllBytes(Paths.get(pathHolder.songPath)));
            musicFile = parser.parse(jsonfile).getAsJsonArray();
            //System.out.println(jsonArray);

        } catch (Exception e)
        {
            System.out.println(e);
        }
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
        //List<Songs> songList = getSongs();
    	getSongs();
    	for(int i = 0; i < finalResults.size(); i++) {
    		finalResults.remove(0);
    	}
//    	finalResults = new JsonArray();

        for(int i = 0; i < musicFile.size(); i++) {
            String[] wordsInArtistName = musicFile.get(i).getAsJsonObject().get("artist").getAsJsonObject().get("name").getAsString().split(" ");
            String[] wordsInSongTitle = musicFile.get(i).getAsJsonObject().get("song").getAsJsonObject().get("title").getAsString().split(" ");

            if((userInput.length() == 1) &&
            		((userInput.equalsIgnoreCase(musicFile.get(i).getAsJsonObject().get("song").getAsJsonObject().get("title").getAsString().substring(0,1)))
            		|| (userInput.equalsIgnoreCase(musicFile.get(i).getAsJsonObject().get("artist").getAsJsonObject().get("name").getAsString().substring(0,1)))))
            {
                finalResults.add(musicFile.get(i).getAsJsonObject());

            }
            else if( (userInput.equalsIgnoreCase(musicFile.get(i).getAsJsonObject().get("song").getAsJsonObject().get("title").getAsString()) )
    				|| (userInput.equalsIgnoreCase(musicFile.get(i).getAsJsonObject().get("artist").getAsJsonObject().get("name").getAsString())) )
            {
                finalResults.add(musicFile.get(i).getAsJsonObject());

            }
            else if(wordsInSongTitle[0].toLowerCase() == userInput.toLowerCase() || wordsInArtistName[0].toLowerCase() == userInput.toLowerCase()) {
                firstWordMatch.add(musicFile.get(i).getAsJsonObject());

            }
            else if(compareInputToSome(wordsInSongTitle, userInput) || compareInputToSome(wordsInArtistName, userInput))
            {
                someWordMatch.add(musicFile.get(i).getAsJsonObject());
            }
            else {
                notRelevant.add(musicFile.get(i).getAsJsonObject());
            }
        }

       finalResults = getSortedList(finalResults);
       // getSortedListPractice(finalResults);

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
        	singleResult.addProperty("file", finalResults.get(i).getAsJsonObject().get("file").getAsString());
        	singleResult.addProperty("artist", finalResults.get(i).getAsJsonObject().get("song").getAsJsonObject().get("title").getAsString());
        	singleResult.addProperty("title", finalResults.get(i).getAsJsonObject().get("artist").getAsJsonObject().get("name").getAsString());
        	finalizeArray.add(singleResult);
        }
        List<Songs> fs = new ArrayList<Songs>();
        
        for(int i = 0; i < finalResults.size(); i++) {
        	Songs s = new Songs(finalResults.get(i).getAsJsonObject().get("song").getAsJsonObject().get("title").getAsString(), 
        			finalResults.get(i).getAsJsonObject().get("artist").getAsJsonObject().get("name").getAsString(), 
        			finalResults.get(i).getAsJsonObject().get("file").getAsString());
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
        System.out.println(search.searchSong("have", "remote"));

    }

}