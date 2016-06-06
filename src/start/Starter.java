package start;

import java.io.IOException;

import input.SocketHandler;

public class Starter {

	private SocketHandler handler;
	public static final int port = 5000;
	
	public Starter(){
		try {
			handler = new SocketHandler(port);
		} catch (IOException e) {
			System.out.println("A server is already running on port: "+port+", close it before running again.");
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public void start(){
		try {
			handler.start();
		} catch (IOException e) {
			System.out.println("Error in socketHandler, exitting program.");
			e.printStackTrace();
			System.exit(0);
		}
	}
}
