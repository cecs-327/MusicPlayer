package services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

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
	  FilesJson filesJson = dfs.readMetaData();
	  String fileInfo = "";
	  int numOfPages = 0;
	  RemoteInputFileStream rfs;
	  
	  System.out.println("Files Json size:" + filesJson.getSize());
	  for(int i = 0; i < filesJson.getSize(); i++){
          if(filesJson.getFileJson(i).getName().equalsIgnoreCase(fileName)){
        	  System.out.println("Found file name");
              numOfPages = filesJson.getFileJson(i).getNumOfPages();
          }
      }
	  
	  for(int pageNum = 1; pageNum <= numOfPages; pageNum++)
	  {
		  System.out.println("Going through pages");
		  rfs = dfs.read(fileName, pageNum);
          rfs.connect();
          int i;
          while((i = rfs.read()) != -1){
              fileInfo += ((char) i);
              System.out.println(fileInfo);
          }
	  }
	  return fileInfo;
  }
	 
  public static void main(String[] args) throws Exception
  {
	  dfs = new DFS(pathHolder.portNum);
	  dfs.join("127.0.0.1", pathHolder.portToJoin);
	  Scanner scan = new Scanner(System.in);
	  String response = scan.next();
	  if(response.equals("read"))
		  System.out.println("Result: " + readFile("playlist"));
  }
}
