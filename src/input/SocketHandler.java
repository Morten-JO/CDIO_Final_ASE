package input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import communication.WeightCommunication;
import dao.DatabaseHandler;

public class SocketHandler {

	private boolean running;
	private Socket socket;
	private String[] ips;
	private int port;
	private DatabaseHandler handler;
	
	public SocketHandler(String[] ips, int port) {
		running = true;
		this.ips = ips;
		this.port = port;
		handler = new DatabaseHandler();
	}
	
	public void start() {
		for(int i = 0; i < ips.length; i++){
			try {
				socket = new Socket(ips[i], port);
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
				WeightCommunication comm = new WeightCommunication(socket, reader, writer, handler);
				comm.start();
				System.out.println("Weight #"+(i+1)+" is now online with ip: "+ips[i]);
			} catch(IOException e){
				System.err.println("Weight #"+(i+1)+" is offline with ip: "+ips[i]);
			}
		}
	}
	
	public Socket getsocket(){
		return socket;
	}
	
	public boolean isRunning(){
		return running;
	}
}