package datacontainers;

/**
 * Service is a subclass of Product class which has two constructors and overrides
 * two methods including getTax() and getDetail()
 */
public abstract class Service extends Product {

	/**
	 * @param productCode
	 * @param cost
	 * @param quantity
	 */
	public Service(String productCode, double cost, int quantity) {
		super(productCode, cost, quantity);
	}
	
	/**
	 * @param productCode
	 * @param cost
	 */
	public Service(String productCode, double cost) {
		super(productCode, cost);
	}

	@Override
	public double getTax() {
		return this.getSubTotal() * 0.04;
	}
	
	@Override
	public String getDetail() {
		return String.format("%-71s%s\n", this.toString() + " (" + 
				String.format("%d units @ $%.2f/unit", this.getQuantity(), this.getCost()) + 
				this.getAdditionalInfo() + ")",	Report.getDetailedVal(this));
	}
	
}
