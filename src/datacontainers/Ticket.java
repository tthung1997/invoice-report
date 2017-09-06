package datacontainers;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * Ticket is a subclass of Product class which has two constructors, overrides
 * getTax() method, and implements dateToString() static method which is used
 * for MovieTicket class and SeasonPass class.
 */
public abstract class Ticket extends Product {
	
	/**
	 * @param productCode
	 * @param cost
	 * @param quantity
	 */
	public Ticket(String productCode, double cost, int quantity) {
		super(productCode, cost, quantity);
	}
	
	/**
	 * @param productCode
	 * @param cost
	 */
	public Ticket(String productCode, double cost) {
		super(productCode, cost);
	}
	
	@Override
	public double getTax() {
		return this.getSubTotal() * 0.06;
	}
	
	/**
	 * @param date the LocalDateTime object to transfer to string
	 * @return a string describes full detail of date
	 */
	public static String dateToString(LocalDateTime date) {
		return date.getMonth().getDisplayName(TextStyle.SHORT, Locale.US) + " " + date.getDayOfMonth() + 
				", " + date.getYear() + " " + date.getHour() + ":" + date.getMinute();
	}
	
}
