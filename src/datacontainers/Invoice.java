package datacontainers;

import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Invoice class contains a constructor, getter and setter methods for all data
 * fields and implements Report interface to print out detailed and summary 
 * report.
 */
public class Invoice implements Report, Comparable<Invoice> {

	private String invoiceCode;
	private Customer customer;
	private Person salesPerson;
	private LocalDateTime thisDate;
	private ArrayList<Product> products;
	
	/**
	 * @param invoiceCode
	 * @param customer
	 * @param salesPerson
	 * @param thisDate
	 * @param products
	 */
	public Invoice(String invoiceCode, Customer customer, Person salesPerson, String thisDate,
			ArrayList<Product> products) {
		super();
		this.setInvoiceCode(invoiceCode);
		this.setCustomer(customer);
		this.setSalesPerson(salesPerson);
		this.setthisDate(thisDate);
		this.setProducts(products);
	}

	/**
	 * @return the invoiceCode
	 */
	public String getInvoiceCode() {
		return invoiceCode;
	}

	/**
	 * @param invoiceCode the invoiceCode to set
	 */
	public void setInvoiceCode(String invoiceCode) {
		this.invoiceCode = invoiceCode;
	}

	/**
	 * @return the customer
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * @param customer the customer to set
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	/**
	 * @return the salesPerson
	 */
	public Person getSalesPerson() {
		return salesPerson;
	}

	/**
	 * @param salesPerson the salesPerson to set
	 */
	public void setSalesPerson(Person salesPerson) {
		this.salesPerson = salesPerson;
	}

	/**
	 * @return the thisDate
	 */
	public LocalDateTime getthisDate() {
		return thisDate;
	}

	/**
	 * @param thisDate the thisDate to set
	 */
	public void setthisDate(String thisDate) {
		DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		this.thisDate = LocalDateTime.parse(thisDate.concat(" 00:00"), f);
	}

	/**
	 * @return the products
	 */
	public ArrayList<Product> getProducts() {
		return products;
	}

	/**
	 * @param products the products to set
	 */
	public void setProducts(ArrayList<Product> products) {
		this.products = products;
	}

	/**
	 * @return additional fee for student customer
	 */
	public double getFee() {
		return (this.customer instanceof Student ? 6.75 : 0.00);
	}

	/**
	 * @return discount for student customer
	 */
	public double getDiscount() {
		return (this.customer instanceof Student ? this.getSubTotal() * 0.08 + this.getTax() : 0.00);
	}

	/**
	 * @return final total based on total money, additional fee and discount
	 */
	public double getFinalTotal() {
		return Report.getTotal(this) + this.getFee() - this.getDiscount();
	}

	@Override
	public double getSubTotal() {
		double subTotal = 0;
		for (Product p: products) {
			if (p instanceof Ticket)
				subTotal += ((Ticket)p).getSubTotal();
			else
				subTotal += ((Service)p).getSubTotal();
		}
		return subTotal;
	}

	@Override
	public double getTax() {
		double tax = 0;
		for (Product p: products) {
			if (p instanceof Ticket)
				tax += ((Ticket)p).getTax();
			else
				tax += ((Service)p).getTax();
		}
		return tax;
	}
	
	/**
	 * @return a string describes the summary report of this invoice
	 */
	public String getSummaryReport() {
		return String.format("%-10s%-50s%-30s$%10.2f $%10.2f $%10.2f $%+10.2f $%10.2f\n", 
					this.getInvoiceCode(), this.getCustomer() != null ? this.getCustomer().getName() + " " + 
					this.getCustomer().toString() : "", this.getSalesPerson() != null ? this.getSalesPerson().getName() : "", 
					this.getSubTotal(),this.getFee(), this.getTax(), -this.getDiscount(), 
					this.getFinalTotal());
	}
	
	/**
	 * @param pw the PrintWriter object used for printing report
	 * This method prints out the detailed report of this invoice
	 */
	public void printDetailedReport(PrintWriter pw) {
		pw.println("Invoice " + this.getInvoiceCode() + "\n========================");
		
		if (this.getSalesPerson() != null)
			pw.println("Salesperson: " + this.getSalesPerson().getName());
		if (this.getCustomer() != null) 
			pw.println(this.getCustomer().getInfo());
		pw.println("-------------------------------------------");

		pw.printf("%-10s%-74s%-16s%-10s%s\n", "Code", "Item", "Subtotal", "Tax", "Total");
		ArrayList<Product> products = this.getProducts();
		for (Product p: products) {
			pw.printf("%-10s%s", p.getProductCode(), p.getDetail());
		}
		pw.printf("%-81s%s\n", "", "====================================");
		
		pw.printf("%-81s%s\n", "SUB-TOTALS", Report.getDetailedVal(this));
		if (this.getCustomer() instanceof Student) {
			pw.printf("%-105s$%+10.2f\n", "DISCOUNT (8% STUDENT & NO TAX)", -this.getDiscount());
			pw.printf("%-105s$%10.2f\n", "ADDITIONAL FEE (STUDENT)", this.getFee());
		}
		pw.printf("%-105s$%10.2f\n", "TOTAL", this.getFinalTotal());
		
		pw.println("\n\t\tThank you for your purchase!\n");
	}

	@Override
	public String toString() {
		String s = "Invoice " + this.getInvoiceCode() + "\n========================\n";
		if (this.getSalesPerson() != null)
			s += "Salesperson: " + this.getSalesPerson().getName() + "\n";
		if (this.getCustomer() != null) 
			s += (this.getCustomer().getInfo()) + "\n";
		s += ("-------------------------------------------\n");

		s += String.format("%-10s%-74s%-16s%-10s%s\n", "Code", "Item", "Subtotal", "Tax", "Total");
		ArrayList<Product> products = this.getProducts();
		for (Product p: products) {
			s += String.format("%-10s%s", p.getProductCode(), p.getDetail());
		}
		s += String.format("%-81s%s\n", "", "====================================");
		
		s += String.format("%-81s%s\n", "SUB-TOTALS", Report.getDetailedVal(this));
		if (this.getCustomer() instanceof Student) {
			s += String.format("%-105s$%+10.2f\n", "DISCOUNT (8% STUDENT & NO TAX)", -this.getDiscount());
			s += String.format("%-105s$%10.2f\n", "ADDITIONAL FEE (STUDENT)", this.getFee());
		}
		s += String.format("%-105s$%10.2f\n", "TOTAL", this.getFinalTotal());
		
		s += "\n\t\tThank you for your purchase!\n\n";
		return s;
	}
	
	@Override
	public int compareTo(Invoice o) {
		if (this.getFinalTotal() > o.getFinalTotal())
			return 1;
		return -1;
	}
	
}
