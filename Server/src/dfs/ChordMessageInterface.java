package dfs;

import java.rmi.*;
import java.io.*;

public interface ChordMessageInterface extends Remote
{
    public ChordMessageInterface getPredecessor()  throws RemoteException;
    ChordMessageInterface locateSuccessor(long key) throws RemoteException;
    ChordMessageInterface closestPrecedingNode(long key) throws RemoteException;
    public void joinRing(String Ip, int port)  throws RemoteException;
    public void joinRing(ChordMessageInterface successor)  throws RemoteException;
    public void notify(ChordMessageInterface j) throws RemoteException;
    public boolean isAlive() throws RemoteException;
    public long getId() throws RemoteException;
    
    
    public void put(long guidObject, RemoteInputFileStream inputStream) throws IOException, RemoteException;
    public void put(long guidObject, String text) throws IOException, RemoteException;
    public RemoteInputFileStream get(long guidObject) throws IOException, RemoteException;   
    public byte[] get(long guidObject, long offset, int len) throws IOException, RemoteException;  
    public void delete(long guidObject) throws IOException, RemoteException;
	public void bulk(long page) throws RemoteException;
	public String mapContext(Long guid) throws Exception;
	public void reduceContext(Long guid, DFS dfs, String fileOutput) throws RemoteException;
	public void onChordSize(long id, int i) throws RemoteException;
}
