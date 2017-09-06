package datacontainers;

/**
 * Student is a subclass of Customer method. It overrides only the toString() 
 * method to return customer type. 
 */

public class Student extends Customer {

	/**
	 * @param customerCode
	 * @param primaryContact
	 * @param name
	 * @param address
	 */
	public Student(String customerCode, Person primaryContact, String name, Address address) {
		super(customerCode, primaryContact, name, address);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "[Student]";
	}
	
}
