package reports;

import java.util.ArrayList;

import datacontainers.Customer;
import datacontainers.Person;
import datacontainers.Product;
import filereader.FlatFileReader;
import filewriter.JsonWriter;
import filewriter.XMLWriter;

/**
 * DataConverter class is the driver class containing the main method to 
 * read data from flat files and write them to JSON and XML files.
 */
public class DataConverter {

	public static void main(String[] args) {
		
		// Create a FlatFileReader object
		FlatFileReader fr = new FlatFileReader();
		
		/* fr Reads data from the flat file;
		 * Creates objects and stores objects in an ArrayList
		 */
		ArrayList<Person> personList = fr.readPersons();
		ArrayList<Customer> customerList = fr.readCustomers(personList);
		ArrayList<Product> productList = fr.readProducts();
		
		// Write ArrayList into a Json file
		JsonWriter jWriter = new JsonWriter();
		jWriter.jsonConverter(personList, 0);
		jWriter.jsonConverter(customerList, 1);
		jWriter.jsonConverter(productList, 2);
		
		// Write ArrayList into an XML file
		XMLWriter xmlWriter = new XMLWriter();
	    xmlWriter.xmlConverter(personList, 0);
	    xmlWriter.xmlConverter(customerList, 1);
	    xmlWriter.xmlConverter(productList, 2);
	}
}
