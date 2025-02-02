import java.io.*;
import java.util.*;

public class Customer
{
    private String customerID;
    private String name;
    private int contactInfo;
    
    public Customer(String customerID, String name, int contactInfo)
    {
        this.customerID = customerID;
        this.name = name;
        this.contactInfo = contactInfo;
    }
    
    public void setCustomerID(String customerID){this.customerID = customerID;}
    public void setCustomerName(String name){this.name = name;}
    public void setCustomerContact(int contactInfo){this.contactInfo = contactInfo;}
    
    public String getCustomerID(){return customerID;}
    public String getCustomerName(){return name;}
    public int getCustomerContact(){return contactInfo;}

    public void registerCustomer(String filePath)
    {
        String customerData = customerID + ";" + name + ";" + contactInfo; 
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true)))
        {
            bw.write(customerData);
            bw.newLine();
            
            System.out.println("Register successful.");
        }
        catch (IOException e)
        {
            System.out.println("Error registering up your account. Please try again :) ");
            return;
        }
    }
    
}