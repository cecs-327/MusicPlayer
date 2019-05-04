package dfs;

import java.rmi.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.math.BigInteger;
import java.security.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import dfs.FileMapObject.Page;

import java.util.*;
import java.time.LocalDateTime;

/* JSON Format

{"file":
  [
     {"name":"MyFile",
      "size":128000000,
      "pages":
      [
         {
            "guid":11,
            "size":64000000
         },
         {
            "guid":13,
            "size":64000000
         }
      ]
      }
   ]
}
*/

public class DFS {
	public class PagesJson {
		Long guid;
		Long size;
		String creationTS;
		String readTS;
		String writeTS;
		int referenceCount;
		

		public PagesJson(Long guid, Long size, String creationTS, String readTS, String writeTS, int referenceCount) {

			this.guid = guid;
			this.size = size;
			this.creationTS = creationTS;
			this.readTS = readTS;
			this.writeTS = writeTS;
			this.referenceCount = referenceCount;
		}

		// getters
		public Long getSize() {
			return this.size;
		}

		public Long getGuid() {
			return this.guid;
		}

		public String getCreationTS() {
			return this.creationTS;
		}

		public int getReferenceCount() {
			return this.referenceCount;
		}

		// setters
		public void setSize(Long size) {
			this.size = size;
		}

		public void setGuid(Long guid) {
			this.guid = guid;
		}

		public void setCreationTS(String creationTS) {
			this.creationTS = creationTS;
		}

		public void setReadTS(String readTS) {
			this.readTS = readTS;
		}

		public void setWriteTS(String writeTS) {
			this.writeTS = writeTS;
		}

		public void setReferenceCount(int referenceCount) {
			this.referenceCount = referenceCount;
		}
	};

	public class FileJson {
		String name;
		Long size;
		String creationTS;
		String readTS;
		String writeTS;
		int referenceCount;
		int numOfPages;
		int maxPageSize;
		ArrayList<PagesJson> pages;

		public FileJson() {
			this.size = new Long(0);
			this.numOfPages = 0;
			this.referenceCount = 0;
			// this.creationTS = new Long(date.getTime());
			this.creationTS = LocalDateTime.now().toString();
			this.readTS = "0";
			this.writeTS = "0";
			this.maxPageSize = 0;
			this.pages = new ArrayList<PagesJson>();
		}
		// dealing with pages

		public void addPageInfo(Long guid, Long size, String creationTS, String readTS, String writeTS,
				int referenceCount) {
			PagesJson page = new PagesJson(guid, size, creationTS, readTS, writeTS, referenceCount);
			pages.add(page);
		}

		// getters
		public int getMaxPageSize() {
			return this.maxPageSize;
		}

		public int getReferenceCount() {
			return this.referenceCount;
		}

		public int getNumOfPages() {
			return this.numOfPages;
		}

		public Long getSize() {
			return this.size;
		}

		public ArrayList<PagesJson> getPages() {
			return pages;
		}

		public String getCreationTS() {
			return creationTS;
		}

		public String getName() {
			return this.name;
		}

		// setters
		public void setMaxPageSize(int maxPageSize) {
			this.maxPageSize = maxPageSize;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setSize(Long size) {
			this.size = size;
		}

		public void addSize(Long size) {
			this.size += size;
		}

		public void setReferenceCount(int referenceCount) {
			this.referenceCount = referenceCount;
		}

		public void setNumOfPages(int numOfPages) {
			this.numOfPages = numOfPages;
		}

		public void addNumOfPages(int numOfPages) {
			this.numOfPages += numOfPages;
		}

		public void setCreationTS(String time) {
			this.creationTS = time;
		}

		public void setReadTS(String time) {
			this.readTS = time;
		}

		public void setWriteTS(String time) {
			this.writeTS = time;
		}

	};

	public class FilesJson {
		List<FileJson> file;

		public FilesJson() {
			file = new ArrayList<FileJson>();

		}

		// getters
		public FileJson getFileJson(int index) {
			return file.get(index);
		}

		public int getSize() {
			return file.size();
		}

		// setters
		public void addFile(FileJson fileJson) {
			file.add(fileJson);

		}
	};

	int port;
	Chord chord;
	FilesJson filesJson;

//l
	private long md5(String objectName) {
		try {
			MessageDigest m = MessageDigest.getInstance("MD5");
			m.reset();
			m.update(objectName.getBytes());
			BigInteger bigInt = new BigInteger(1, m.digest());
			return Math.abs(bigInt.longValue());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();

		}
		return 0;
	}

