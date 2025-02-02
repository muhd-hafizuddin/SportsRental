import java.util.*;
import java.io.*;

public class Staff
{
    private String staffID;
    private String name; 
    private String role;
    protected ArrayList<Customer> cus = new ArrayList<>();
    
    public Staff(String staffID, String name, String role)
    {
        this.staffID = staffID;
        this.name = name;
        this.role = role;
    }
    
    public void setStaffID(String staffID){this.staffID = staffID;}
    public void setStaffName(String name){this.name = name;}
    public void setStaffRole(String role){this.role = role;}
    
    public String getStaffID(){return staffID;}
    public String getStaffName(){return name;}
    public String getStaffRole(){return role;}
    
    public void loadDataCustomer(String filePath)
    {
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath)))
        {
            while((line = br.readLine()) != null)
            {
                StringTokenizer st = new StringTokenizer(line, ";");
                String customerID = st.nextToken();
                String name = st.nextToken();
                int contactInfo = Integer.parseInt(st.nextToken());
                
                Customer cs = new Customer(customerID, name, contactInfo);
                cus.add(cs);
            }
        }
        catch (IOException io)
        {
            System.out.println("Error loading data from text file. Make sure it's in the same folder. ");
        }
    }
}
