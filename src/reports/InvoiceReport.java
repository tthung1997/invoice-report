package reports;

import java.io.PrintWriter;
import java.util.ArrayList;

import org.apache.log4j.BasicConfigurator;

import datacontainers.Customer;
import datacontainers.Invoice;
import datacontainers.LinkedList;
import datacontainers.Person;
import datacontainers.Product;
import filereader.DataReader;

/**
 * InvoiceReport class is the driver class containing the main method to read data
 * from the database and to generate summary and detailed reports for all invoices.
 */
public class InvoiceReport {

	public static void main(String[] args) {
		
		BasicConfigurator.configure();
		PrintWriter pw =  new PrintWriter (System.out);
		
		ArrayList<Person> personList = DataReader.getPersons();
		ArrayList<Customer> customerList = DataReader.getCustomers(personList);
		ArrayList<Product> productList = DataReader.getProducts();
		LinkedList<Invoice> invoiceList = DataReader.getInvoices(personList, customerList, productList);		
		
		printSummaryReport(pw, invoiceList);
		printDetailedReport(pw, invoiceList);
		
		pw.close();
		
	}
	
	/**
	 * @param pw
	 * This method uses a PrintWriter object to print out a summary report of all
	 * invoices
	 */
    public static void printSummaryReport(PrintWriter pw, LinkedList<Invoice> invoiceList) {
    	double tSubTotal = 0;
		double tFee = 0;
		double tTaxes = 0;
		double tDiscount = 0;
		double tTotal = 0;
		
		pw.println("=========================\n" + 
				"Executive Summary Report (Sorted by Invoice Total)\n" + 
				"=========================");
		
		pw.printf("%-10s%-50s%-33s%-16s%-11s%-9s%-15s%s\n", "Invoice", "Customer", 
				"Salesperson", "Subtotal", "Fees", "Taxes", "Discount", "Total");
		
		for (Invoice inv: invoiceList) {
			pw.printf(inv.getSummaryReport());
			tSubTotal += inv.getSubTotal();
			tFee += inv.getFee();
			tTaxes += inv.getTax();
			tDiscount += inv.getDiscount();
			tTotal += inv.getFinalTotal();
		}
		pw.println("==================================================================="
				+ "==================================================================================");
		
		pw.printf("%-90s$%10.2f $%10.2f $%10.2f $%+10.2f $%10.2f\n\n\n\n", "TOTALS", tSubTotal, 
				tFee, tTaxes, -tDiscount, tTotal);
    }
	
    /**
   	 * @param pw
   	 * This method uses a PrintWriter object to print out a detail report of each
   	 * invoice
   	 */
	public static void printDetailedReport(PrintWriter pw, LinkedList<Invoice> invoiceList) {
		pw.println("Individual Invoice Detail Reports");
		pw.println("==================================================");
		
		for (Invoice inv: invoiceList)
			pw.print(inv.toString());
		
		pw.println("=============================================================="
				+ "========================================================");
	}
	
}
