package services;

import java.io.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import gson.UserPlaylists;
import gson.Users;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RegisterServices{
	private String userFPath = pathHolder.testUsers;
	private String playlistPath = pathHolder.testPlaylists;
	
	public static List<Users> getUsers() {
		List<Users> userList = new ArrayList<Users>();
		JsonParser parser = new JsonParser();
		try {
			String users = pathHolder.readFile(pathHolder.userFile);
			JsonArray userFile = parser.parse(users).getAsJsonArray();
			for(int i = 0; i < userFile.size(); i++) {
				Users temp = new Users(userFile.get(i).getAsJsonObject().get("UserName").getAsString(), userFile.get(i).getAsJsonObject().get("Password").getAsString());
				userList.add(temp);
			}            
            return userList;
		}catch(Exception e) {
			System.out.println(e);
			return userList;
        }
	}
	
    public List<UserPlaylists> getUserPlaylists(){
        List<UserPlaylists> userPlaylistList = new ArrayList<UserPlaylists>();
        JsonParser parser = new JsonParser();
        try{
            String UserPlaylists = pathHolder.readFile(pathHolder.playlistFile);
            JsonArray playlistsFile = parser.parse(UserPlaylists).getAsJsonArray();
            for(int i = 0; i < playlistsFile.size(); i++){
                UserPlaylists temp = new UserPlaylists(playlistsFile.get(i).getAsJsonObject().get("UserName").getAsString());
                userPlaylistList.add(temp);
            }
            return userPlaylistList;
        }catch(Exception e){
            e.printStackTrace();
            return userPlaylistList;
        }
    }

    public boolean isUniqueUser(List<Users> userList, String username){
        boolean isUnique = true;
        for(int i = 0; i < userList.size(); i++){
            if(userList.get(i).getUsername().equals(username)){
                isUnique = false;
            }
        }
        return isUnique;
    }
    public void initiateNewUserPlaylist(String username){
    	Gson gson = new Gson();
    	
        List<UserPlaylists> userPlaylistList = getUserPlaylists();
        UserPlaylists newPlaylistList = new UserPlaylists(username);
        userPlaylistList.add(newPlaylistList);
        try {
        	pathHolder.writeFile(pathHolder.playlistFile,gson.toJson(userPlaylistList));
        } catch(Exception e) {
        	System.out.println(e);
        }
    }
    public String registerUser(String username, String password){
        boolean successfulRegister = false;
        List<Users> userList = getUsers();
        boolean isUnique = isUniqueUser(userList, username);
        
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("eventListenerName", "message-registration");
        
        JsonObject data = new JsonObject();
        String stringifiedResponse;
        if(isUnique == false){
            successfulRegister = false;
            data.addProperty("success", successfulRegister);
            responseObject.add("data", data);
            stringifiedResponse = responseObject.toString();
        }
        else{
            Users newUser = new Users(username, password);
            userList.add(newUser);
            try {
            	Gson gson = new Gson();
            	pathHolder.writeFile(pathHolder.userFile, gson.toJson(userList));
            	successfulRegister = true;
                data.addProperty("success", successfulRegister);
                data.addProperty("UserName", username);
                data.addProperty("Password", password);
                responseObject.add("data", data);
                stringifiedResponse = responseObject.toString();
            }
            catch(Exception e){
                e.printStackTrace();
                successfulRegister = false;
                data.addProperty("success", successfulRegister);
                responseObject.add("data", data);
                stringifiedResponse = responseObject.toString();
            }
           initiateNewUserPlaylist(username);
        }
        return stringifiedResponse;
    }
}
