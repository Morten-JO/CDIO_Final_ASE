package communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class WeightingCommunication {

	private BufferedReader reader;
	private PrintWriter writer;

	public WeightingCommunication(BufferedReader reader, PrintWriter writer) {
		this.reader = reader;
		this.writer = writer;
	}

	public void writeReadLines(String... strings) throws IOException {
		for (String string : strings) {
			writeReadLines(string);
		}
	}

	public String writeReadLines(String output) throws IOException {
		return writeReadLines(output, 1);
	}

	public String writeReadLines(String output, int lines) throws IOException {
		writer.println(output);
		for (int i = 0; i < lines - 1; i++) {
			reader.readLine();
		}
		String str = reader.readLine();
		return str;
	}

	public String writeReadRM20(String output) throws IOException {
		writeReadLines(output);
		String str = reader.readLine();
		return str;
	}

	public BufferedReader getReader() {
		return reader;
	}

	public PrintWriter getWriter() {
		return writer;
	}

}
