package services;

import java.io.BufferedWriter;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import gson.*;
import dfs.*;
import dfs.DFS.FilesJson;

public class pathHolder {

	public static String userDir = System.getProperty("user.dir");
	public static String testUsers = userDir + "/src/testUsers.json";
	public static String testPlaylists = userDir + "/src/testplaylists.json";
	public static String mp3Directory = userDir + "/src/mp3/";
	public static String dataDirectory = userDir + "/src/data/";
	public static String songPath = userDir + "/src/data/musicComplete.json";
	public static DFS dfs;
	public static int portNum = 12345;
	public static int portToJoin = 1234;
	public static String songFile = "songs";
	public static String userFile = "user";
	public static String playlistFile = "playlist";

  public static String readFile(String fileName) throws Exception {
	  FilesJson filesJson = dfs.readMetaData();
	  StringBuilder fileInfo = new StringBuilder();
	  int numOfPages = getNumberOfPages(fileName);
	  RemoteInputFileStream rfs;
	  Gson gson = new Gson();
	  
	  for(int pageNum = 1; pageNum <= numOfPages; pageNum++)
	  {
		  rfs = dfs.read(fileName, pageNum);
          rfs.connect();
          int i;
          while((i = rfs.read()) != -1){
        	  fileInfo.append((char) i);
          }
	  }
	  return fileInfo.toString();
  }
  
  public static RemoteInputFileStream getInputStream(String fileName, int pageNum) throws Exception{
	  FilesJson filesJson = dfs.readMetaData();
	  int numOfPages = getNumberOfPages(fileName);
	  
	  if(pageNum >= numOfPages)
		  return null;
	  return dfs.read(fileName, pageNum);
  }
  
  public static void initalizeFreshDFS() throws Exception
  {
//	  dfs = new DFS(portToJoin);
	  dfs = new DFS(portNum);
	  dfs.join("127.0.0.1", portToJoin);
	  dfs.create(userFile);
	  dfs.create(playlistFile);
	  dfs.create(songFile);
	  RemoteInputFileStream input = new RemoteInputFileStream(testUsers);
	  dfs.append(userFile, input);
	  RemoteInputFileStream input2 = new RemoteInputFileStream(testPlaylists);
	  dfs.append(playlistFile, input2);
	  RemoteInputFileStream input3 = new RemoteInputFileStream(songPath);
	  dfs.append(songFile, input3);
  }
  
  public static void initalizeDFS() throws Exception
  {
	  dfs = new DFS(portNum);
	  dfs.join("127.0.0.1", portToJoin);
  }
  
  public static int getNumberOfPages(String fileName) throws Exception {
	  FilesJson filesJson = dfs.readMetaData();
	  int numOfPages = 0;
	  for(int i = 0; i < filesJson.getSize(); i++){
          if(filesJson.getFileJson(i).getName().equalsIgnoreCase(fileName)){
              numOfPages = filesJson.getFileJson(i).getNumOfPages();
          }
      }
	  return numOfPages;
  }
  
  public static void writeFile(String fileName, String fileInfo) throws Exception 
  {
	  dfs.delete(fileName);
	  dfs.create(fileName);
	  File tempFile = File.createTempFile("tempfile", ".json");
	  BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile.getAbsolutePath()));
	  writer.write(fileInfo);
	  writer.close();
	  RemoteInputFileStream input = new RemoteInputFileStream(tempFile.getAbsolutePath());
	  dfs.append(fileName, input);
	  tempFile.deleteOnExit();
  }
	 
  public static void main(String[] args) throws Exception
  {
	  System.out.println("Starting initalization");
	  initalizeDFS();
	  System.out.println("Initalization Finished");
	  Scanner scan = new Scanner(System.in);
	  String response = scan.next();
	  
	  
	  if(response.equals("cont")) {
		  System.out.println(readFile(songFile));
		  
	  }
	  scan.close();
  }
}
