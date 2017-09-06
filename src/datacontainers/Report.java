package datacontainers;

/**
 * This interface provides several method signatures used for generating report
 */
public interface Report {
	
	/**
	 * @return the subTotal
	 */
	public double getSubTotal();
	
	/**
	 * @return the tax
	 */
	public double getTax();
	
	/**
	 * @param r an object of all classes which implement Report interface
	 * @return the total including subTotal and tax
	 */
	public static <T extends Report> double getTotal(T r) {
		return r.getSubTotal() + r.getTax();
	}
	
	/**
	 * @param r an object of all classes which implement Report interface
	 * @return a string describes all the values including subTotal, tax, 
	 * and total
	 */
	public static <T extends Report> String getDetailedVal (T r) {
		return String.format("$%10.2f $%10.2f $%10.2f", r.getSubTotal(), 
				r.getTax(), Report.getTotal(r));
	}
	
}
