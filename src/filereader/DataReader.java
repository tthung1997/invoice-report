package filereader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.ceg.ext.InvoiceData;

import datacontainers.Address;
import datacontainers.Customer;
import datacontainers.General;
import datacontainers.Invoice;
import datacontainers.LinkedList;
import datacontainers.MovieTicket;
import datacontainers.ParkingPass;
import datacontainers.Person;
import datacontainers.Product;
import datacontainers.Refreshment;
import datacontainers.SeasonPass;
import datacontainers.Student;
import datacontainers.Ticket;

/**
 * DataReader class reads and stores data from database
 */
public class DataReader {

	private static org.apache.log4j.Logger log = Logger.getLogger(DataReader.class);
	
	 /**
     * @param addressID
     * @return an address from the database with the corresponding addressID
     */
    public static Address getAddress(Object addressID) {
    	Connection conn = InvoiceData.getConnection();
    	Address address = null;
    	if (addressID != null) {
	    	PreparedStatement ps;
	    	ResultSet rs;
	    	
	    	try {
	    		String query = "SELECT Addresses.Street AS street, Addresses.City AS city, Addresses.Zip AS zip, StateCountries.State AS state, StateCountries.Country AS country"
	    				+ " FROM Addresses JOIN StateCountries ON Addresses.StateCountryID = StateCountries.StateCountryID WHERE Addresses.AddressID = ?";
	    		ps = conn.prepareStatement(query);
	    		ps.setInt(1, (Integer)addressID);
	    		rs = ps.executeQuery();
	    		
	    		if (rs.next()) {
	    			address = new Address(rs.getString("street"), rs.getString("city"), rs.getString("state"), rs.getString("zip"), rs.getString("country"));
	    		}
	    		
	    		ps.close();
	    		rs.close();
	    	} catch (SQLException e) {
	    		log.error("SQLException: ", e);
				throw new RuntimeException(e);
			}
			
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				log.error("SQLException: ", e);
			}
    	}
		
