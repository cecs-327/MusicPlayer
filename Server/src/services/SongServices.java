package services;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.util.Base64;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

public class SongServices {

    private String eventListenerName;
    private String data;
    private boolean finished;
    private boolean success;
    private String fragment;
    static final int FRAGMENT_SIZE = 8192;
    
    public SongServices() {
        eventListenerName = "message-SongChunk";
        data = new String();
        
    }

    public void setData(String data, boolean finished, boolean success, String fragment) {
        this.data = data;
        this.finished = finished;
        this.success = success;
        this.fragment = fragment;
    } 

    public String getSongChunck(String musicFileName, String frag) throws FileNotFoundException, IOException {
    	System.out.println("made it here");
    	int fragment = Integer.parseInt(frag);
        Gson g = new Gson();
    	byte buf[] = new byte[FRAGMENT_SIZE];
    	File songFile = new File(pathHolder.mp3Directory + musicFileName);
    	FileInputStream inputStream = new FileInputStream(songFile);
    	if(fragment * FRAGMENT_SIZE > songFile.length()) {
    		inputStream.close();
    		this.setData("Done", true, true, Integer.toString(fragment+1));
    		return g.toJson(this);
    	}
        inputStream.skip(fragment * FRAGMENT_SIZE);
        inputStream.read(buf);
        inputStream.close();
        BufferedWriter writer = new BufferedWriter(new FileWriter("./music.txt", true));
        if(fragment+1 * FRAGMENT_SIZE > songFile.length())
        	System.out.println(Base64.getEncoder().encodeToString(buf));
        writer.append(Base64.getEncoder().encodeToString(buf));
        writer.close();
        this.setData(Base64.getEncoder().encodeToString(buf), false, true, Integer.toString(fragment+1));
        return g.toJson(this);
    }

//    public String getSongChunk(String musicFileName, String frag) throws FileNotFoundException, IOException
//    {
//    	int fragment = Integer.parseInt(frag);
//    	Gson g = new Gson();
//        byte buf[] = new byte[FRAGMENT_SIZE];
//
//        File file = new File(pathHolder.mp3Directory + musicFileName);
//        FileInputStream inputStream = new FileInputStream(file);
//        inputStream.skip(fragment * FRAGMENT_SIZE);
//        inputStream.read(buf);
//        inputStream.close(); 
//        // Encode in base64 so it can be transmitted 
//        this.setData(Base64.getEncoder().encodeToString(buf), false, true, Integer.toString(fragment+1));
//        return g.toJson(this);
//    }
    
    /* 
    * getFileSize: Gets a size of the file
    * @param key: Song ID. Each song has a unique ID 
     */
    public Integer getFileSize(Long key) throws FileNotFoundException, IOException
    {
        File file = new File("./" + key);        
        Integer total =  (int)file.length();
        
        return total;
    }
    
    public int getFileSize(String musicFileName) {
        File songFile = new File(pathHolder.mp3Directory + musicFileName);
        Integer total = (int)songFile.length();

        return total;
    }
}