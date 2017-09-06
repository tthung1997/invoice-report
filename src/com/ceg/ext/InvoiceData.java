package com.ceg.ext;

import java.sql.*;

import org.apache.log4j.Logger;

/*
 * This is a collection of utility methods that define a general API for
 * interacting with the database supporting this application.
 * 15 methods in total, add more if required.
 * Donot change any method signatures or the package name.
 * 
 */

public class InvoiceData {

	private static final String url = "jdbc:mysql://cse.unl.edu/ttran";
	private static final String username = "ttran";
	private static final String password = "ht221097.";
	private static org.apache.log4j.Logger log = Logger.getLogger(InvoiceData.class);
	
	static public Connection getConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (InstantiationException e) {
			System.out.println("InstantiationException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			System.out.println("IllegalAccessException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		Connection conn = null;

		try {
			conn = DriverManager.getConnection(InvoiceData.url, InvoiceData.username, InvoiceData.password);
		} catch (SQLException e) {
			log.error("SQLException: ", e);
			throw new RuntimeException(e);
		}
		
		return conn;
	}
	
	/**
	 * 1. Method that removes every person record from the database
	 */
	public static void removeAllPersons() {
		Connection conn = InvoiceData.getConnection();
		PreparedStatement ps;
		
		try {
			String delete = "DELETE FROM PersonEmails";
			ps = conn.prepareStatement(delete);
			ps.executeUpdate();
			ps.close();
			
			String update = "UPDATE Customers SET PrimaryContact = NULL";
			ps = conn.prepareStatement(update);
			ps.executeUpdate();
			ps.close();
			
			update = "UPDATE Invoices SET SalespersonCode = NULL";
			ps = conn.prepareStatement(update);
			ps.executeUpdate();
			ps.close();
			
			delete = "DELETE FROM Persons";		
			ps = conn.prepareStatement(delete);
			ps.executeUpdate();
			ps.close();
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
	
	/**
	 * 2. Method to add a person record to the database with the provided data.
	 * 
	 * @param personCode
	 * @param firstName
	 * @param lastName
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 * @param country
	 */
	public static void addPerson(String personCode, String firstName, String lastName, String street, String city, String state, String zip, String country) {
		Connection conn = InvoiceData.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		
		try {
			int stateCountryID;
			String stateCountryQuery = "SELECT StateCountryID FROM StateCountries WHERE State = ? AND Country = ?";
			ps = conn.prepareStatement(stateCountryQuery);
			ps.setString(1, state);
			ps.setString(2, country);	
			rs = ps.executeQuery();
			
			if (rs.next()) {
				stateCountryID = rs.getInt("StateCountryID");
				rs.close();
				ps.close();
			}
			else {
				rs.close();
				ps.close();

				String insertStateCountry = "INSERT INTO StateCountries (State, Country) values (?, ?)";
				ps = conn.prepareStatement(insertStateCountry);
				ps.setString(1, state);
				ps.setString(2, country);
				ps.executeUpdate();
				ps.close();

				ps = conn.prepareStatement(stateCountryQuery);
				ps.setString(1, state);
				ps.setString(2, country);	
				rs = ps.executeQuery();

				rs.next();
				stateCountryID = rs.getInt("StateCountryID");
				rs.close();
				ps.close();
			}
			
			int addressID;
			String addressQuery = "SELECT AddressID FROM Addresses WHERE Street = ? AND City = ? AND Zip = ? AND StateCountryID = ?";
			ps = conn.prepareStatement(addressQuery);
			ps.setString(1, street);
			ps.setString(2, city);
			ps.setString(3, zip);
			ps.setInt(4, stateCountryID);
			rs = ps.executeQuery();
			
			if (rs.next()) {
				addressID = rs.getInt("AddressID");
				rs.close();
				ps.close();
			}
			else {
				rs.close();
				ps.close();

				String insertAddress = "INSERT INTO Addresses (Street, City, Zip, StateCountryID) values (?, ?, ?, ?)";
				ps = conn.prepareStatement(insertAddress);
				ps.setString(1, street);
				ps.setString(2, city);
				ps.setString(3, zip);
				ps.setInt(4, stateCountryID);
				ps.executeUpdate();
				ps.close();

				ps = conn.prepareStatement(addressQuery);
				ps.setString(1, street);
				ps.setString(2, city);
				ps.setString(3, zip);
				ps.setInt(4, stateCountryID);	
				rs = ps.executeQuery();

				rs.next();
				addressID = rs.getInt("AddressID");
				rs.close();
				ps.close();
			}
			
			String insertPerson = "INSERT INTO Persons values (?, ?, ?, ?)";
			ps = conn.prepareStatement(insertPerson);
			ps.setString(1, personCode);
			ps.setString(2, lastName);
			ps.setString(3, firstName);
			ps.setInt(4, addressID);
			ps.executeUpdate();
			ps.close();
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

	/**
	 * 3. Adds an email record corresponding person record corresponding to the
	 * provided <code>personCode</code>
	 * 
	 * @param personCode
	 * @param email
	 */
	public static void addEmail(String personCode, String email) {
		Connection conn = InvoiceData.getConnection();
		PreparedStatement ps;
		
		try {
			String insertEmail = "INSERT INTO PersonEmails VALUES (?, ?)";
			ps = conn.prepareStatement(insertEmail);
			ps.setString(1, personCode);
			ps.setString(2, email);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		try {
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			log.error("SQLException: ", e);
		}
	}

	/**
	 * 4. Method that removes every customer record from the database
	 */
	public static void removeAllCustomers() {
		Connection conn = InvoiceData.getConnection();
		PreparedStatement ps;
		
		try {
			String update = "UPDATE Invoices SET CustomerCode = NULL";
			ps = conn.prepareStatement(update);
			ps.executeUpdate();
			ps.close();
			
			String delete = "DELETE FROM Customers";		
			ps = conn.prepareStatement(delete);
			ps.executeUpdate();
			ps.close();
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

	public static void addCustomer(String customerCode, String customerType, String primaryContactPersonCode, String name, String street, String city, String state, String zip, String country) {
		Connection conn = InvoiceData.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		
		try {
			int stateCountryID;
			String stateCountryQuery = "SELECT StateCountryID FROM StateCountries WHERE State = ? AND Country = ?";
			ps = conn.prepareStatement(stateCountryQuery);
			ps.setString(1, state);
			ps.setString(2, country);	
			rs = ps.executeQuery();
			
			if (rs.next()) {
				stateCountryID = rs.getInt("StateCountryID");
				rs.close();
				ps.close();
			}
			else {
				rs.close();
				ps.close();

				String insertStateCountry = "INSERT INTO StateCountries (State, Country) values (?, ?)";
				ps = conn.prepareStatement(insertStateCountry);
				ps.setString(1, state);
				ps.setString(2, country);
				ps.executeUpdate();
				ps.close();

				ps = conn.prepareStatement(stateCountryQuery);
				ps.setString(1, state);
				ps.setString(2, country);	
				rs = ps.executeQuery();

				rs.next();
				stateCountryID = rs.getInt("StateCountryID");
				rs.close();
				ps.close();
			}
			
			int addressID;
			String addressQuery = "SELECT AddressID FROM Addresses WHERE Street = ? AND City = ? AND Zip = ? AND StateCountryID = ?";
			ps = conn.prepareStatement(addressQuery);
			ps.setString(1, street);
			ps.setString(2, city);
			ps.setString(3, zip);
			ps.setInt(4, stateCountryID);
			rs = ps.executeQuery();
			
			if (rs.next()) {
				addressID = rs.getInt("AddressID");
				rs.close();
				ps.close();
			}
			else {
				rs.close();
				ps.close();

				String insertAddress = "INSERT INTO Addresses (Street, City, Zip, StateCountryID) values (?, ?, ?, ?)";
				ps = conn.prepareStatement(insertAddress);
				ps.setString(1, street);
				ps.setString(2, city);
				ps.setString(3, zip);
				ps.setInt(4, stateCountryID);
				ps.executeUpdate();
				ps.close();

				ps = conn.prepareStatement(addressQuery);
				ps.setString(1, street);
				ps.setString(2, city);
				ps.setString(3, zip);
				ps.setInt(4, stateCountryID);	
				rs = ps.executeQuery();

				rs.next();
				addressID = rs.getInt("AddressID");
				rs.close();
				ps.close();
			}
			
			String insertCustomer = "INSERT INTO Customers values (?, ?, ?, ?, ?)";
			ps = conn.prepareStatement(insertCustomer);
			ps.setString(1, customerCode);
			ps.setString(2, "" + customerType.charAt(0));
			ps.setString(3, primaryContactPersonCode);
			ps.setString(4, name);
			ps.setInt(5, addressID);
			ps.executeUpdate();
			ps.close();
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
	
	/**
	 * 5. Removes all product records from the database
	 */
	public static void removeAllProducts() {
		Connection conn = InvoiceData.getConnection();
		PreparedStatement ps;
		
		try {
			String delete = "DELETE FROM InvoiceProducts";
			ps = conn.prepareStatement(delete);
			ps.executeUpdate();
			ps.close();
			
			delete = "DELETE FROM Products";		
			ps = conn.prepareStatement(delete);
			ps.executeUpdate();
			ps.close();
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

	public static void addProducts(String productCode, String productType, String productName, String movieDate, Integer theaterAddress, String seat, String startDate, String endDate, Double cost) {
		Connection conn = InvoiceData.getConnection();
		PreparedStatement ps;
		
		try {
			String insertMovieTicket = "INSERT INTO Products values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			ps = conn.prepareStatement(insertMovieTicket);
			ps.setString(1, productCode);
			ps.setString(2, productType);
			ps.setString(3, productName);
			ps.setString(4, movieDate);
			ps.setObject(5, theaterAddress);
			ps.setString(6, seat);
			ps.setString(7, startDate);
			ps.setString(8, endDate);
			ps.setDouble(9, cost);
			ps.executeUpdate();
			ps.close();
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
	
	/**
	 * 6. Adds an movieTicket record to the database with the provided data.
	 */
	public static void addMovieTicket(String productCode, String dateTime, String movieName, String street, String city,String state, String zip, String country, String screenNo, double pricePerUnit) {
		Connection conn = InvoiceData.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		
		try {
			int stateCountryID;
			String stateCountryQuery = "SELECT StateCountryID FROM StateCountries WHERE State = ? AND Country = ?";
			ps = conn.prepareStatement(stateCountryQuery);
			ps.setString(1, state);
			ps.setString(2, country);	
			rs = ps.executeQuery();
			
			if (rs.next()) {
				stateCountryID = rs.getInt("StateCountryID");
				rs.close();
				ps.close();
			}
			else {
				rs.close();
				ps.close();

				String insertStateCountry = "INSERT INTO StateCountries (State, Country) values (?, ?)";
				ps = conn.prepareStatement(insertStateCountry);
				ps.setString(1, state);
				ps.setString(2, country);
				ps.executeUpdate();
				ps.close();

				ps = conn.prepareStatement(stateCountryQuery);
				ps.setString(1, state);
				ps.setString(2, country);	
				rs = ps.executeQuery();

				rs.next();
				stateCountryID = rs.getInt("StateCountryID");
				rs.close();
				ps.close();
			}
			
			int addressID;
			String addressQuery = "SELECT AddressID FROM Addresses WHERE Street = ? AND City = ? AND Zip = ? AND StateCountryID = ?";
			ps = conn.prepareStatement(addressQuery);
			ps.setString(1, street);
			ps.setString(2, city);
			ps.setString(3, zip);
			ps.setInt(4, stateCountryID);
			rs = ps.executeQuery();
			
			if (rs.next()) {
				addressID = rs.getInt("AddressID");
				rs.close();
				ps.close();
			}
			else {
				rs.close();
				ps.close();

				String insertAddress = "INSERT INTO Addresses (Street, City, Zip, StateCountryID) values (?, ?, ?, ?)";
				ps = conn.prepareStatement(insertAddress);
				ps.setString(1, street);
				ps.setString(2, city);
				ps.setString(3, zip);
				ps.setInt(4, stateCountryID);
				ps.executeUpdate();
				ps.close();

				ps = conn.prepareStatement(addressQuery);
				ps.setString(1, street);
				ps.setString(2, city);
				ps.setString(3, zip);
				ps.setInt(4, stateCountryID);	
				rs = ps.executeQuery();

				rs.next();
				addressID = rs.getInt("AddressID");
				rs.close();
				ps.close();
			}
			
			InvoiceData.addProducts(productCode, "M", movieName, dateTime, addressID, screenNo, null, null, pricePerUnit);
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

	/**
	 * 7. Adds a seasonPass record to the database with the provided data.
	 */
	public static void addSeasonPass(String productCode, String name, String seasonStartDate, String seasonEndDate,	double cost) {
		InvoiceData.addProducts(productCode, "S", name, null, null, null, seasonStartDate, seasonEndDate, cost);
	}

	/**
	 * 8. Adds a ParkingPass record to the database with the provided data.
	 */
	public static void addParkingPass(String productCode, double parkingFee) {
		InvoiceData.addProducts(productCode, "P", null, null, null, null, null, null, parkingFee);
	}

	/**
	 * 9. Adds a refreshment record to the database with the provided data.
	 */
	public static void addRefreshment(String productCode, String name, double cost) {
		InvoiceData.addProducts(productCode, "R", name, null, null, null, null, null, cost);
	}

	/**
	 * 10. Removes all invoice records from the database
	 */
	public static void removeAllInvoices() {
		Connection conn = InvoiceData.getConnection();
		PreparedStatement ps;
		
		try {
			String delete = "DELETE FROM InvoiceProducts";
			ps = conn.prepareStatement(delete);
			ps.executeUpdate();
			ps.close();
			
			delete = "DELETE FROM Invoices";		
			ps = conn.prepareStatement(delete);
			ps.executeUpdate();
			ps.close();
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

	/**
	 * 11. Adds an invoice record to the database with the given data.
	 */
	public static void addInvoice(String invoiceCode, String customerCode, String salesPersonCode, String invoiceDate) {
		Connection conn = InvoiceData.getConnection();
		PreparedStatement ps;
		
		try {
			String insertInvoice = "INSERT INTO Invoices values (?, ?, ?, ?)";
			ps = conn.prepareStatement(insertInvoice);
			ps.setString(1, invoiceCode);
			ps.setString(2, customerCode);
			ps.setString(3, salesPersonCode);
			ps.setString(4, invoiceDate);
			ps.executeUpdate();
			ps.close();
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

	public static void addInvoiceProducts(String invoiceCode, String productCode, int quantity, String movieTicket) {
		Connection conn = InvoiceData.getConnection();
		PreparedStatement ps;
		
		try {
			String insertInvoiceProduct = "INSERT INTO InvoiceProducts(InvoiceCode, ProductCode, Quantity, MovieTicket) values (?, ?, ?, ?)";
			ps = conn.prepareStatement(insertInvoiceProduct);
			ps.setString(1, invoiceCode);
			ps.setString(2, productCode);
			ps.setInt(3, quantity);
			ps.setString(4, movieTicket);
			ps.executeUpdate();
			ps.close();
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
	
	/**
	 * 12. Adds a particular movieticket (corresponding to <code>productCode</code>
	 * to an invoice corresponding to the provided <code>invoiceCode</code> with
	 * the given number of units
	 */

	public static void addMovieTicketToInvoice(String invoiceCode, String productCode, int quantity) {
		InvoiceData.addInvoiceProducts(invoiceCode, productCode, quantity, null);
	}

	/*
	 * 13. Adds a particular seasonpass (corresponding to <code>productCode</code>
	 * to an invoice corresponding to the provided <code>invoiceCode</code> with
	 * the given begin/end dates
	 */
	public static void addSeasonPassToInvoice(String invoiceCode, String productCode, int quantity) {
		InvoiceData.addInvoiceProducts(invoiceCode, productCode, quantity, null);
	}

     /**
     * 14. Adds a particular ParkingPass (corresponding to <code>productCode</code> to an 
     * invoice corresponding to the provided <code>invoiceCode</code> with the given
     * number of quantity.
     * NOTE: ticketCode may be null
     */
    public static void addParkingPassToInvoice(String invoiceCode, String productCode, int quantity, String ticketCode) {
    	InvoiceData.addInvoiceProducts(invoiceCode, productCode, quantity, ticketCode);
    }
	
    /**
     * 15. Adds a particular refreshment (corresponding to <code>productCode</code> to an 
     * invoice corresponding to the provided <code>invoiceCode</code> with the given
     * number of quantity. 
     */
    public static void addRefreshmentToInvoice(String invoiceCode, String productCode, int quantity) {
    	InvoiceData.addInvoiceProducts(invoiceCode, productCode, quantity, null);
    }
    
}