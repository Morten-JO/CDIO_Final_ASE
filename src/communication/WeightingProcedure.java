package communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import dao.DatabaseHandler;
import input.HelpFunctions;

public class WeightingProcedure implements Runnable{

	private boolean running;
	private Socket socket;
	private Thread thread;
	private WeightingCommunication comm;
	private DatabaseHandler handler;
	private String[] raavare;
	private int transactionProduktBatchID = -1;//-1 to force sqlException to prevent integrity issues in case of failure before receiving batchID
	private int currentOperatoerID;
	boolean validOprId = false;
	
	public WeightingProcedure(Socket socket, BufferedReader reader, PrintWriter writer, DatabaseHandler handler){
		this.socket = socket;
		comm = new WeightingCommunication(reader, writer);
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
			try {
				handleUserConfirmId();
				if(handleGetProduktBatchReceptName()){
					handleMeasuringLoop();
				}
			} catch (IOException | NullPointerException e) {
				e.printStackTrace();
				running = false;
				validOprId = true;
				handler.setProduktBatchStatus(transactionProduktBatchID, 0);
			}
		}
	}
	
	public Socket getSocket(){
		return socket;
	}
	
	/**
	 * Used for the procedure 2-4
	 */
	private void handleUserConfirmId() throws IOException, NullPointerException{
		while(!validOprId){
			String str = comm.writeReadRM20("RM20 8 \"Enter ID\" \"\" \"&3\"");
			if(str.contains("RM20 A")){
				String[] splits = str.split(" ");
				if(splits.length > 2){
					handlerUserConfirmIdParse(splits);
				}
			} 
		}
	}

	private void handlerUserConfirmIdParse(String[] splits) throws IOException {
		String id = splits[2];
		id = id.replace("\"", "");
		try{
			int integer_id = Integer.parseInt(id);
			currentOperatoerID = integer_id;
			String name = handler.getOperatoerNameFromId(integer_id);
			if(name != null){
				String str2 = comm.writeReadRM20("RM20 8 \"Confirm name: "+name+"\" \"\" \"&3\"");
				if(str2.contains("RM20 A")){
					validOprId = true;
				}
			}
		} catch(NumberFormatException e){
			e.printStackTrace();
		}
	}

	/**
	 * Used for the procedure 5-6
	 * @return
	 */
	private boolean handleGetProduktBatchReceptName() throws IOException, NullPointerException{
			String str = comm.writeReadRM20("RM20 8 \"Enter produktbatch nr\" \"\" \"&3\"");
			if(str.startsWith("RM20 A")){
				String[] splits = str.split(" ");
				if(splits.length > 2){
					if(handleGetProduktBatchReceptNameParser(splits)){
						return true;
					}
				}
			}
			
		return false;
	}

	private boolean handleGetProduktBatchReceptNameParser(String[] splits) throws IOException {
		String id = splits[2];
		id = id.replace("\"", "");
		try{
			int integer_id = Integer.parseInt(id);
			transactionProduktBatchID = integer_id;
			String name = handler.getReceptNameFromProduktBatchId(transactionProduktBatchID);
			if(name != null){
				comm.writeReadLines("P111 \"Ingredient: "+name+"\"");
				raavare = handler.getRaavareForProduktBatch(handler.getReceptIdFromProduktBatchId(transactionProduktBatchID));
				return true;
			}
		} catch(NumberFormatException e){}
		return false;
	}
	
	/**
	 * Used for the procedure 8-end
	 */
	private void handleMeasuringLoop() throws IOException, NullPointerException{
		for(int i = 0; i < raavare.length; i++){
				String str = comm.writeReadRM20("RM20 8 \"Confirm empty weight\" \"\" \"&3\"");
				if(str.startsWith("RM20 A")){
					handleRaavareMeasurement(i);
				}
		}
	}

	private void handleRaavareMeasurement(int i) throws IOException {
		comm.writeReadLines("P111 \"Ingredient: "+raavare[i]+"\"");
		handler.setProduktBatchStatus(transactionProduktBatchID, 1);
		comm.writeReadLines("T");
		String res = comm.writeReadRM20("RM20 8 \"Place tara container\" \"\" \"&3\"");
		if(res.startsWith("RM20 A")){
			String tara = comm.writeReadLines("T");
			String resp = comm.writeReadRM20("RM20 8 \"Enter raavarebatch nr:\" \"\" \"&3\"");
			if(resp.startsWith("RM20 A")){
				String[] respSplit = resp.split(" ");
				if(respSplit.length > 2){
					handleRaavareMeasurementStepTwo(i, tara, respSplit);
				}
			}
		}
	}

	private void handleRaavareMeasurementStepTwo(int i, String tara, String[] respSplit) throws IOException {
		String rb_id = respSplit[2];
		rb_id = rb_id.replace("\"", "");
		try{
			int integer_id = Integer.parseInt(rb_id);
			double maengde = handler.getMaengdeFromRBIDandPBID(integer_id, transactionProduktBatchID);
			
			comm.writeReadLines("P111 \"Ingre: "+raavare[i]+" Amount: "+maengde+"\"");
			double maengdeOnWeight = 0.0;
			boolean isInTolerance = false;
			double toleranceAmount = handler.getToleranceFromRBIDandPBID(integer_id, transactionProduktBatchID);
			comm.writeReadLines("P121 "+HelpFunctions.roundDouble(maengde, 2)+" kg "+HelpFunctions.roundDouble((maengde * toleranceAmount/100), 2)+" kg "+HelpFunctions.roundDouble((maengde * toleranceAmount/100),2)+" kg");
			comm.writeReadLines("ST 1");
			while(!isInTolerance){
				String maengdeWeight = comm.getReader().readLine();
				maengdeOnWeight = Double.parseDouble(maengdeWeight.substring(7, maengdeWeight.length()-3));
				if(maengdeOnWeight > (maengde - maengde * toleranceAmount/100) && maengdeOnWeight < (maengde + maengde * toleranceAmount/100)){
					isInTolerance = true;
				} else{
					comm.writeReadRM20("RM20 8 \"Not in tolerance!\" \"\" \"&3\"");
				}
			}
			comm.writeReadLines("ST 0");
			double taraOnWeight = Double.parseDouble(tara.substring(7, tara.length()-3));
			handler.updateProduktBatchKomponent(transactionProduktBatchID, integer_id, taraOnWeight, maengdeOnWeight, currentOperatoerID);
			handler.removeNettoFromRaavareBatch(integer_id, maengdeOnWeight);
			if(i != raavare.length-1){
				comm.writeReadRM20("RM20 8 \"Finished item\" \"\" \"&3\"");
			} else{
				comm.writeReadRM20("RM20 8 \"Done! Resetting.\" \"\" \"&3\"");
				handler.setProduktBatchStatus(transactionProduktBatchID, 2);
			}
		} catch(NumberFormatException e){
			e.printStackTrace();
		}
	}

	public boolean isRunning(){
		return running;
	}
	
	public void setRunning(boolean running){
		this.running = running;
	}
	
}