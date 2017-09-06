package datacontainers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * SeasonPass is a subclass of Product containing a constructor and 
 * getter, setter methods for all data fields. It also override four
 * methods which are getSubTotal(), getDetail(), getAdditionalInfo(), 
 * and toString()
 */
public class SeasonPass extends Ticket {

	private String name;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private LocalDateTime invoiceDate = null;
	
	/**
	 * @param productCode
	 * @param name
	 * @param startDate
	 * @param endDate
	 * @param cost
	 */
	public SeasonPass(String productCode, String name, String startDate, 
			String endDate, double cost) {
		super(productCode, cost);
		this.setName(name);
		this.setStartDate(startDate);
		this.setEndDate(endDate);
	}
	
	/**
	 * @return the invoiceDate
	 */
	public LocalDateTime getInvoiceDate() {
		return invoiceDate;
	}

	/**
	 * @param invoiceDate the invoiceDate to set
	 */
	public void setInvoiceDate(String invoiceDate) {
		DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		this.invoiceDate = LocalDateTime.parse(invoiceDate.concat(" 00:01"), f);
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
	 * @return the startDate
	 */
	public LocalDateTime getStartDate() {
		return startDate;
	}
	
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		this.startDate = LocalDateTime.parse(startDate.concat(" 00:00"), f);
	}
	
	/**
	 * @return the endDate
	 */
	public LocalDateTime getEndDate() {
		return endDate;
	}
	
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		this.endDate = LocalDateTime.parse(endDate.concat(" 23:59"), f);
	}

	@Override
	public double getSubTotal() {
		double subTotal = 8.0 * this.getQuantity();
		if (this.invoiceDate.isBefore(this.startDate)) 
			return subTotal + this.getCost() * this.getQuantity();
		else {
			if (this.invoiceDate.isAfter(this.endDate))
				return 0;
			else 
				return subTotal + this.getCost() * 
						(1.0 * this.invoiceDate.until(this.endDate, ChronoUnit.DAYS) / 
								this.startDate.until(this.endDate, ChronoUnit.DAYS)) * this.getQuantity();
		}
	}

	@Override
	public String toString() {
		return "SeasonPass - " + this.getName();
	}

	@Override
	public String getAdditionalInfo() {
		String s = " + $8 fee/unit";
		if (this.invoiceDate.isBefore(this.startDate))
			return s;
		else 
			return " prorated " + this.invoiceDate.until(this.endDate, ChronoUnit.DAYS) + "/" +
				this.startDate.until(this.endDate, ChronoUnit.DAYS) + " days" + s;
	}

	@Override
	public String getDetail() {
		return String.format("%-71s%s\n%10s%s\n", this.toString(), Report.getDetailedVal(this), "", 
				"(" + String.format("%d units @ $%.2f/unit", this.getQuantity(), this.getCost()) + 
				this.getAdditionalInfo() + ")");
	}
	
}
