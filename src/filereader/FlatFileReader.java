package filereader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import datacontainers.Address;
import datacontainers.Customer;
import datacontainers.General;
import datacontainers.Invoice;
import datacontainers.Person;
import datacontainers.Product;
import datacontainers.MovieTicket;
import datacontainers.SeasonPass;
import datacontainers.Student;
import datacontainers.Ticket;
import datacontainers.ParkingPass;
import datacontainers.Refreshment;

/**
 * FlatFileReader class reads and stores data from flat files using three
 * methods which are readPersons(), readCustomers(), and readProducts().
 */
public class FlatFileReader {

	/**
	 * This method reads and stores the data from Persons.dat file into an ArrayList 
	 * @return an ArrayList contains of Person objects
	 */
	public ArrayList<Person> readPersons() {
		Scanner sc = null;

		try {
			sc = new Scanner(new File("data/Persons.dat"));
			sc.nextLine(); 

			ArrayList<Person> personList = new ArrayList<Person>();

			while (sc.hasNext()) {
				String line = sc.nextLine(); 
				String data[] = line.split(";"); 

				String personCode = data[0].trim();
				String name[] = data[1].split(",");
				String lastName = name[0].trim();
				String firstName = name[1].trim();
				String fullAddress[] = data[2].split(","); 
				
				Address address = new Address(fullAddress[0].trim(), 
						fullAddress[1].trim(), fullAddress[2].trim(), 
						fullAddress[3].trim(), fullAddress[4].trim());

				ArrayList<String> emails = new ArrayList<String>();
				if (data.length > 3) {
					String emailSet[] = data[3].split(","); 
					for(int i = 0; i < emailSet.length; i++) 
						emails.add(emailSet[i].trim());
				}
				
				Person person = new Person(personCode, firstName, lastName, 
						address, emails);

				personList.add(person);
			}
			sc.close();
			return personList;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * This method reads and stores the data from Customers.dat file into an ArrayList 
	 * @return an ArrayList contains of Customer objects
	 */
	public ArrayList<Customer> readCustomers(ArrayList<Person> personList) {
		Scanner sc = null;

		try {
			sc = new Scanner(new File("data/Customers.dat"));
			sc.nextLine(); 

			ArrayList<Customer> customerList = new ArrayList<Customer>();

			while (sc.hasNext()) {
				String line = sc.nextLine(); 
				String data[] = line.split(";"); 

				String customerCode = data[0].trim();
				char type = data[1].trim().charAt(0);
				String personCode = data[2].trim();
				String name = data[3].trim();
				String fullAddress[] = data[4].split(",");  
				
				Person primaryContact = null;
				for(Person p: personList) {
					if (p.getPersonCode().equals(personCode)) {
						primaryContact = p;
					}
				}
				
				Address address = new Address(fullAddress[0].trim(), 
						fullAddress[1].trim(), fullAddress[2].trim(), 
						fullAddress[3].trim(), fullAddress[4].trim());
				
				if (type == 'G')
					customerList.add(new General (customerCode, primaryContact, name, address));
				else 
					customerList.add(new Student (customerCode, primaryContact, name, address));

//				customerList.add(customer);
			}
			sc.close();
			return customerList;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * This method reads and stores the data from Products.dat file into an ArrayList 
	 * @return an ArrayList contains of Product objects
	 */
	public ArrayList<Product> readProducts() {
		Scanner sc = null;

		try {
			sc = new Scanner(new File("data/Products.dat"));
			sc.nextLine(); 

			ArrayList<Product> productList = new ArrayList<Product>();

			while (sc.hasNext()) {
				String line = sc.nextLine(); 
				String data[] = line.split(";"); 
				
				Product product = null;

				String productCode = data[0].trim();
				char type = data[1].trim().charAt(0);
				String name = "";
				double cost = 0;
				
				switch (type) {
				case 'M':
					String dateTime = data[2].trim();
					String movieName = data[3].trim();
					String fullAddress[] = data[4].split(",");
					String screenNo = data[5].trim();
					double pricePerUnit = Double.parseDouble(data[6]);
					
					Address address = new Address(fullAddress[0].trim(), 
							fullAddress[1].trim(), fullAddress[2].trim(), 
							fullAddress[3].trim(), fullAddress[4].trim());
					
					product = new MovieTicket(productCode, dateTime, movieName, 
							address, screenNo, pricePerUnit);					
					break;
				case 'S':
					name = data[2].trim();
					String startDate = data[3].trim();
					String endDate = data[4].trim();
					cost = Double.parseDouble(data[5]);
					
					product = new SeasonPass(productCode, name, startDate, 
							endDate, cost);
					break;
				case 'P':
					double parkingFee = Double.parseDouble(data[2]);
					
					product = new ParkingPass(productCode, parkingFee);
					break;
				default:
					name = data[2].trim();
					cost = Double.parseDouble(data[3]);
					
					product = new Refreshment(productCode, name, cost);
				}  

				productList.add(product);
			}
			sc.close();
			return productList;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * This method reads and stores the data from Invoices.dat file into an ArrayList 
	 * @return an ArrayList contains of Invoice objects
	 */
	public ArrayList<Invoice> readInvoices(ArrayList<Person> personList, ArrayList<Customer> customerList,
			ArrayList<Product> productList) {
		Scanner sc = null;

		try {
			sc = new Scanner(new File("data/Invoices.dat"));
			sc.nextLine(); 
			
			ArrayList<Invoice> invoiceList = new ArrayList<Invoice>();

			while (sc.hasNext()) {
				String line = sc.nextLine();

				if (!line.equals("")) {
					String data[] = line.split(";"); 
	
					boolean ticketCheck = false;
					String invoiceCode = data[0].trim();
					String invoiceDate = data[3].trim();
					
					String customerCode = data[1].trim();
					Customer customer = null;
					for (Customer c: customerList) 
						if (c.getCustomerCode().equals(customerCode))
							customer = c;
					
					String personCode = data[2].trim();
					Person person = null;
					for (Person p: personList) 
						if (p.getPersonCode().equals(personCode))
							person = p;
					
					ArrayList<Product> products = new ArrayList<Product>();
					if (data.length > 4) {
						String[] productL = data[4].split(",");
						for (int i = 0; i < productL.length; i++) {
							String[] productInfo = productL[i].split(":");
							
							for (Product p: productList) 
								if (p.getProductCode().equals(productInfo[0].trim())) {
									if (p instanceof Ticket) {
										ticketCheck = true;
										if (p instanceof MovieTicket) {
											MovieTicket q = (MovieTicket)p.clone();
											q.setQuantity(Integer.parseInt(productInfo[1]));
											products.add(q);
										} 
										else {
											SeasonPass q = (SeasonPass)p.clone();
											q.setQuantity(Integer.parseInt(productInfo[1]));
											q.setInvoiceDate(invoiceDate);
											products.add(q);
										}
									}
									else {
										if (p instanceof ParkingPass) {
											ParkingPass q = new ParkingPass((ParkingPass)p);
											q.setQuantity(Integer.parseInt(productInfo[1]));
											products.add(q);
										}
										else {
											Refreshment q = (Refreshment)p.clone();
											q.setQuantity(Integer.parseInt(productInfo[1]));
											products.add(q);
										}
									}
								}
						}
						
						for (int i = 0; i < products.size(); i++) 
							if (products.get(i) instanceof ParkingPass) {
								String[] parkingInfo = productL[i].split(":");
								
								if (parkingInfo.length > 2) {
									for (Product p: products) 
										if (p instanceof Ticket && p.getProductCode().equals(parkingInfo[2])) 
											((ParkingPass)products.get(i)).setTicket(p);
								}
							}
							else if (products.get(i) instanceof Refreshment && ticketCheck) {
								((Refreshment)products.get(i)).setDiscounted(true);
							}
					}
					
					Invoice invoice = new Invoice(invoiceCode, customer, person, invoiceDate, products);
	
					invoiceList.add(invoice);
				}
			}
			sc.close();
			return invoiceList;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

}
