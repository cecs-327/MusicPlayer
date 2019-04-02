
import java.io.*;
import java.util.*;
import com.google.gson.Gson;

public class DFSCommand
{
    DFS dfs;

    public DFSCommand(int p, int portToJoin) throws Exception {
        dfs = new DFS(p);

        if (portToJoin > 0)
        {
            System.out.println("Joining somewhere"+ portToJoin);
            dfs.join("127.0.0.1", portToJoin);
        }

        BufferedReader buffer=new BufferedReader(new InputStreamReader(System.in));
        String line = buffer.readLine();
        while (!line.equals("quit"))
        {
            String[] result = line.split("\\s");
            if (result[0].equals("join")  && result.length > 1)
            {
                dfs.join("127.0.0.1", Integer.parseInt(result[1]));
            }
            if (result[0].equals("print"))
            {
                dfs.print();
            }
            if (result[0].equals("create"))
            {
            	dfs.create(result[1]);
                System.out.println("File created");
            }
            if (result[0].equals("create"))
            {
            	dfs.create(result[1]); 
            	System.out.println("File created");
            }
            if (result[0].equals("append"))
            {
            
            	RemoteInputFileStream input = new RemoteInputFileStream(result[2]);
                dfs.append(result[1], input); 
                System.out.println("page added");
               
            }
            if (result[0].equals("read"))
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
            if (result[0].equals("head")) 
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
            
            if (result[0].equals("tail"))
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
            if (result[0].equals("leave"))
            {
                dfs.leave();
            }
            line=buffer.readLine();
        }
            // User interface:
            // join, ls, touch, delete, read, tail, head, append, move
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
