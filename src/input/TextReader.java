package input;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TextReader {

	public static String[] getContentsOfFile(String loc) {
		try {
			FileReader fileReader = new FileReader(loc);
			BufferedReader reader = new BufferedReader(fileReader);
			String line = null;
			ArrayList<String> listOfStrings = new ArrayList<String>();
			while ((line = reader.readLine()) != null) {
				listOfStrings.add(line);
			}
			String[] str = new String[listOfStrings.size()];
			str = listOfStrings.toArray(str);
			reader.close();
			return str;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
