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
			String users = pathHolder.readFile("user");
			System.out.println("This is a string user: " + users);
			JsonArray userFile = parser.parse(users).getAsJsonArray();
			for(int i = 0; i < userFile.size(); i++) {
				Users temp = new Users(userFile.get(i).getAsJsonObject().get("UserName").getAsString(), userFile.get(i).getAsJsonObject().get("Password").getAsString());
				System.out.println(temp.getUsername() + " " + temp.getPassword());
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
            String UserPlaylists = pathHolder.readFile("playlist");
            System.out.println("This is a string user playlists: " + UserPlaylists);
            JsonArray playlistsFile = parser.parse(UserPlaylists).getAsJsonArray();
            for(int i = 0; i < playlistsFile.size(); i++){
                UserPlaylists temp = new UserPlaylists(playlistsFile.get(i).getAsJsonObject().get("UserName").getAsString());
                System.out.println("User: " + temp.getUserName());
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
        List<UserPlaylists> userPlaylistList = getUserPlaylists();
        UserPlaylists newPlaylistList = new UserPlaylists(username);
        userPlaylistList.add(newPlaylistList);
        try(Writer w = new FileWriter(playlistPath)) {
            Gson gsonWriter = new GsonBuilder().setPrettyPrinting().create();
            gsonWriter.toJson(userPlaylistList, w);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    public String registerUser(String username, String password){
        boolean successfulRegister = false;
        List<Users> userList = getUsers();
        for(int i = 0; i < userList.size(); i++) {
        	System.out.print(userList.get(i) + " ");
        }
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
            try(Writer w = new FileWriter(userFPath)) {
                Gson gsonWriter = new GsonBuilder().setPrettyPrinting().create();
                gsonWriter.toJson(userList, w);
                successfulRegister = true;
                
                data.addProperty("success", successfulRegister);
                data.addProperty("UserName", username);
                data.addProperty("Password", password);
                responseObject.add("data", data);
                stringifiedResponse = responseObject.toString();
            }
            catch(IOException e){
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
