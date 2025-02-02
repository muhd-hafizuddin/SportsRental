import java.util.*;
import java.io.*;
import java.time.*;

public class RentalTransaction
{
    private int transactionID;
    private RentalRequest rentalRequest;
    private LocalDate returnDate;
    private double charges;
    
    public RentalTransaction(int transactionID, RentalRequest rentalRequest, LocalDate returnDate, double charges)
    {
        this.transactionID = transactionID;
        this.rentalRequest = rentalRequest;
        this.returnDate = returnDate;
        this.charges = charges;
    }
    
    public int getTransactionID(){return transactionID;}
    public double getCharges(){return charges;}
    public LocalDate getReturnDate(){return returnDate;}
    
    public void setTransactionID(int transactionID){this.transactionID = transactionID;}
    public void setCharges(double charges){this.charges = charges;}
    public void setReturnDate(LocalDate returnDate){this.returnDate = returnDate;}
    
    
}
