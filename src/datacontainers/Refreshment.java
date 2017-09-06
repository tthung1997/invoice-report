package datacontainers;

/**
 * Refreshment is a subclass of Product containing a constructor and 
 * getter, setter methods for all data fields. It also override three
 * methods which are getSubTotal(), toString() and getAdditionalInfo()
 */
public class Refreshment extends Service {

	private String name;
	private boolean discounted = false;
	
	/**
	 * @param productCode
	 * @param name
	 * @param cost
	 */
	public Refreshment(String productCode, String name, double cost) {
		super(productCode, cost);
		this.setName(name);
	}

	/**
	 * @return the discounted
	 */
	public boolean isDiscounted() {
		return discounted;
	}

	/**
	 * @param discounted the discounted to set
	 */
	public void setDiscounted(boolean discounted) {
		this.discounted = discounted;
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

	@Override
	public double getSubTotal() {
		return this.getCost() * this.getQuantity() * (this.isDiscounted() ? 0.95 : 1);
	}
	
	@Override
	public String toString() {
		return this.getName();
	}

	@Override
	public String getAdditionalInfo() {
		if (this.isDiscounted())
			return " with 5% off";
		return "";
	}

}
