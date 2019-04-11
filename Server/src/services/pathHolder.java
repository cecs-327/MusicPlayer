package services;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
	  dfs = new DFS(pathHolder.portNum);
	  dfs.join("127.0.0.1", pathHolder.portToJoin);
	  Scanner scan = new Scanner(System.in);
	  String response = scan.next();
	  if(response.equals("read"))
		  writeFile("playlist", "[user1: {playlistTitle: []}]");
  }
}
