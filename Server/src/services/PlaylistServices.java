package services;
import java.io.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import gson.*;

public class PlaylistServices{
    private String playlistPath = pathHolder.testPlaylists;
    private ArrayList<Songs> songsToAdd = new ArrayList<Songs>();
    private Playlists currentPlaylist;

    //Internal
    public ArrayList<Songs> getAPlaylistsSongs(Playlists playlistAffected){
        ArrayList<Songs> playlistsSongs = playlistAffected.getPlaylistsSongs();
        for(int i = 0; i < playlistsSongs.size(); i++) {
            System.out.println(playlistsSongs);
        }
        songsToAdd = (playlistsSongs);

        return songsToAdd;
    }

    //Internal
    public Playlists getPlaylist(String PlaylistTitle, String userName){
        UserPlaylists usersPlaylists = getUsersPlaylists(userName);
        Playlists playlist = null;
        ArrayList<Playlists> listOfUsersPlaylists = usersPlaylists.getPlaylists();
        for (Playlists p : listOfUsersPlaylists) {
            if(p.getPlaylistTitle().equalsIgnoreCase(PlaylistTitle)){
                playlist = p;
            }
        }
        currentPlaylist = playlist;
        songsToAdd = getAPlaylistsSongs(currentPlaylist);
        return currentPlaylist;
    }
    
    //Internal
    public ArrayList<UserPlaylists> getUserPlaylistsArrayList(){
        ArrayList<UserPlaylists> userPlaylistList = new ArrayList<UserPlaylists>();
        JsonParser parser = new JsonParser();
        try{
            String userPlaylists = pathHolder.readFile(pathHolder.playlistFile);
            Type jsonListType = new TypeToken<ArrayList<UserPlaylists>>() {}.getType();
            userPlaylistList = new Gson().fromJson(userPlaylists, jsonListType);
                        
            return userPlaylistList;
        }catch(Exception e){
            e.printStackTrace();
            return userPlaylistList;
        }
       
        
    }

    //Internal
    public UserPlaylists getUsersPlaylists(String userName){
        UserPlaylists usersPlaylists = null;
        ArrayList<UserPlaylists> userPlaylistList = new ArrayList<UserPlaylists>();
        
        try{
            userPlaylistList = getUserPlaylistsArrayList();
            for (UserPlaylists up : userPlaylistList) {
                if(up.getUserName().equalsIgnoreCase(userName)){
                    usersPlaylists = up;
                }
            }
            return usersPlaylists;
        }catch(Exception e){
            e.printStackTrace();
            return usersPlaylists;
        }
    }

    public String getAPlaylistsSongsJSON(String playlistTitle, String userName){
        boolean success = true;
        currentPlaylist = new Playlists(playlistTitle);
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("eventListenerName", "message-getPlaylistInfo");
        
        JsonObject data = new JsonObject();
        String stringifiedResponse = "";
        Playlists playlistAffected = getPlaylist(playlistTitle, userName);
        ArrayList<Songs> playlistsSongs = playlistAffected.getPlaylistsSongs();
        songsToAdd = playlistsSongs;
        
        JsonArray songsArray = new JsonArray();
        for(Songs s : songsToAdd){
            JsonObject songData = new JsonObject();
            songData.addProperty("SongTitle", s.getSongTitle());
            songData.addProperty("SongArtist", s.getSongArtist());
            songData.addProperty("MusicFile", s.getMusicFile());
            songsArray.add(songData);
        }
        songsToAdd = new ArrayList<Songs>();
        data.addProperty("success", success);
        data.add("Songs", songsArray);
        responseObject.add("data", data);
        stringifiedResponse = responseObject.toString();
        System.out.println("PlaylistJSON Returning\n" + stringifiedResponse);
        return stringifiedResponse;
    }
    
