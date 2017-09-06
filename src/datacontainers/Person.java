package datacontainers;

import java.util.ArrayList;

/**
 * Person is the parent class containing a constructor and getter, setter
 * methods for all data fields.
 */
public class Person {
	
	private String personCode;
	private String firstName;
	private String lastName;
	private Address address;
	private ArrayList<String> emails;
	/**
	 * @param personCode
	 * @param firstName
	 * @param lastName
	 * @param address
	 * @param emails
	 */
	public Person(String personCode, String firstName, String lastName, Address address, ArrayList<String> emails) {
		super();
		this.setPersonCode(personCode);
		this.setFirstName(firstName);
		this.setLastName(lastName);
		this.setAddress(address);
		this.setEmails(emails);
	}
	/**
	 * @return the personCode
	 */
	public String getPersonCode() {
		return personCode;
	}
	/**
	 * @param personCode the personCode to set
	 */
	public void setPersonCode(String personCode) {
		this.personCode = personCode;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
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
	 * @return the emails
	 */
	public ArrayList<String> getEmails() {
		return emails;
	}
	/**
	 * @param emails the emails to set
	 */
	public void setEmails(ArrayList<String> emails) {
		this.emails = emails;
	} 
	
	/**
	 * @return the full name of this person
	 */
	public String getName() {
		return this.getLastName() + ", " + this.getFirstName();
	}
	
}
