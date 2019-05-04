package dfs;
import java.io.*;
import java.util.*;
import com.google.gson.Gson;

public class DFSCommand
{
    DFS dfs;
    public static String userDir = System.getProperty("user.dir");
	public static String songPath = userDir + "/src/data/musicComplete.json";
	public static String songFile = "songs";
	public static String mapReduceFile = "songsClean";
    
    public DFSCommand(int p, int portToJoin) throws Exception {
        dfs = new DFS(p);

        if (portToJoin > 0)
        {
            System.out.println("Joining somewhere"+ portToJoin);
            dfs.join("127.0.0.1", portToJoin);
        }
        
        BufferedReader buffer=new BufferedReader(new InputStreamReader(System.in));
        mapReduceTest(buffer);
        
        System.out.println("Please enter the next command");
        String line = buffer.readLine();
        
        while (!line.equals("quit"))
        {
        	try {
        		
	            String[] result = line.split("\\s");
	            if (result[0].equals("join")  && result.length > 1)
	            {
	                dfs.join("127.0.0.1", Integer.parseInt(result[1]));
	            }
	            else if (result[0].equals("print"))
	            {
	                dfs.print();
	            }
	            else if (result[0].equals("create"))
	            {
	            	dfs.create(result[1]);
	                System.out.println("File created");
	            }
	            else if (result[0].equals("ls"))
	            {
	                System.out.println("Listing Files: ");
	                dfs.lists();
	            }
	            else if (result[0].equals("append"))
	            {
	
	            	RemoteInputFileStream input = new RemoteInputFileStream(result[2]);
	                dfs.append(result[1], input);
	                System.out.println("page added");
	
	            }
	            else if (result[0].equals("delete"))
	            {
	              dfs.delete(result[1]);
	              System.out.println("Deleted "+result[1]+" from the page");
	            }
	            else if(result[0].equals("mapreduce")) {
	            	dfs.runMapReduce(result[1], result[2]);
	            }
	            else if (result[0].equals("read"))
	            {
	                int pageNumber = Integer.parseInt(result[2]);
	                int i;
	                RemoteInputFileStream r = dfs.read(result[1], pageNumber);
	                r.connect();
	                while((i = r.read()) != -1){
	                    System.out.print((char) i);
	                }
	                System.out.println();
	                System.out.println("page read");
	
	            }
	            else if (result[0].equals("head"))
	            {
	            	RemoteInputFileStream head = dfs.read(result[1], 1);
	            	head.connect();
	            	int i;
	            	while((i = head.read()) != -1) {
	            		System.out.print((char)i);
	            	}
	            	System.out.println();
	            	System.out.println("read head");
	            }
	
	            else if (result[0].equals("tail"))
	            {
	            	RemoteInputFileStream tail = dfs.tail(result[1]);
	            	tail.connect();
	            	int i;
	            	while((i = tail.read()) != -1) {
	            		System.out.print((char)i);
	            	}
	            	System.out.println();
	            	System.out.println("read tail");
	            }
	            else if (result[0].equals("leave"))
	            {
	                dfs.leave();
	            }
	            else if (result[0].equals("move"))
	            {
	            	dfs.move(result[1], result[2]);
	            }
	            line=buffer.readLine();
        	}catch(ArrayIndexOutOfBoundsException e) {
        		System.out.println("Invalid command string");
        		line=buffer.readLine();
        	}
        }
            // User interface:
            // join, ls, touch, delete, read, tail, head, append, move
    }
    
    public void mapReduceTest(BufferedReader buffer) throws Exception {
    	System.out.println("Would you like to test mapreduce?");
    	String userInput = buffer.readLine();
    	if(userInput.equals("yes") || userInput.equals("y")) {
    		String files = dfs.lists();
    		if(files.contains(mapReduceFile)) {
    			System.out.println("Deleting old mapreduceFile");
    			dfs.delete(mapReduceFile);
    		}
    		if(files.contains(songFile)) {
    			System.out.println("Running mapreduce method");
    			dfs.runMapReduce(songFile, mapReduceFile);
    		}else {
    			dfs.create(songFile);
    			RemoteInputFileStream input = new RemoteInputFileStream(songPath);
    			System.out.println("Appending file: " + songPath + "\nplease wait...");
                dfs.append(songFile, input);
                System.out.println("Finished appending file");
                System.out.println("Running mapreduce method");
    			dfs.runMapReduce(songFile, mapReduceFile);
    		}
    			
    		System.out.println("Finished with initalization");
    	}
    	
    }

    static public void main(String arg[]) throws Exception
    {
    	System.out.println("Enter port: ");
    	Scanner input = new Scanner(System.in);
    	String userInput = input.nextLine();
    	String[] args = new String[1];
    	args[0] = userInput;
        if (args.length < 1 ) {
            throw new IllegalArgumentException("Parameter: <port> <portToJoin>");
        }
        if (args.length > 1 ) {
            DFSCommand dfsCommand=new DFSCommand(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        }
        else
        {
            DFSCommand dfsCommand=new DFSCommand( Integer.parseInt(args[0]), 0);
        }
     }
}
