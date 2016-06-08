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
	private int transactionProduktBatchID;
	private int currentOperatoerID;
	
	public WeightCommunication(Socket socket, BufferedReader reader, PrintWriter writer, DatabaseHandler handler){
		this.socket = socket;
		this.reader = reader;
		this.writer = writer;
		running = true;
		this.thread = new Thread(this);
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
				handleMeasuringLoop();
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
				writer.println("RM20 8 \"Enter ID\" \"\" \"&3\"");
				reader.readLine();
				String str = reader.readLine();
				if(str.contains("RM20 A")){
					String[] splits = str.split(" ");
					if(splits.length > 2){
						String id = splits[2];
						id = id.replace("\"", "");
						try{
							int integer_id = Integer.parseInt(id);
							currentOperatoerID = integer_id;
							String name = handler.getOperatoerNameFromId(integer_id);
							if(name != null){
								writer.println("RM20 8 \"Confirm name: "+name+"\" \"\" \"&3\"");
								reader.readLine();
								String str2 = reader.readLine();
								if(str2.contains("RM20 A")){
									validOprId = true;
								}
							}
						} catch(NumberFormatException e){
							e.printStackTrace();
						}
					}
				} 
			} catch (IOException e) {
				running = false;
				validOprId = true;
			}
		}
	}

	/**
	 * Used for the procedure 5-6
	 * @return
	 */
	private boolean handleGetProduktBatchReceptName(){
		try {
			writer.println("RM20 8 \"Enter produktbatch nr\" \"\" \"&3\"");
			reader.readLine();
			String str = reader.readLine();
			if(str.startsWith("RM20 A")){
				String[] splits = str.split(" ");
				if(splits.length > 2){
					String id = splits[2];
					id = id.replace("\"", "");
					try{
						int integer_id = Integer.parseInt(id);
						transactionProduktBatchID = integer_id;
						String name = handler.getReceptNameFromProduktBatchId(transactionProduktBatchID);
						if(name != null){
							writer.println("P111 \"Ingredient: "+name+"\"");
							reader.readLine();
							raavare = handler.getRaavareForProduktBatch(handler.getReceptIdFromProduktBatchId(transactionProduktBatchID));
							return true;
						}
					} catch(NumberFormatException e){}
				}
			}
			
		} catch (IOException e) {
			running = false;
		}
		return false;
	}
	
	/**
	 * Used for the procedure 8-end
	 */
	private void handleMeasuringLoop(){
		for(int i = 0; i < raavare.length; i++){
			try {
				writer.println("RM20 8 \"Confirm empty weight\" \"\" \"&3\"");
				reader.readLine();
				String str = reader.readLine();
				if(str.startsWith("RM20 A")){
					writer.println("P111 \"Ingredient: "+raavare[i]+"\"");
					reader.readLine();
					handler.setProduktBatchStatus(transactionProduktBatchID, 1);
					writer.println("T");
					reader.readLine();
					writer.println("RM20 8 \"Place tara container\" \"\" \"&3\"");
					reader.readLine();
					String res = reader.readLine();
					if(res.startsWith("RM20 A")){
						writer.println("T");
						String tara = reader.readLine();
						writer.println("RM20 8 \"Enter raavarebatch nr:\" \"\" \"&3\"");
						reader.readLine();
						String resp = reader.readLine();
						if(resp.startsWith("RM20 A")){
							String[] respSplit = resp.split(" ");
							if(respSplit.length > 2){
								String rb_id = respSplit[2];
								rb_id = rb_id.replace("\"", "");
								try{
									int integer_id = Integer.parseInt(rb_id);
									Double maengde = handler.getMaengdeFromRBIDandPBID(integer_id, transactionProduktBatchID);
									writer.println("P111 \"Ingre: "+raavare[i]+" Amount: "+maengde+"\"");
									reader.readLine();
									writer.println("ST 1");
									reader.readLine();
									double maengdeOnWeight = 0.0;
									boolean isInTolerance = false;
									double toleranceAmount = handler.getToleranceFromRBIDandPBID(integer_id, transactionProduktBatchID);
									writer.println("P121 "+maengde+" kg "+(maengde * toleranceAmount/100)+" kg "+(maengde * toleranceAmount/100)+" kg");
									System.out.println("P121 "+maengde+" kg "+(maengde * toleranceAmount/100)+" kg "+(maengde * toleranceAmount/100)+" kg");
									reader.readLine();
									while(!isInTolerance){
										String maengdeWeight = reader.readLine();
										maengdeOnWeight = Double.parseDouble(maengdeWeight.substring(7, maengdeWeight.length()-3));
										if(maengdeOnWeight > (maengde - maengde * toleranceAmount/100) && maengdeOnWeight < (maengde + maengde * toleranceAmount/100)){
											isInTolerance = true;
										} else{
											writer.println("RM20 8 \"Not in tolerance!\" \"\" \"&3\"");
											reader.readLine();
											reader.readLine();
										}
									}
									
									writer.println("ST 0");
									reader.readLine();
									double taraOnWeight = Double.parseDouble(tara.substring(7, tara.length()-3));
									System.out.println("NETTO: "+maengdeOnWeight);
									System.out.println("TARA: "+taraOnWeight);
									handler.updateProduktBatchKomponent(transactionProduktBatchID, integer_id, taraOnWeight, maengdeOnWeight, currentOperatoerID);
									handler.removeNettoFromRaavareBatch(integer_id, maengdeOnWeight);
									if(i != raavare.length-1){
										writer.println("RM20 8 \"Finished item\" \"\" \"&3\"");
										reader.readLine();
										reader.readLine();
									} else{
										writer.println("RM20 8 \"Done! Resetting.\" \"\" \"&3\"");
										reader.readLine();
										reader.readLine();
										handler.setProduktBatchStatus(transactionProduktBatchID, 2);
									}
								} catch(NumberFormatException e){
									e.printStackTrace();
								}
							}
						}
					}
				}
			} catch (IOException e) {
				running = false;
			}
		}
	}

	public boolean isRunning(){
		return running;
	}
	
	public void setRunning(boolean running){
		this.running = running;
	}
}