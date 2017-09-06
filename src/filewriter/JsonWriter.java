package filewriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * JsonWriter class takes the ArrayList and writes it into a JSON file.
 */
public class JsonWriter {

	public <T> void jsonConverter(List<T> objects, int type) {

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String filename = "data/";

		switch (type) {
		case 0:
			filename = filename.concat("Persons.json");
			break;
		case 1:
			filename = filename.concat("Customers.json");
			break;
		default:
			filename = filename.concat("Products.json");
		}

		File jsonOutput = new File(filename);

		PrintWriter jsonPrintWriter = null;

		try {
			jsonPrintWriter = new PrintWriter(jsonOutput);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String output = gson.toJson(objects);
		jsonPrintWriter.write(output + "\n");

		jsonPrintWriter.close();
	}
}
