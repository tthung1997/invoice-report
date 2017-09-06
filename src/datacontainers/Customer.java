package datacontainers;

/**
 * Customer is the parent class containing a constructor and getter, setter
 * methods for all data fields.
 */
public abstract class Customer {

	private String customerCode;
	private Person primaryContact;
	private String name;
	private Address address;
	
	/**
	 * @param customerCode
	 * @param primaryContact
	 * @param name
	 * @param address
	 */
	public Customer(String customerCode, Person primaryContact, String name, Address address) {
		super();
		this.setCustomerCode(customerCode);
		this.setPrimaryContact(primaryContact);
		this.setName(name);
		this.setAddress(address);
	}

	/**
	 * @return the customerCode
	 */
	public String getCustomerCode() {
		return customerCode;
	}

	/**
	 * @param customerCode the customerCode to set
	 */
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	/**
	 * @return the primaryContact
	 */
	public Person getPrimaryContact() {
		return primaryContact;
	}

	/**
	 * @param primaryContact the primaryContact to set
	 */
	public void setPrimaryContact(Person primaryContact) {
		this.primaryContact = primaryContact;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the address
	 */
	public Address getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(Address address) {
		this.address = address;
	}

	/**
	 * @return the String describes customer's info
	 */
	public String getInfo() {
		return "Customer Info:\n  " + 
				this.getName() + " (" + this.customerCode + ")\n  " +
				this.toString() + "\n" +
				(this.getPrimaryContact() != null ? "  " + this.getPrimaryContact().getName() + "\n" : "") +
				(this.getAddress() != null ? "  " + this.getAddress().getStreet() + "\n  " +
				this.getAddress().getAddressWoStreet() : "");
	}
	
}
