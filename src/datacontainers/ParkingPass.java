package datacontainers;

/**
 * ParkingPass is a subclass of Product containing a constructor and a copy 
 * constructor, getter and setter methods for all data fields. It also 
 * override three methods which are getSubTotal(), getDetail(), and 
 * getAdditionalInfo(). 
 */
public class ParkingPass extends Service {

	private Product ticket = null;
	
	/**
	 * @param productCode
	 * @param cost
	 */
	public ParkingPass(String productCode, double cost) {
		super(productCode, cost);
	}
	
	/**
	 * @param p the old ParkingPass object to make a copy of
	 */
	public ParkingPass(ParkingPass p) {
		super(p.getProductCode(), p.getCost(), p.getQuantity());
		this.ticket = null;
	}

	/**
	 * @return the ticket
	 */
	public Product getTicket() {
		return ticket;
	}

	/**
	 * @param ticket the ticket to set
	 */
	public void setTicket(Product ticket) {
		this.ticket = ticket;
	}

	@Override
	public double getSubTotal() {
		return this.getCost() * Math.max((this.getQuantity() - (this.ticket != null ? 
				this.ticket.getQuantity() : 0)), 0);
	}
	
	@Override
	public String toString() {
		return "ParkingPass" + (this.ticket != null ? " " + this.getTicket().getProductCode() : "");
	}

	@Override
	public String getAdditionalInfo() {
		if (this.ticket == null) 
			return "";
		else
			return " with " + Math.min(this.getQuantity(), this.ticket.getQuantity()) + " free";
	}

}
