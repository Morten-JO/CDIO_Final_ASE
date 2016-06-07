package input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import communication.WeightCommunication;
import dao.DatabaseHandler;

public class SocketHandler{

	private boolean running;
	private Socket socket;
	private String[] ips;
	private int port;
	private DatabaseHandler handler;
	private WeightCommunication[] coms;
	private long timePerStart = 3600000;
	private boolean forceStart;
	
	public SocketHandler(String[] ips, int port) {
		running = true;
		this.ips = ips;
		this.port = port;
		handler = new DatabaseHandler();
		coms = new WeightCommunication[ips.length];
	}
	
	public void start() {
		while(true){
			forceStart = false;
			connectToWeights();
			System.out.println("RAN");
			long timeToReach = System.currentTimeMillis() + timePerStart;
			while(timeToReach > System.currentTimeMillis() && !forceStart){
				//Waiting amount of time till next attempt to start them.
			}
		}
	}
	
	private void connectToWeights(){
		for(int i = 0; i < ips.length; i++){
			boolean shouldStart = false;
			if(coms[i] == null){
				shouldStart = true;
			} else{
				if(!coms[i].isRunning()){
					shouldStart = true;
				}
			}
			if(shouldStart){
				try {
					socket = new Socket();
					socket.connect(new InetSocketAddress(ips[i], port), 3000);
					BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
					WeightCommunication comm = new WeightCommunication(socket, reader, writer, handler);
					comm.start();
					coms[i] = comm;
					System.out.println("Weight #"+(i+1)+" is now online with ip: "+ips[i]);
				} catch(IOException e){
					System.err.println("Weight #"+(i+1)+" is offline with ip: "+ips[i]);
				}
			}
		}
		System.out.println("-----------------------------------------");
	}
	
	public Socket getsocket(){
		return socket;
	}
	
	public boolean isRunning(){
		return running;
	}
	
	public void forcePassiveRefresh(){
		forceStart = true;
		System.out.println("forcestarted! is: "+forceStart);
	}
	
	public WeightCommunication[] getWeights(){
		return coms;
	}
	
	public String[] getIps(){
		return ips;
	}
}