		return address;
    }
    
    /**
     * @param personCode
     * @return an ArrayList contains all emails of the person corresponding to the personCode
     */
    public static ArrayList<String> getEmails(String personCode) {
    	Connection conn = InvoiceData.getConnection();
    	PreparedStatement ps;
    	ResultSet rs;
    	ArrayList<String> emails = new ArrayList<String>();
    	
    	try {
    		String query = "SELECT PersonEmails.Email AS email FROM Persons JOIN PersonEmails ON Persons.PersonCode = PersonEmails.PersonCode WHERE Persons.PersonCode = ?";
    		ps = conn.prepareStatement(query);
    		ps.setString(1, personCode);
    		rs = ps.executeQuery();
    		while (rs.next()) {
    			emails.add(rs.getString("email"));
    		}
    		ps.close();
    		rs.close();
	    } catch (SQLException e) {
			log.error("SQLException: ", e);
			throw new RuntimeException(e);
		}
		
		try {
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			log.error("SQLException: ", e);
		}
		
		return emails;
    }
    
    /**
	 * This method reads and stores the database into an ArrayList 
	 * @return an ArrayList contains of Person objects
	 */
    public static ArrayList<Person> getPersons() {
    	Connection conn = InvoiceData.getConnection();
    	PreparedStatement ps;
    	ResultSet rs;
    	ArrayList<Person> personList = new ArrayList<Person>();
    	
    	try {
    		String query = "SELECT * FROM Persons";
    		ps = conn.prepareStatement(query);
    		rs = ps.executeQuery();
    		while (rs.next()) {
    			String personCode = rs.getString("PersonCode");
    			String lastName = rs.getString("LastName");
    			String firstName = rs.getString("FirstName");
    			Address address = getAddress(rs.getInt("AddressID"));
    			ArrayList<String> emails = getEmails(personCode);
    			personList.add(new Person(personCode, firstName, lastName, address, emails));
    		}
    		ps.close();
    		rs.close();
    	} catch (SQLException e) {
    		log.error("SQLException: ", e);
			throw new RuntimeException(e);
    	}
    	
    	try {
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			log.error("SQLException: ", e);
		}
    	
    	return personList;
    }
    
    /**
	 * This method reads and stores the data from the database into an ArrayList 
	 * @return an ArrayList contains of Customer objects
	 */
	public static ArrayList<Customer> getCustomers(ArrayList<Person> personList) {
		Connection conn = InvoiceData.getConnection();
    	PreparedStatement ps;
    	ResultSet rs;
    	ArrayList<Customer> customerList = new ArrayList<Customer>();
    	
    	try {
    		String query = "SELECT * FROM Customers";
    		ps = conn.prepareStatement(query);
    		rs = ps.executeQuery();
    		while (rs.next()) {
    			String customerCode = rs.getString("CustomerCode");
    			String type = rs.getString("CustomerType");
    			String personCode = rs.getString("PrimaryContact");
    			Person primaryContact = null;
    			for(Person p: personList) {
    				if (p.getPersonCode().equals(personCode)) {
						primaryContact = p;
					}
    			}
    			String name = rs.getString("CustomerName");
    			Address address = getAddress(rs.getObject("AddressID"));
    			if (type.charAt(0) == 'G')
					customerList.add(new General (customerCode, primaryContact, name, address));
				else 
					customerList.add(new Student (customerCode, primaryContact, name, address));
    		}
    		ps.close();
    		rs.close();
    	} catch (SQLException e) {
    		log.error("SQLException: ", e);
			throw new RuntimeException(e);
    	}
    	
    	try {
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			log.error("SQLException: ", e);
		}
    	
    	return customerList;
	}
	
	/**
	 * This method reads and stores the data from the database into an ArrayList 
	 * @return an ArrayList contains of Product objects
	 */
	public static ArrayList<Product> getProducts() {
		ArrayList<Product> productList = new ArrayList<Product>();
		Connection conn = InvoiceData.getConnection();
    	PreparedStatement ps;
    	ResultSet rs;
    	
    	try {
    		String query = "SELECT * FROM Products";
    		ps = conn.prepareStatement(query);
    		rs = ps.executeQuery();
    		while (rs.next()) {
    			String productCode = rs.getString("ProductCode");
    			String name = "";
    			double cost = rs.getDouble("Cost");
    			switch (rs.getString("ProductType").charAt(0)) {
    			case 'M':
    				String dateTime = rs.getString("MovieDate");
    				name = rs.getString("ProductName");
					Address address = getAddress(rs.getObject("TheaterAddress"));
					String screenNo = rs.getString("Seat");
					productList.add(new MovieTicket(productCode, dateTime, name, address, screenNo, cost));
    				break;
    			case 'S':
    				name = rs.getString("ProductName");
    				String startDate = rs.getString("SeasonStartDate");
    				String endDate = rs.getString("SeasonEndDate");
    				productList.add(new SeasonPass(productCode, name, startDate, endDate, cost));
    				break;
    			case 'P':
    				productList.add(new ParkingPass(productCode, cost));
    				break;
    			case 'R':
    				name = rs.getString("ProductName");
    				productList.add(new Refreshment(productCode, name, cost));
    			}
    		}
    		ps.close();
    		rs.close();
    	} catch (SQLException e) {
    		log.error("SQLException: ", e);
			throw new RuntimeException(e);
    	}
    	
    	try {
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			log.error("SQLException: ", e);
		}
		return productList;
	}
	
	/**
	 * This method reads and stores the data from the database into a Linked List 
	 * @return an Linked List contains of Invoice objects
	 */
	public static LinkedList<Invoice> getInvoices(ArrayList<Person> personList, ArrayList<Customer> customerList, ArrayList<Product> productList) {
		LinkedList<Invoice> invoiceList = new LinkedList<Invoice>();
		Connection conn = InvoiceData.getConnection();
    	PreparedStatement ps1, ps2;
    	ResultSet rs1, rs2;
    	
    	try {
    		String query1 = "SELECT * FROM Invoices";
    		ps1 = conn.prepareStatement(query1);
    		rs1 = ps1.executeQuery();
    		while (rs1.next()) {
    			String invoiceCode = rs1.getString("InvoiceCode");
    			String customerCode = rs1.getString("CustomerCode");
    			Customer customer = null;
    			for (Customer c: customerList) 
					if (c.getCustomerCode().equals(customerCode))
						customer = c;
    			String personCode = rs1.getString("SalespersonCode");
    			Person person = null;
				for (Person p: personList) 
					if (p.getPersonCode().equals(personCode))
						person = p;
    			String invoiceDate = rs1.getString("InvoiceDate");
    			ArrayList<Product> products = new ArrayList<Product>();
    			boolean ticketCheck = false;
    			String query2 = "SELECT * FROM InvoiceProducts WHERE InvoiceCode = ? ORDER BY MovieTicket";
    			ps2 = conn.prepareStatement(query2);
    			ps2.setString(1, invoiceCode);
    			rs2 = ps2.executeQuery();
    			while (rs2.next()) {
    				for(Product p: productList) {
    					if (p.getProductCode().equals(rs2.getString("ProductCode"))) {
    						if (p instanceof Ticket) {
    							ticketCheck = true;
								if (p instanceof MovieTicket) {
									MovieTicket q = (MovieTicket)p.clone();
									q.setQuantity(rs2.getInt("Quantity"));
									products.add(q);
								} 
								else {
									SeasonPass q = (SeasonPass)p.clone();
									q.setQuantity(rs2.getInt("Quantity"));
									q.setInvoiceDate(invoiceDate);
									products.add(q);
								}
    						}
    						else {
								if (p instanceof ParkingPass) {
									ParkingPass q = new ParkingPass((ParkingPass)p);
									q.setQuantity(rs2.getInt("Quantity"));
									String ticketCode = rs2.getString("MovieTicket");
									if (ticketCode != null)
										for(Product p1: products) {
											if (p1.getProductCode().equals(ticketCode)) {
												q.setTicket(p1);
											}
										}
									products.add(q);
								}
								else {
									Refreshment q = (Refreshment)p.clone();
									q.setQuantity(rs2.getInt("Quantity"));
									products.add(q);
								}
							}
    					}
    				}
    			}
    			if (ticketCheck) 
	    			for(int i = 0; i < products.size(); ++i) 
	    				if (products.get(i) instanceof Refreshment) {
							((Refreshment)products.get(i)).setDiscounted(true);
						}
    			ps2.close();
    			rs2.close();
    			invoiceList.addItem(new Invoice(invoiceCode, customer, person, invoiceDate, products));
    		}
    		ps1.close();
    		rs1.close();
    	} catch (SQLException e) {
    		log.error("SQLException: ", e);
			throw new RuntimeException(e);
    	}
    	
    	try {
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			log.error("SQLException: ", e);
		}
    	
		return invoiceList;
	}
	
}
