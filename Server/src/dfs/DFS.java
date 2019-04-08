
import java.rmi.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.math.BigInteger;
import java.security.*;
import com.google.gson.Gson;
import java.io.InputStream;
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


public class DFS
{
    public class PagesJson
    {
        Long guid;
        Long size;
        String creationTS;
        String readTS;
        String writeTS;
        int referenceCount;
        public PagesJson(Long guid, Long size, String creationTS, String readTS, String writeTS,int referenceCount)
        {

        	this.guid = guid;
            this.size = size;
            this.creationTS = creationTS;
            this.readTS = readTS;
            this.writeTS = writeTS;
            this.referenceCount = referenceCount;
        }
        // getters
        public Long getSize()
        {
        	return this.size;
        }
        public Long getGuid()
        {
        	return this.guid;
        }

        public String getCreationTS(){
            return this.creationTS;
        }

        public int getReferenceCount()
        {
        	return this.referenceCount;
        }
        // setters
        public void setSize(Long size)
        {
        	this.size = size;
        }
        public void setGuid(Long guid)
        {
        	this.guid = guid;
        }
        public void setCreationTS(String creationTS)
        {
        	this.creationTS = creationTS;
        }

        public void setReadTS(String readTS)
        {
        	this.readTS = readTS;
        }

        public void setWriteTS(String writeTS)
        {
        	this.writeTS = writeTS;
        }
        public void setReferenceCount(int referenceCount)
        {
        	this.referenceCount = referenceCount;
        }
    };

    public class FileJson
    {
        String name;
        Long   size;
        String creationTS;
        String readTS;
        String writeTS;
        int referenceCount;
        int numOfPages;
        int maxPageSize;
        ArrayList<PagesJson> pages;
        public FileJson()
        {
            this.size = new Long(0);
            this.numOfPages = 0;
            this.referenceCount = 0;
            //this.creationTS = new Long(date.getTime());
            this.creationTS = LocalDateTime.now().toString();
            this.readTS = "0";
            this.writeTS = "0";
            this.maxPageSize = 0;
            this.pages = new ArrayList<PagesJson>();
        }
        //dealing with pages

        public void addPageInfo(Long guid, Long size, String creationTS, String readTS, String writeTS,int referenceCount)
        {
        	PagesJson page = new PagesJson(guid, size,creationTS,readTS, writeTS,referenceCount);
        	pages.add(page);
        }

        // getters
        public int getMaxPageSize()
        {
        	return this.maxPageSize;
        }
        public int getReferenceCount()
        {
        	return this.referenceCount;
        }
        public int getNumOfPages()
        {
        	return this.numOfPages;
        }
        public Long getSize()
        {
        	return this.size;
        }

        public ArrayList<PagesJson> getPages(){
            return pages;
        }

        public String getCreationTS(){
            return creationTS;
        }

        public String getName()
        {
        	return this.name;
        }


        // setters
        public void setMaxPageSize(int maxPageSize)
        {
        	this.maxPageSize = maxPageSize;
        }
        public void setName(String name)
     	{
     		this.name = name;
     	}

        public void setSize(Long size)
     	{
     		this.size = size;
     	}
        public void addSize(Long size)
     	{
     		this.size += size;
     	}
        public void setReferenceCount(int referenceCount)
        {
        	this.referenceCount = referenceCount;
        }
        public void setNumOfPages(int numOfPages)
        {
        	this.numOfPages = numOfPages;
        }
        public void addNumOfPages(int numOfPages)
        {
        	this.numOfPages += numOfPages;
        }
        public void setCreationTS(String time)
        {
        	this.creationTS = time;
        }

        public void setReadTS(String time)
        {
        	this.readTS = time;
        }

        public void setWriteTS(String time)
        {
        	this.writeTS = time;
        }

    };

    public class FilesJson
    {
         List<FileJson> file;

         public FilesJson()
         {
             file = new ArrayList <FileJson>();

         }

        // getters
     	 public FileJson getFileJson(int index)
     	 {
     		 return file.get(index);
     	 }

         public int getSize()
         {
        	 return file.size();
         }
        // setters
     	public void addFile(FileJson fileJson)
     	{
     		file.add(fileJson);

     	}
    };


    int port;
    Chord  chord;
    FilesJson filesJson;

    private long md5(String objectName)
    {
        try
        {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(objectName.getBytes());
            BigInteger bigInt = new BigInteger(1,m.digest());
            return Math.abs(bigInt.longValue());
        }
        catch(NoSuchAlgorithmException e)
        {
                e.printStackTrace();

        }
        return 0;
    }



