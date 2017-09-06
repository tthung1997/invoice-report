package datacontainers;

/**
 * Product is the abstract class containing a constructor and getter, setter
 * methods for all data fields. It implements Cloneable and Report
 * interfaces. It also has several methods serving for this product's report.
 * Product class is extended in two different classes including Ticket and 
 * Service.
 */
public abstract class Product implements Cloneable, Report {
	
	private String productCode;
	private int quantity = 0;
	private double cost;

	/**
	 * @param productCode
	 * @param cost
	 * @param quantity
	 */
	public Product(String productCode, double cost, int quantity) {
		super();
		this.setProductCode(productCode);
		this.setCost(cost);
		this.setQuantity(quantity);
	}
	
	/**
	 * @param productCode
	 * @param cost
	 */
	public Product(String productCode, double cost) {
		super();
		this.setProductCode(productCode);
		this.setCost(cost);
	}

	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the cost
	 */
	public double getCost() {
		return cost;
	}

	/**
	 * @param cost the cost to set
	 */
	public void setCost(double cost) {
		this.cost = cost;
	}

	/**
	 * @return the productCode
	 */
	public String getProductCode() {
		return productCode;
	}

	/**
	 * @param productCode the productCode to set
	 */
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	
	/**
	 * @return detailed price including price per unit and additional info
	 */
	public String getPriceDetailed() {
		return "(" + String.format("%d units @ $%.2f/unit", this.getQuantity(), this.getCost()) + 
				this.getAdditionalInfo() + ")";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {  
		try {  
			return super.clone();  
	    } catch(Exception e) { 
	        return null; 
	    }
	}
	
	/**
	 * @return additional info of this product including possible discount
	 */
	abstract public String getAdditionalInfo();
	
	/**
	 * @return full detailed report of this product
	 */
	abstract public String getDetail();
	
}