    //Method that returns a JSON String
    public String deletePlaylist(String playlistName, String userName){
        UserPlaylists usersPlaylists = getUsersPlaylists(userName);
        ArrayList<Playlists> usersListOfPlaylists = usersPlaylists.getPlaylists();
        ArrayList<UserPlaylists> userPlaylistArrList = getUserPlaylistsArrayList();
        
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("eventListenerName", "message-deletePlaylist");
        
        JsonObject data = new JsonObject();
        String stringifiedResponse = "";
        
        boolean isSuccessfulDeletion = false;
        Iterator itr = usersListOfPlaylists.iterator();
        Playlists removedPlaylist;
        while(itr.hasNext()){
            Playlists p = (Playlists) itr.next();
            if(p.getPlaylistTitle().equalsIgnoreCase(playlistName)){
                removedPlaylist = p;
                itr.remove();
                usersPlaylists.setPlaylists(usersListOfPlaylists);
                for(int i = 0; i < userPlaylistArrList.size(); i++){
                    if(userPlaylistArrList.get(i).getUserName().equalsIgnoreCase(userName)){
                        userPlaylistArrList.set(i, usersPlaylists);
                    }
                }
//                try(Writer w = new FileWriter(playlistPath)) {
//                    Gson gsonWriter = new GsonBuilder().setPrettyPrinting().create();
//                    gsonWriter.toJson(userPlaylistArrList, w);
//                    isSuccessfulDeletion = true;
//                    
//                    data.addProperty("success", isSuccessfulDeletion);
//                    data.addProperty("UserName", userName);
//                    data.addProperty("DeletedPlaylist", removedPlaylist.getPlaylistTitle());
//                    responseObject.add("data", data);
//                    stringifiedResponse = responseObject.toString();
//                }
//                catch(IOException e){
//                    e.printStackTrace();
//                    isSuccessfulDeletion = false;
//                    data.addProperty("success", isSuccessfulDeletion);
//                    responseObject.add("data", data);
//                    stringifiedResponse = responseObject.toString();
//                }
              Gson gson = new Gson();
              try {
				  pathHolder.writeFile(pathHolder.playlistFile,gson.toJson(userPlaylistArrList));
			  } catch (Exception e) {
				  e.printStackTrace();
			  }
              isSuccessfulDeletion = true;
              data.addProperty("success", isSuccessfulDeletion);
              data.addProperty("UserName", userName);
              data.addProperty("DeletedPlaylist", removedPlaylist.getPlaylistTitle());
              responseObject.add("data", data);
              stringifiedResponse = responseObject.toString();

            }
            else{
                isSuccessfulDeletion = false;
                data.addProperty("success", isSuccessfulDeletion);
                responseObject.add("data", data);
                stringifiedResponse = responseObject.toString();
                
            }
        }
        return stringifiedResponse;
    }
    
    public String getUsersPlaylistsTitles(String userName){
        UserPlaylists usersPlaylists = null;
        ArrayList<UserPlaylists> userPlaylistList = new ArrayList<UserPlaylists>();
        ArrayList<Playlists> playlists = new ArrayList<Playlists>();
        String stringifiedResponse = "";
        
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("eventListenerName", "message-getPlaylist");
        JsonObject data = new JsonObject();
        
        boolean success = false;
        
        try{
            userPlaylistList = getUserPlaylistsArrayList();
            for (UserPlaylists up : userPlaylistList) {
                if(up.getUserName().equalsIgnoreCase(userName)){
                    usersPlaylists = up;
                    playlists = usersPlaylists.getPlaylists();
                }
            }
            JsonArray jsonArr = new JsonArray();
            for (Playlists p : playlists){
                jsonArr.add(p.getPlaylistTitle());
            }
            success = true;
            data.addProperty("success", success);
            data.add("Playlists", jsonArr);
            responseObject.add("data", data);
            stringifiedResponse = responseObject.toString();
            return stringifiedResponse;
        }catch(Exception e){
            e.printStackTrace();
            success = false;
            data.addProperty("success", success);
            responseObject.add("data", data);
            stringifiedResponse = responseObject.toString();
            return stringifiedResponse;
        }
    }
    