	public DFS(int port) throws Exception {

		this.port = port;
		long guid = md5("" + port);
		this.filesJson = new FilesJson();
		chord = new Chord(port, guid);
		Files.createDirectories(Paths.get(guid + "/repository"));
		Files.createDirectories(Paths.get(guid + "/tmp"));
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				chord.leave();
			}
		});

	}
	/**
	 * Join the chord
	 *
	 */
	public void join(String Ip, int port) throws Exception {
		chord.joinRing(Ip, port);
		chord.print();
	}

	/**
	 * leave the chord
	 *
	 */
	public void leave() throws Exception {
		chord.leave();
	}

	/**
	 * print the status of the peer in the chord
	 *
	 */
	public void print() throws Exception {
		chord.print();
	}

	/**
	 * readMetaData read the metadata from the chord
	 *
	 */
	public FilesJson readMetaData() throws Exception {
		FilesJson filesJson = null;
		try {
			Gson gson = new Gson();
			long guid = md5("Metadata");
			ChordMessageInterface peer = chord.locateSuccessor(guid);
			RemoteInputFileStream metadataraw = peer.get(guid);
			metadataraw.connect();
			Scanner scan = new Scanner(metadataraw);
			scan.useDelimiter("\\A");
			String strMetaData = scan.next();
			filesJson = gson.fromJson(strMetaData, FilesJson.class);
		} catch (NoSuchElementException ex) {
			filesJson = new FilesJson();
		}
		return filesJson;
	}

	/**
	 * writeMetaData write the metadata back to the chord
	 *
	 */
	public void writeMetaData(FilesJson filesJson) throws Exception {

		long guid = md5("Metadata");
		ChordMessageInterface peer = chord.locateSuccessor(guid);

		Gson gson = new Gson();
		peer.put(guid, gson.toJson(filesJson));
	}

	/**
	 * Change Name
	 *
	 */
	public void move(String oldName, String newName) throws Exception {
		filesJson = readMetaData();
		boolean found = false;
		for (int i = 0; i < filesJson.getSize(); i++) {
			if (filesJson.getFileJson(i).getName().equalsIgnoreCase(oldName)) {
				filesJson.getFileJson(i).setName(newName);
				String timeOfWrite = LocalDateTime.now().toString();
				filesJson.getFileJson(i).setWriteTS(timeOfWrite);
				writeMetaData(filesJson);
				found = true;
			}
		}
		if (found)
			System.out.println(oldName + " has been rewritten to " + newName);
		else
			System.out.println(oldName + " not found");
	}

	/**
	 * List the files in the system
	 *
	 * @param filename Name of the file
	 */
	public String lists() throws Exception {
		filesJson = readMetaData();
		String listOfFiles = "";
		for (int i = 0; i < filesJson.getSize(); i++) {
			String filename = filesJson.getFileJson(i).getName();
			listOfFiles = listOfFiles + " " + filename + "\n";
		}
		System.out.println(listOfFiles);
		return listOfFiles;
	}

	/**
	 * create an empty file
	 *
	 * @param filename Name of the file
	 */
	public void create(String fileName) throws Exception {
		filesJson = readMetaData();
		FileJson fileJson = new FileJson();
		fileJson.setName(fileName);
		filesJson.addFile(fileJson);
		writeMetaData(filesJson);

	}

	/**
	 * delete file
	 *
	 * @param filename Name of the file
	 */
	public void delete(String fileName) throws Exception {
		filesJson = readMetaData();
		for (int i = 0; i < filesJson.getSize(); i++) {

			if (filesJson.getFileJson(i).getName().equalsIgnoreCase(fileName)) {

				// get metadata information
				long guidPort = md5("" + port);

				// get list of pages from the file
				ArrayList<PagesJson> pagesList = filesJson.getFileJson(i).getPages();

				// iterate through all pages of the file
				for (int k = 0; k < pagesList.size(); k++) {

					// get the specified page info from file
					PagesJson pagesRead = pagesList.get(k);
					long pageGuid = pagesRead.getGuid();

					// remove JSONFile from files

					// remove appended file in directory
					String userDir = System.getProperty("user.dir");
					File file = new File(userDir + "/" + guidPort + "/repository/" + pageGuid);
					file.delete();

					// remove from chord
					ChordMessageInterface peer = chord.locateSuccessor(pageGuid);
					peer.delete(pageGuid);
				}

				filesJson.file.remove(filesJson.getFileJson(i));
				writeMetaData(filesJson);

			}
		}
	}

	/**
	 * Read block pageNumber of fileName
	 *
	 * @param filename   Name of the file
	 * @param pageNumber number of block.
	 */
	public RemoteInputFileStream read(String fileName, int pageNumber) throws Exception {
		// List of pages starts at 0 so decrement it
		filesJson = readMetaData();
		pageNumber--;
		RemoteInputFileStream rifs = null;
		for (int i = 0; i < filesJson.getSize(); i++) {
			if (filesJson.getFileJson(i).getName().equalsIgnoreCase(fileName)) {
				ArrayList<PagesJson> pagesList = filesJson.getFileJson(i).getPages();
				for (int k = 0; k < pagesList.size(); k++) {
					if (k == pageNumber) {
						PagesJson pageToRead = pagesList.get(k);
						String timeOfRead = LocalDateTime.now().toString();
						pageToRead.setReadTS(timeOfRead);
						filesJson.getFileJson(i).setReadTS(timeOfRead);
						Long pageGUID = md5(fileName + pageToRead.getCreationTS());
						// System.out.println("Read pageGUID: " + pageGUID);
						ChordMessageInterface peer = chord.locateSuccessor(pageGUID);
						rifs = peer.get(pageGUID);
					}
				}
				writeMetaData(filesJson);
			}
		}
		return rifs;
	}

	public RemoteInputFileStream tail(String fileName) throws Exception {
		filesJson = readMetaData();
		RemoteInputFileStream tail = null;
		for (int i = 0; i < filesJson.getSize(); i++) {
			if (filesJson.getFileJson(i).getName().equalsIgnoreCase(fileName)) {
				ArrayList<PagesJson> pagesList = filesJson.getFileJson(i).getPages();
				int last = pagesList.size() - 1;
				PagesJson pageToRead = pagesList.get(last);
				String timeOfRead = LocalDateTime.now().toString();
				pageToRead.setReadTS(timeOfRead);
				filesJson.getFileJson(i).setReadTS(timeOfRead);
				Long pageGUID = md5(fileName + pageToRead.getCreationTS());
				ChordMessageInterface peer = chord.locateSuccessor(pageGUID);
				tail = peer.get(pageGUID);
				writeMetaData(filesJson);
			}
		}
		return tail;
	}

	/**
	 * Add a page to the file
	 *
	 * @param filename Name of the file
	 * @param data     RemoteInputStream.
	 */

	public void append(String filename, RemoteInputFileStream data) throws Exception {
		filesJson = readMetaData();
		for (int i = 0; i < filesJson.getSize(); i++) {
			// append the page to the file specified by the user
			if (filesJson.getFileJson(i).getName().equalsIgnoreCase(filename)) {

				// update information in the file we are going to append
				// data.connect();
				// This is used to get the size of the file
				Long sizeOfFile = new Long(data.available());
				String timeOfAppend = LocalDateTime.now().toString();
				filesJson.getFileJson(i).setWriteTS(timeOfAppend);
				filesJson.getFileJson(i).addNumOfPages(1);
				filesJson.getFileJson(i).addSize(sizeOfFile);

				// create the page metadata information
				String objectName = filename + timeOfAppend;
				Long guid = md5(objectName);
				
				ChordMessageInterface peer = chord.locateSuccessor(guid);
				peer.put(guid, data);
				// chord locate successor , then put

				// filesJson.getFileJson(i).addPageInfo(guid, size, creationTS, readTS, writeTs,
				// referenceCount);
				Long defaultZero = new Long(0);
				filesJson.getFileJson(i).addPageInfo(guid, sizeOfFile, timeOfAppend, "0", "0", 0);

			}

		}
		writeMetaData(filesJson);

	}
	
	int fileInputCounter;
	FileMapObject fileMapObject;
	
	public void runMapReduce(String fileInput, String fileOutput) throws Exception {
		fileInputCounter = 0;
	    Mapper mapreducer = new Mapper();

		filesJson = readMetaData();
		
	    chord.successor.onChordSize(chord.successor.getId(), 1); // Obtain the number of nodes currently active

	    while(chord.size == 0) {
	    	Thread.sleep(10);
	    }
	    
	    int size = chord.size;
	    int interval = 1444 / size; // Hard value comes from 38 * 38 from the index table size
	    
    	/**
    	 * Creates a blank file with enough pages to hold all information for the interval
    	 * Creating FileMap class
    	 */
    	createFile(fileOutput + ".map", interval, size);
    	// mapreducer is an instance of the class that
      	// implements MapReduceInterface
      	//for each page in fileInput
    	for (int i = 0; i < filesJson.getSize(); i++) {
    		if (filesJson.getFileJson(i).getName().equalsIgnoreCase(fileInput)) {
    			ArrayList<PagesJson> inputList = filesJson.getFileJson(i).getPages(); //music.json getting all pages
  				
  				//iterate through pages of fileinput 
  				for (int j = 0; j < inputList.size(); j++) { //going through pages in music.json
  					
  					PagesJson page = inputList.get(j);

  					fileInputCounter++;
  			    	ChordMessageInterface peer = chord.locateSuccessor(page.guid); //finds the chord which holds the page
  			    	//Just music.json file to parse as mapContext
  			    	peer.mapContext(page.guid, mapreducer, this, fileOutput + ".map");
  				}
  	
	
  			}
      	}
	    	
			
			//while page ==0 set timer/ sleep thread.sleep for 10 milliseconds
	    	//while counter ==0 then sleep
	    /**
	     * To wait for all coordinators to say that they are okay!
	     */
	    while (fileInputCounter > 0)
	    {
	    	Thread.sleep(10);
	    }

	    /**
	     * TODO
	     * 
	     * This section should create a file in a normal using the create(fileOutput)
	     * 
	     * Section should then loop through all pages of the fileMapObject and append a page to the newly create fileOutput for each page
	     * in the fileMapObject, each page should contain the TreeMap data from the page it was created from.
	     * 
	     */
    	create(fileOutput);
    	for(Page page : fileMapObject.getPages()) {
    		fileInputCounter++;
    		reduceContext(page.getId(), mapreducer);
    	}
    	//    for each page in fileOutput + ".map"{
		    for (int i = 0; i < filesJson.getSize(); i++) {
		    	if (filesJson.getFileJson(i).getName().equalsIgnoreCase(fileOutput + ".map")) {
		    		ArrayList<PagesJson> pages = filesJson.getFileJson(i).getPages();
		    		for(int b =0; b <pages.size();b++) {
			    		PagesJson page = pages.get(b);
			       		fileInputCounter++;
			    		ChordMessageInterface peer = chord.locateSuccessor(page.guid);
			    		/**
			    		 * At this point the data in the page is already trimmed and sorted, just needs to be resorted based on hotness
			    		 * Should allow for code reuse of previous functions
			    		 */
			    		peer.reduceContext(page.guid, mapreducer, this, fileOutput);
		    		}
		    	}
		    }
	    
	    while(fileInputCounter > 0)
	    {
        	Thread.sleep(10);
	    }
	    
//	    bulkTree(fileOutput);
	    
	}

	private void createFile(String fileOutput, int interval, int size) throws Exception { // Helper function
		
		int lower = 0;
		fileMapObject = new FileMapObject(fileOutput);
		for (int i = 0; i <= size - 1; i++) {
			long pageId = md5(fileOutput + i);
			//Each page should hold an interval
			String lowerBoundInterval = Double.toString((Math.floor(lower / 38))) + Double.toString((lower % 38));
			// appendEmptyPage needs to be created
			fileMapObject.appendEmptyPage(Long.toString(pageId), lowerBoundInterval);
			lower += interval;
		}
		
	}

	public void onPageCompleted() {
		fileInputCounter--;
	}
	
	public void reduceContext(String pageId, Mapper mapreducer) {
		
	}
	
//	private void bulkTree(String fileOutput) throws Exception { // Helper function
//		int size = 0;
//		// only using output file and all the pages of that
//		// read fileoutput and check how many pages in it
//		for (int i = 0; i < filesJson.getSize(); i++) {
//			{
//				if (filesJson.getFileJson(i).getName().equalsIgnoreCase(fileOutput)) {
//					ArrayList<PagesJson> pagesList = filesJson.getFileJson(i).getPages();
//					// iterate through pages of fileOutput and bulk
//					for (int j = 0; j < pagesList.size(); j++) {
//						long pageGuid = pagesList.get(j).getGuid();
//						long page = md5(fileOutput + i);
//						ChordMessageInterface peer = chord.locateSuccessor(pageGuid);
//						peer.bulk(page);
//					}
//				}
//			}
//		}
//	}

		
}
