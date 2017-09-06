package datacontainers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * MovieTicket is a subclass of Product containing a constructor and 
 * getter, setter methods for all data fields. It also override three
 * methods which are getSubTotal(), getDetail(), and getAdditionalInfo()
 */
public class MovieTicket extends Ticket {
	
	private LocalDateTime dateTime;
	private String movieName;
	private Address address;
	private String screenNo;
	
	/**
	 * @param productCode
	 * @param dateTime
	 * @param movieName
	 * @param address
	 * @param screenNo
	 * @param pricePerUnit
	 * @param quantity
	 */
	public MovieTicket(String productCode, String dateTime, 
			String movieName, Address address, String screenNo, 
			double pricePerUnit) {
		super(productCode, pricePerUnit);
		this.setDateTime(dateTime);
		this.setMovieName(movieName);
		this.setAddress(address);
		this.setscreenNo(screenNo);
	}

	/**
	 * @return the dateTime
	 */
	public LocalDateTime getDateTime() {
		return dateTime;
	}

	/**
	 * @param dateTime the dateTime to set
	 */
	public void setDateTime(String dateTime) {
		DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		this.dateTime = LocalDateTime.parse(dateTime, f);
	}

	/**
	 * @return the movieName
	 */
	public String getMovieName() {
		return movieName;
	}
	
	/**
	 * @param movieName the movieName to set
	 */
	public void setMovieName(String movieName) {
		this.movieName = movieName;
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
	 * @return the screenNo
	 */
	public String getscreenNo() {
		return screenNo;
	}
	
	/**
	 * @param screenNo the screenNo to set
	 */
	public void setscreenNo(String screenNo) {
		this.screenNo = screenNo;
	}

	/**
	 * @return true if dateTime is Tuesday or Thursday
	 */
	public boolean isDiscounted() {
		int dayOfWeek = this.dateTime.getDayOfWeek().getValue();
		return (dayOfWeek == 2 || dayOfWeek == 4);
	}
	
	@Override
	public double getSubTotal() {
		return this.getCost() * this.getQuantity() * (this.isDiscounted() ? 0.93 : 1);
	}

	@Override
	public String getDetail() {
		return String.format("%-71s%s\n%10s%s\n", "Movie Ticket '" + this.getMovieName() + 
				(this.getAddress() != null ? "' @ " + this.getAddress().getStreet() : ""), Report.getDetailedVal(this), "",
				Ticket.dateToString(this.getDateTime()) + " " + this.getPriceDetailed());
	}

	@Override
	public String getAdditionalInfo() {
		if (this.isDiscounted()) 
			return " - Tue/Thu 7% off";
		return "";
	}

}
