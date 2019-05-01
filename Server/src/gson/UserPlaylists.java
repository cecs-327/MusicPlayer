package gson;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.reflect.TypeToken;

import services.pathHolder;

import java.lang.reflect.Type;

import com.google.gson.Gson;

public class UserPlaylists {
    private String UserName;
    private ArrayList<Playlists> Playlists;
    public UserPlaylists(String UserName){
        this.UserName = UserName;
        Playlists = new ArrayList<Playlists>();
        getUserPlaylist();
    }

    public void getUserPlaylist() {
        List<UserPlaylists> UserPlaylist = new ArrayList<UserPlaylists>();
        
        String fPath = pathHolder.testPlaylists;
        try {
        	String userPlaylists = pathHolder.readFile(pathHolder.playlistFile);
            Type jsonListType = new TypeToken<ArrayList<UserPlaylists>>() {}.getType();
            UserPlaylist = new Gson().fromJson(userPlaylists, jsonListType);

            for(int i = 0; i < UserPlaylist.size(); i++) {
                if(UserPlaylist.get(i).getUserName() == UserName) {
                    this.Playlists = UserPlaylist.get(i).getPlaylists();
                }
            }
        }catch(FileNotFoundException e) {
            System.out.println(e);
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public String getUserName(){
        return UserName;
    }

    public ArrayList<Playlists> getPlaylists(){
        return Playlists;
    }
    
    public void setPlaylists(ArrayList<Playlists> listOfPlaylists){
        Playlists = listOfPlaylists;
    }
}