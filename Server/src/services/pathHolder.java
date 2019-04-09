package services;

import java.time.LocalDateTime;
import java.util.ArrayList;

import dfs.*;
import dfs.DFS.FileJson;
import dfs.DFS.PagesJson;
import dfs.DFS.*;

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

  public static String readFile(String fileName) throws Exception {
	  FilesJson filesJson = dfs.getFilesJson();
	  String fileInfo = "";
	  int numOfPages = 0;
	  RemoteInputFileStream rfs;
	  
	  for(int i = 0; i < filesJson.getSize(); i++){
          if(filesJson.getFileJson(i).getName().equalsIgnoreCase(fileName)){
              numOfPages = filesJson.getFileJson(i).getNumOfPages();
          }
      }
	  
	  for(int pageNum = 1; pageNum <= numOfPages; pageNum++)
	  {
		  rfs = dfs.read(fileName, pageNum);
          rfs.connect();
          int i;
          while((i = rfs.read()) != -1){
              fileInfo += ((char) i);
          }
	  }
	  return fileInfo;
  }
	 

}
