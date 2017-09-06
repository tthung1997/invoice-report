package filewriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import datacontainers.Customer;
import datacontainers.Person;
import datacontainers.Product;

/**
 * XMLWriter class takes the ArrayList and writes it into a XML file.
 */
public class XMLWriter {

	public <T> void xmlConverter(List<T> objects, int type) {
		XStream xstream = new XStream();
		String filename = "data/";

		switch (type) {
		case 0:
			filename = filename.concat("Persons.xml");
			xstream.alias("person", Person.class);
			break;
		case 1:
			filename = filename.concat("Customers.xml");
			xstream.alias("customer", Customer.class);
			break;
		default:
			filename = filename.concat("Products.xml");
			xstream.alias("product", Product.class);
		}

		File xmlOutput = new File(filename);

		PrintWriter xmlPrintWriter = null;
		try {
			xmlPrintWriter = new PrintWriter(xmlOutput);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		xmlPrintWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");

		String output = xstream.toXML(objects);
		xmlPrintWriter.write(output);
		
		xmlPrintWriter.close();
	}
}
