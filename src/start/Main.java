package start;

import gui.GUIWeightWindow;
import input.SocketHandler;
import input.TextReader;

public class Main {

	public static void main(String[] args) {
		String[] weights = TextReader.getContentsOfFile("res/weights.txt");
		if (weights != null) {
			SocketHandler handler = new SocketHandler(weights, DataVariables.port);
			new GUIWeightWindow(handler);
			handler.start();
		} else {
			System.err.println("Weights.txt is invalid!");
		}
	}
}