    //Method that returns a JSON string
    public String addSongsToPlaylistDashboard(Songs song, String userName, Playlists affectedPlaylist){
        boolean isSuccessfulAddition = false;
        UserPlaylists usersPlaylist = getUsersPlaylists(userName);
        ArrayList<Playlists> usersListOfPlaylists = usersPlaylist.getPlaylists();
        
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("eventListenerName", "message-addSongOnDashboard");
        
        JsonObject data = new JsonObject();
        String stringifiedResponse = "";
        
        for(Playlists p : usersListOfPlaylists){
            if(p.getPlaylistTitle().equalsIgnoreCase(affectedPlaylist.getPlaylistTitle())){
                currentPlaylist = p;
                currentPlaylist.getPlaylistsSongs().add(song);
                ArrayList<UserPlaylists> userPlaylistArrList = getUserPlaylistsArrayList();
                
//                try(Writer w = new FileWriter(playlistPath)) {
//                Gson gsonWriter = new GsonBuilder().setPrettyPrinting().create();
//                gsonWriter.toJson(userPlaylistArrList, w);
//                isSuccessfulAddition = true;
//                
//                JsonObject songData = new JsonObject();
//                songData.addProperty("SongTitle", song.getSongTitle());
//                songData.addProperty("SongArtist", song.getSongArtist());
//                songData.addProperty("MusicFile", song.getMusicFile());
//
//                data.addProperty("success", isSuccessfulAddition);
//                data.addProperty("UserName", userName);
//                data.addProperty("Playlist", affectedPlaylist.getPlaylistTitle());
//                data.add("Song", songData);
//                responseObject.add("data", data);
//                stringifiedResponse = responseObject.toString();
//                
//                }
//                catch(IOException e){
//                    e.printStackTrace();
//                    isSuccessfulAddition = false;
//                    
//                    data.addProperty("success", isSuccessfulAddition);
//                    responseObject.add("data", data);
//                    stringifiedResponse = responseObject.toString();
//                }
                
                
                Gson gson = new Gson();
                try {
  				  pathHolder.writeFile(pathHolder.playlistFile,gson.toJson(userPlaylistArrList));
  				  
  				  isSuccessfulAddition = true;
	              JsonObject songData = new JsonObject();
	              songData.addProperty("SongTitle", song.getSongTitle());
	              songData.addProperty("SongArtist", song.getSongArtist());
	              songData.addProperty("MusicFile", song.getMusicFile());
	
	              data.addProperty("success", isSuccessfulAddition);
	              data.addProperty("UserName", userName);
	              data.addProperty("Playlist", affectedPlaylist.getPlaylistTitle());
	              data.add("Song", songData);
	              responseObject.add("data", data);
	              stringifiedResponse = responseObject.toString();
  			    } catch (Exception e) {
  				  e.printStackTrace();
  			    }
                
                
            }
        }
        if(isSuccessfulAddition == false){
            data.addProperty("success", isSuccessfulAddition);
            responseObject.add("data", data);
            stringifiedResponse = responseObject.toString();
        }
        
        return stringifiedResponse;
    }
    
    public String saveSong(String eventListenerName, String musicFile, String artist, String songTitle) {
    	Songs s = new Songs(songTitle, artist, musicFile);
    	songsToAdd.add(s);
        JsonObject data = new JsonObject();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("eventListenerName", eventListenerName);
        data.addProperty("success", true);
        responseObject.add("data", data);
        String stringifiedResponse = responseObject.toString();
        return stringifiedResponse;
    }

