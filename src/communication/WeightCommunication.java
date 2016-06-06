package communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import dao.DatabaseHandler;

public class WeightCommunication implements Runnable{

	private boolean running;
	private Socket socket;
	private BufferedReader reader;
	private PrintWriter writer;
	private Thread thread;
	private DatabaseHandler handler;
	private String[] raavare;
	
	public WeightCommunication(Socket socket, BufferedReader reader, PrintWriter writer, DatabaseHandler handler){
		this.socket = socket;
		this.reader = reader;
		this.writer = writer;
		running = true;
		this.thread = new Thread();
		this.handler = handler;
	}
	
	public void start(){
		thread.start();
	}
	
	@Override
	public void run() {
		while(running){
			handleUserConfirmId();
			if(handleGetProduktBatchReceptName()){
				for(int i = 0; i < raavare.length; i++){
					try {
						writer.println("RM20 8 \"Er vægten ubelastet?\" \"\" \"&3\"");
						String str = reader.readLine();
						if(str.startsWith("RM20 A")){
							String[] splits = str.split(" ");
							if(splits.length > 2){
								String response = splits[2];
								response = response.replace("\"", "");
								if(response.equals("ja")){
									writer.println("D \""+raavare[i]+"\"");
									//fortsæt fra 8 til 14
								}
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
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
	
	/**
	 * Used for the procedure 2-4
	 */
	private void handleUserConfirmId(){
		boolean validOprId = false;
		while(!validOprId){
			try {
				writer.println("RM20 8 \"Indtast ID\" \"\" \"&3\"");
				String str = reader.readLine();
				if(str.contains("RM20 A")){
					String[] splits = str.split(" ");
					if(splits.length > 2){
						String id = splits[2];
						id.replace("\"", "");
						try{
							int integer_id = Integer.parseInt(id);
							String name = handler.getOperatoerNameFromId(integer_id);
							if(name != null){
								writer.println("RM20 8 \""+name+", fortsæt?\" \"\" \"&3\"");
								String str2 = reader.readLine();
								if(str.contains("RM20 A")){
									splits = str2.split(" ");
									if(splits.length > 2){
										String response = splits[2];
										response = response.replace("\"", "");
										if(response.equals("ja")){
											validOprId = true;
										}
									}
								}
							}
						} catch(NumberFormatException e){}
					}
				} 
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Used for the procedure 5-6
	 * @return
	 */
	private boolean handleGetProduktBatchReceptName(){
		try {
			writer.println("RM20 8 \"Indtast produktbatch nr\" \"\" \"&3\"");
			String str = reader.readLine();
			if(str.startsWith("RM20 A")){
				String[] splits = str.split(" ");
				if(splits.length > 2){
					String id = splits[2];
					id = id.replace("\"", "");
					try{
						int integer_id = Integer.parseInt(id);
						String name = handler.getReceptNameFromProduktBatchId(integer_id);
						if(name != null){
							writer.println("D \""+name+"\"");
							raavare = handler.getRaavareForProduktBatch(integer_id);
							return true;
						}
					} catch(NumberFormatException e){}
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
}