package input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import communication.ClientCommunication;

public class SocketHandler {

	private boolean running;
	private ServerSocket socket;
	private ArrayList<ClientCommunication> clients;
	
	public SocketHandler(int port) throws IOException{
		this.socket = new ServerSocket(port);
		running = true;
	}
	
	public void start() throws IOException{
		while(running){
			Socket acceptedSocket = socket.accept();
			BufferedReader reader = new BufferedReader(new InputStreamReader(acceptedSocket.getInputStream()));
			PrintWriter writer = new PrintWriter(acceptedSocket.getOutputStream(), true);
			ClientCommunication client = new ClientCommunication(acceptedSocket, reader, writer);
			client.start();
			clients.add(client);
		}
	}
	
	public ServerSocket getsocket(){
		return socket;
	}
	
	public boolean isRunning(){
		return running;
	}
	
	public ArrayList<ClientCommunication> getClients(){
		return clients;
	}
}