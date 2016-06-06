package communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientCommunication implements Runnable{

	private boolean running;
	private Thread thread;
	private Socket socket;
	private BufferedReader reader;
	private PrintWriter writer;
	
	public ClientCommunication(Socket socket, BufferedReader reader, PrintWriter writer){
		this.socket = socket;
		this.reader = reader;
		this.writer = writer;
		thread = new Thread(this);
		running = true;
	}
	
	public void start(){
		thread.start();
	}
	
	@Override
	public void run() {
		while(running){
			try {
				String str = reader.readLine();
				writer.println(str);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void shutdownPassive(){
		running = false;
	}
	
	public Thread.State getThreadState(){
		return thread.getState();
	}
	
	public Thread getThread(){
		return thread;
	}
	
	public Socket getSocket(){
		return socket;
	}
	
	public BufferedReader getReader(){
		return reader;
	}
	
	public PrintWriter getWriter(){
		return writer;
	}
	
	public boolean isRunning(){
		return running;
	}
}