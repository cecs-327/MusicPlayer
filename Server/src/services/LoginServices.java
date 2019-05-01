package services;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import dfs.DFS;
import gson.UserPlaylists;
import gson.Users;

import java.lang.reflect.Type;

public class LoginServices {
	
	private String eventListenerName;
	private UserPlaylists data;
	private boolean success;
	
	public LoginServices() {
		eventListenerName = "message-login";
	}
	
	public List<Users> getUsers() {
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
	
	public String Login(String username, String password) {
		List<Users> userList = getUsers();
		Gson g = new Gson();
		String JSONoutput = new String();
        
		if(username.equals(null) || password.equals(null) || username == "" || password == "") {
			success = false;
			return JSONoutput;
		} else {
			for(int i = 0; i < userList.size(); i++) {
				if((userList.get(i).getUsername().equals(username)) && (userList.get(i).getPassword().equals(password))) {
					data = new UserPlaylists(username);
					success = true;
					JSONoutput = g.toJson(this);
					// JSONoutput = "{\"eventListenerName\":\"" + eventListenerName + "\",\"data\": " + "\"success\":true,\"username\":\"" + username + "\",\"password\":\"" + password + "\",";
					return JSONoutput;
				}
			}
		}
		success = false;
		JSONoutput = g.toJson(this);		
		
		return JSONoutput;
	}
	 
}