    //Method that returns a JSON string
    public String savePlaylist(String userName, String existingTitle, String playlistTitle){
    	for(int i = 0; i < songsToAdd.size(); i++) {
    		System.out.println(i);
    	}
        boolean isSuccessfulCreation = false;
        boolean successfulTitle = false;
        UserPlaylists usersPlaylists = getUsersPlaylists(userName);
        ArrayList<Playlists> usersListOfPlaylists = usersPlaylists.getPlaylists();
        ArrayList<UserPlaylists> userPlaylistArrList = getUserPlaylistsArrayList();

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("eventListenerName", "message-savePlaylist");
        
        JsonObject data = new JsonObject();
        String stringifiedResponse = "";
        
        if(playlistTitle.equals("") || playlistTitle == null){
            successfulTitle = false;
            data.addProperty("success", true);
            data.addProperty("titlesuccess", successfulTitle);
            responseObject.add("data", data);
            stringifiedResponse = responseObject.toString();
        }
        else if(existingTitle.equals("") || existingTitle == null){
            for (Playlists p : usersListOfPlaylists){
                if(p.getPlaylistTitle().equalsIgnoreCase(playlistTitle)){
                    successfulTitle = false;
                    data.addProperty("success", true);
                    data.addProperty("titlesuccess", successfulTitle);
                    responseObject.add("data", data);
                    stringifiedResponse = responseObject.toString();
                    songsToAdd = new ArrayList<Songs>();
                    return stringifiedResponse;
                }
            }
            currentPlaylist = new Playlists(playlistTitle);
            currentPlaylist.setPlaylistsSongs(songsToAdd);
            usersListOfPlaylists.add(currentPlaylist);
//            songsToAdd = new ArrayList<Songs>();
            usersPlaylists.setPlaylists(usersListOfPlaylists);
            for(int i = 0; i < userPlaylistArrList.size(); i++){
                if(userPlaylistArrList.get(i).getUserName().equalsIgnoreCase(userName)){
                    userPlaylistArrList.set(i, usersPlaylists);
                }
            }
            isSuccessfulCreation = true;
            successfulTitle = true;
        }
        else if(existingTitle.equalsIgnoreCase(playlistTitle)){
            for (Playlists p : usersListOfPlaylists) {
                if(p.getPlaylistTitle().equalsIgnoreCase(playlistTitle)){
                    p.setPlaylistsSongs(songsToAdd);
//                    songsToAdd = new ArrayList<Songs>();
                    usersPlaylists.setPlaylists(usersListOfPlaylists);
                    for(int i = 0; i < userPlaylistArrList.size(); i++){
                        if(userPlaylistArrList.get(i).getUserName().equalsIgnoreCase(userName)){
                            userPlaylistArrList.set(i, usersPlaylists);
                        }
                    }
                    isSuccessfulCreation = true;
                    successfulTitle = true;
                }
            }
        }
        else{
            for (Playlists p : usersListOfPlaylists) {
                if(p.getPlaylistTitle().equalsIgnoreCase(playlistTitle)){
                    successfulTitle = false;
                    data.addProperty("success", true);
                    data.addProperty("titlesuccess", successfulTitle);
                    responseObject.add("data", data);
                    stringifiedResponse = responseObject.toString();
                    songsToAdd = new ArrayList<Songs>();
                    return stringifiedResponse;
                }
                else if(p.getPlaylistTitle().equalsIgnoreCase(existingTitle)){
                    p.setPlaylistTitle(playlistTitle);
                    currentPlaylist = p;
                    currentPlaylist.setPlaylistsSongs(songsToAdd);
                    usersPlaylists.setPlaylists(usersListOfPlaylists);
                    for(int i = 0; i < userPlaylistArrList.size(); i++){
                        if(userPlaylistArrList.get(i).getUserName().equalsIgnoreCase(userName)){
                            userPlaylistArrList.set(i, usersPlaylists);
                        }
                    }
                    isSuccessfulCreation = true;
                    successfulTitle = true;
                }
            }
        }
        if(isSuccessfulCreation == true){
        	Gson gson = new Gson();
            try {
//                Gson gsonWriter = new GsonBuilder().setPrettyPrinting().create();
//                gsonWriter.toJson(userPlaylistArrList, w);
                pathHolder.writeFile(pathHolder.playlistFile,gson.toJson(userPlaylistArrList));
                
                JsonArray songsArray = new JsonArray();
                for(Songs s : songsToAdd){
                    JsonObject songData = new JsonObject();
                    songData.addProperty("SongTitle", s.getSongTitle());
                    songData.addProperty("SongArtist", s.getSongArtist());
                    songData.addProperty("MusicFile", s.getMusicFile());
                    songsArray.add(songData);
                }
                for(int i = 0; i< songsArray.size(); i++) {
                    System.out.println(songsArray.get(i));
                }
                data.addProperty("success", isSuccessfulCreation);
                data.addProperty("titlesuccess", successfulTitle);
                data.addProperty("UserName", userName);
                data.addProperty("Playlist", currentPlaylist.getPlaylistTitle());
                data.add("Songs", songsArray);
                responseObject.add("data", data);
                stringifiedResponse = responseObject.toString();
                
                currentPlaylist = null;
            }
            catch(IOException e){
                e.printStackTrace();
                isSuccessfulCreation = false;
                
                data.addProperty("success", isSuccessfulCreation);
                responseObject.add("data", data);
                stringifiedResponse = responseObject.toString();
            } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        
        }
        songsToAdd = new ArrayList<Songs>();
        return stringifiedResponse;
    }


}