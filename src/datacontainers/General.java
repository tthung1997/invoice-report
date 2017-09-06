package datacontainers;

/**
 * General is a subclass of Customer method. It overrides only the toString() 
 * method to return customer type. 
 */
public class General extends Customer {

	/**
	 * @param customerCode
	 * @param type
	 * @param primaryContact
	 * @param name
	 * @param address
	 */
	public General(String customerCode, Person primaryContact, String name, Address address) {
		super(customerCode, primaryContact, name, address);
	}
	
	@Override
	public String toString() {
		return "[General]";
	}
	
}