    public DFS(int port) throws Exception
    {


        this.port = port;
        long guid = md5("" + port);
        this.filesJson = new FilesJson();
        chord = new Chord(port, guid);
        Files.createDirectories(Paths.get(guid+"/repository"));
        Files.createDirectories(Paths.get(guid+"/tmp"));
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
    public void join(String Ip, int port) throws Exception
    {
        chord.joinRing(Ip, port);
        chord.print();
    }


   /**
 * leave the chord
  *
 */
    public void leave() throws Exception
    {
       chord.leave();
    }

   /**
 * print the status of the peer in the chord
  *
 */
    public void print() throws Exception
    {
        chord.print();
    }

/**
 * readMetaData read the metadata from the chord
  *
 */
    public FilesJson readMetaData() throws Exception
    {
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
            System.out.println(strMetaData);
            filesJson= gson.fromJson(strMetaData, FilesJson.class);
        } catch (NoSuchElementException ex)
        {
            filesJson = new FilesJson();
        }
        return filesJson;
    }

/**
 * writeMetaData write the metadata back to the chord
  *
 */
    public void writeMetaData(FilesJson filesJson) throws Exception
    {

        long guid = md5("Metadata");
        ChordMessageInterface peer = chord.locateSuccessor(guid);

        Gson gson = new Gson();
        peer.put(guid, gson.toJson(filesJson));
    }

/**
 * Change Name
  *
 */
    public void move(String oldName, String newName) throws Exception
    {
        // TODO:  Change the name in Metadata
        // Write Metadata
    }


/**
 * List the files in the system
  *
 * @param filename Name of the file
 */
    public String lists() throws Exception
    {
        String listOfFiles = "";

        return listOfFiles;
    }

/**
 * create an empty file
  *
 * @param filename Name of the file
 */
    public void create(String fileName) throws Exception
    {
          // TODO: Create the file fileName by adding a new entry to the Metadata
         // Write Metadata
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

	 for (int i = 0; i < filesJson.getSize(); i++) {

		 if (filesJson.getFileJson(i).getName().equalsIgnoreCase(fileName)) {

			 // get metadata information
			 long guidPort = md5("" + port);

			 // get list of pages from the file
			 ArrayList<PagesJson> pagesList = filesJson.getFileJson(i).getPages();

			 //iterate through all pages of the file
			 for (int k = 0; k < pagesList.size(); k++) {

				 //get the specified page info from file
				 PagesJson pagesRead = pagesList.get(k);
				 long pageGuid = pagesRead.getGuid();

				 // remove appended file in directory
				 String userDir = System.getProperty("user.dir");
				 File file = new File(userDir + "/" + guidPort + "/repository/" + pageGuid);
				 file.delete();

				 // remove from chord
				 ChordMessageInterface peer = chord.locateSuccessor(pageGuid);
				 peer.delete(pageGuid);
			 }

			 // remove JSONFile from files
			 filesJson.file.remove(filesJson.getFileJson(i));

		 }
	 }

/**
 * Read block pageNumber of fileName
  *
 * @param filename Name of the file
 * @param pageNumber number of block.
 */
    public RemoteInputFileStream read(String fileName, int pageNumber) throws Exception
    {
        //List of pages starts at 0 so decrement it
        pageNumber--;
        RemoteInputFileStream rifs = null;
        for(int i = 0; i < filesJson.getSize(); i++){
            if(filesJson.getFileJson(i).getName().equalsIgnoreCase(fileName)){
                ArrayList<PagesJson> pagesList = filesJson.getFileJson(i).getPages();
                for(int k = 0; k < pagesList.size(); k++){
                    if(k == pageNumber){
                        PagesJson pageToRead = pagesList.get(k);
                        String timeOfRead = LocalDateTime.now().toString();
                        pageToRead.setReadTS(timeOfRead);
                        filesJson.getFileJson(i).setReadTS(timeOfRead);
                        Long pageGUID = md5(fileName + pageToRead.getCreationTS());
                        ChordMessageInterface peer = chord.locateSuccessor(pageGUID);
                        rifs = peer.get(pageGUID);
                    }
                }
                writeMetaData(filesJson);
            }
        }
        return rifs;
    }

    public RemoteInputFileStream tail(String fileName) {
    	RemoteInputFileStream tail = null;
    	for(int i = 0; i < filesJson.getSize(); i++) {
    		if(filesJson.getFileJson(i).getName().equalsIgnoreCase(fileName)) {
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
 * @param data RemoteInputStream.
 */
    public void append(String filename, RemoteInputFileStream data) throws Exception
    {

        for(int i = 0; i < filesJson.getSize();i++)
        {
        	//append the page to the file specified by the user
        	if(filesJson.getFileJson(i).getName().equalsIgnoreCase(filename))
        	{

        		//update information in the file we are going to append
        	    //data.connect();
                //This is used to get the size of the file
                Long sizeOfFile = new Long(data.available());
                String timeOfAppend = LocalDateTime.now().toString();
        		filesJson.getFileJson(i).setWriteTS(timeOfAppend);
        		filesJson.getFileJson(i).addNumOfPages(1);
        		filesJson.getFileJson(i).addSize(sizeOfFile);

        		//create the page metadata information
        		String objectName = filename + LocalDateTime.now();
        		Long guid = md5(objectName);

        		ChordMessageInterface peer = chord.locateSuccessor(guid);
                peer.put(guid, data);
        		//chord locate successor , then put

        		//filesJson.getFileJson(i).addPageInfo(guid, size, creationTS, readTS, writeTs, referenceCount);
        		Long defaultZero = new Long(0);
        		filesJson.getFileJson(i).addPageInfo(guid,sizeOfFile,timeOfAppend,"0","0",0);

        	}

        }
        writeMetaData(filesJson);


    }


}
