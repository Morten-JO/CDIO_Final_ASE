package start;

import input.SocketHandler;
import input.TextReader;

public class Main {

	public static void main(String[] args) {
		String[] weights = TextReader.getContentsOfFile("res/weights.txt");
		if(weights != null){
			new SocketHandler(weights, DataVariables.port).start();
		} else{
			System.err.println("Weights txt is invalid!");
		}
		
	}
}