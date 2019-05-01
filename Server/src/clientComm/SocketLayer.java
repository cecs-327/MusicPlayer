package clientComm;
import java.io.IOException;
import java.net.*;

public class SocketLayer extends Thread{
    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[8292];
    private int serverPort = 41236;
    private Dispatcher dispatcher;
    

    public SocketLayer(){
        try{
            SocketAddress sockaddr = new InetSocketAddress(InetAddress.getByName("127.0.0.1"), serverPort);
            socket = new DatagramSocket(sockaddr);
        } catch(SocketException se) {
            System.out.println(se);
        } catch(UnknownHostException err) {
            System.out.println(err);
        }
        dispatcher = new Dispatcher();
    }

    public void run(){
        running = true;
        System.out.println("Starting up on port; " + serverPort);
        while(running){
        	buf = new byte[256];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try{
                socket.receive(packet);
                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                packet = new DatagramPacket(buf, buf.length, address, port);

                String received = new String(packet.getData(), 0, packet.getLength());
                System.out.println("\n\nRequest:\n\n" + received + "\n\nEnd Request");
                String response = dispatcher.dispatch(received);
                
                DatagramPacket responsePacket = new DatagramPacket(response.getBytes(), response.getBytes().length, address, port);
                
                System.out.println("\n\nResponse from dispatcher: \n\n" + response);
                
                socket.send(responsePacket);

            } catch(IOException ioe) {
                System.out.println(ioe);
            }
            
        }
        socket.close();
    }
}