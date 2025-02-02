import java.util.*;
import java.io.*;
import java.time.*;

public class mainQueue {
    private static Scanner scanner = new Scanner(System.in);
    private static Queue rentalQueue = new Queue();
    private static String customerFilePath = "customers.txt";
    private static String staffFilePath = "staff.txt";
    private static String rentalRequestFilePath = "rental_requests.txt";
    private static String rentalStatusFilePath = "rental_status.txt";
    private static String equipmentFilePath = "equipment.txt";
    private static int lastRequestID = 0;
    private static final String requestIDFilePath = "request_id.txt";
    private static String currentUserID = null;

    public static void main(String[] args) {
        loadRentalRequests(); // Load rental requests from file
        loadLastRequestID();
        boolean run = true;

        while (run) {
            System.out.println("**************************************");
            System.out.println("*           SPORT RENTAL SYSTEM      *");
            System.out.println("**************************************");
            System.out.println("* 1. Login                           *");
            System.out.println("* 2. Sign Up                         *");
            System.out.println("* 3. Exit                            *");
            System.out.println("**************************************");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    signUp();
                    break;
                case 3:
                    run = false;
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } 
        scanner.close();
    }

    // Login functionality
    private static void login() {
        System.out.print("Enter User ID: ");
        String userID = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        if (checkLogin(customerFilePath, userID, password)) {
            currentUserID = userID;
            customerMenu();
        } else if (checkLogin(staffFilePath, userID, password)) {
            currentUserID = userID;
            staffMenu();
        } else {
            System.out.println("Invalid User ID or Password.");
        }
    }

    // Signup functionality
    private static void signUp() {
        System.out.print("Enter Customer ID: ");
        String customerID = scanner.nextLine();
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        System.out.print("Enter Contact Info: ");
        String contactInfo = scanner.nextLine();

        if (checkIfUserExists(customerFilePath, customerID)) {
            System.out.println("Customer ID already exists. Please try again.");
            return;
        }

        String customerData = String.format("%s;%s;%s;%s", customerID, name, password, contactInfo);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(customerFilePath, true))) {
            writer.write(customerData);
            writer.newLine();
            System.out.println("Signup successful. Please login.");
        } catch (IOException e) {
            System.out.println("Error signing up. Please try again.");
        }
    }

    // Customer menu
    private static void customerMenu() {
        while (true) {
            System.out.println("**************************************");
            System.out.println("*           CUSTOMER MENU            *");
            System.out.println("**************************************");
            System.out.println("* 1. Rent Equipment                  *");
            System.out.println("* 2. Return Equipment                *");
            System.out.println("* 3. Check Rental Status             *");
            System.out.println("* 4. Logout                          *");
            System.out.println("**************************************");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    rentEquipment();
                    break;
                case 2:
                    returnEquipment();
                    break;
                case 3:
                    checkRentalStatus();
                    break;
                case 4:
                    currentUserID = null;
                    System.out.println("Logged out successfully.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Staff menu
    private static void staffMenu() {
        while (true) {
            System.out.println("**************************************");
            System.out.println("*           STAFF MENU               *");
            System.out.println("**************************************");
            System.out.println("* 1. Manage Equipment                *");
            System.out.println("* 2. Manage Rental Requests          *");
            System.out.println("* 3. View Customer History           *");
            System.out.println("* 4. Logout                          *");
            System.out.println("**************************************");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    manageEquipment();
                    break;
                case 2:
                    manageRentalRequests();
                    break;
                case 3:
                    viewCustomerHistory();
                    break;
                case 4:
                    currentUserID = null;
                    System.out.println("Logged out successfully.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Display all equipment
    private static void displayAllEquipment() {
        try (BufferedReader reader = new BufferedReader(new FileReader(equipmentFilePath))) {
            String line;
            System.out.println("Available Equipment:");
            System.out.println("ID\tName\t\tQuantity\tRental Rate\tAvailability");
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                int id = Integer.parseInt(data[0]);
                String name = data[1];
                int quantity = Integer.parseInt(data[2]);
                double rentalRate = Double.parseDouble(data[3]);
                boolean availability = Boolean.parseBoolean(data[4]);
                System.out.printf("%d\t%s\t%d\t\t%.2f\t\t%s%n", id, name, quantity, rentalRate, availability ? "Yes" : "No");
            }
        } catch (IOException e) {
            System.out.println("Error reading equipment file.");
        }
    }

    // Load last request ID
    private static void loadLastRequestID() {
        try (BufferedReader reader = new BufferedReader(new FileReader(requestIDFilePath))) {
            String line = reader.readLine();
            if (line != null) {
                lastRequestID = Integer.parseInt(line);
            }
        } catch (IOException e) {
            System.out.println("Request ID file not found. Starting from ID 1.");
            lastRequestID = 0;
        }
    }

    // Save last request ID
    private static void saveLastRequestID() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(requestIDFilePath))) {
            writer.write(String.valueOf(lastRequestID));
        } catch (IOException e) {
            System.out.println("Error saving request ID.");
        }
    }

    // Rent equipment
   private static void rentEquipment() {
    displayAllEquipment();
    Queue equipmentQueue = new Queue();
    char response = 'a';

    do {
        System.out.print("Enter Equipment ID: ");
        int equipmentID = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Equipment equipment = findEquipmentById(equipmentID);
        if (equipment == null) {
            System.out.println("Equipment not found. Please try again.");
            continue;
        }

        System.out.print("Enter Quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (quantity > equipment.getQuantity()) {
            System.out.println("Insufficient quantity available. Available: " + equipment.getQuantity());
            continue;
        }

        Equipment rentedEquipment = new Equipment(equipment.getEquipmentID(), equipment.getEquipmentName(), 
                                                quantity, equipment.getRentalRate(), equipment.isAvailable());
        equipmentQueue.enqueue(rentedEquipment);

        // Update the equipment quantity in the file
        updateEquipmentQuantity(equipmentID, equipment.getQuantity() - quantity);

        System.out.print("Add another item? (Y/N): ");
        response = scanner.nextLine().toUpperCase().charAt(0);
    } while (response == 'Y');

    lastRequestID++; // Increment request ID
    RentalRequest request = new RentalRequest(lastRequestID, currentUserID, equipmentQueue, LocalDate.now());
    rentalQueue.enqueue(request);
    saveLastRequestID(); // Save the updated request ID

    // Add initial rental status
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(rentalStatusFilePath, true))) {
        String data = String.format("%d;%s;Pending", lastRequestID, currentUserID);
        writer.write(data);
        writer.newLine();
        writer.flush();
    } catch (IOException e) {
        System.out.println("Error saving rental status: " + e.getMessage());
    }

    // Save to rental requests file
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(rentalRequestFilePath, true))) {
        StringBuilder equipmentData = new StringBuilder();
        Queue tempQueue = new Queue();
        
        // Process each equipment item
        while (!equipmentQueue.isEmpty()) {
            Equipment eq = (Equipment) equipmentQueue.dequeue();
            tempQueue.enqueue(eq);
            
            equipmentData.append(eq.getEquipmentID())
                        .append(":")
                        .append(eq.getEquipmentName())
                        .append(":")
                        .append(eq.getQuantity())
                        .append(",");
        }
        
        // Restore the original equipment queue
        while (!tempQueue.isEmpty()) {
            equipmentQueue.enqueue(tempQueue.dequeue());
        }
        
        // Remove trailing comma
        if (equipmentData.length() > 0) {
            equipmentData.setLength(equipmentData.length() - 1);
        }
        
        String requestData = String.format("%d;%s;%s;%s", 
            lastRequestID, 
            currentUserID, 
            LocalDate.now(), 
            equipmentData.toString());
        writer.write(requestData);
        writer.newLine();
        writer.flush();
    } catch (IOException e) {
        System.out.println("Error saving rental request: " + e.getMessage());
    }

    System.out.println("Rental request submitted successfully.");
}

    // Update equipment quantity
    private static void updateEquipmentQuantity(int equipmentID, int newQuantity) {
        try {
            File inputFile = new File(equipmentFilePath);
            File tempFile = new File("temp_equipment.txt");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                int id = Integer.parseInt(data[0]);
                if (id == equipmentID) {
                    line = String.format("%d;%s;%d;%s;%s", id, data[1], newQuantity, data[3], data[4]);
                }
                writer.write(line);
                writer.newLine();
            }

            writer.close();
            reader.close();

            // Replace the original file with the updated file
            if (inputFile.delete()) {
                tempFile.renameTo(inputFile);
            } else {
                System.out.println("Error updating equipment quantity.");
            }
        } catch (IOException e) {
            System.out.println("Error updating equipment file.");
        }
    }

    // Find equipment by ID
    private static Equipment findEquipmentById(int equipmentID) {
        try (BufferedReader reader = new BufferedReader(new FileReader(equipmentFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                int id = Integer.parseInt(data[0]);
                if (id == equipmentID) {
                    String equipmentName = data[1];
                    int quantity = Integer.parseInt(data[2]);
                    double rentalRate = Double.parseDouble(data[3]);
                    boolean availability = Boolean.parseBoolean(data[4]);
                    return new Equipment(id, equipmentName, quantity, rentalRate, availability);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading equipment file.");
        }
        return null; // Equipment not found
    }

    private static void returnEquipment() {
    try (BufferedReader reader = new BufferedReader(new FileReader(rentalRequestFilePath))) {
        System.out.println("\n=== Your Active Rentals ===");
        System.out.println("Rental ID\tDate\t\tEquipment Details");
        System.out.println("------------------------------------------------");
        
        String line;
        boolean hasActiveRentals = false;
        
        // Read and display all active rentals for the current user
        while ((line = reader.readLine()) != null) {
            String[] data = line.split(";");
            int requestID = Integer.parseInt(data[0]);
            String customerID = data[1];
            
            // Check if this rental belongs to current user and is not already returned
            if (customerID.equals(currentUserID) && !isRentalReturned(requestID)) {
                hasActiveRentals = true;
                LocalDate requestDate = LocalDate.parse(data[2]);
                
                // Print basic rental info
                System.out.printf("%d\t\t%s\t", requestID, requestDate);
                
                // Process and print equipment details
                if (data.length > 3) {
                    String[] equipmentList = data[3].split(",");
                    System.out.println("\nRented Equipment:");
                    for (String equipment : equipmentList) {
                        String[] equipmentDetails = equipment.split(":");
                        System.out.printf("  - %s (Qty: %s)\n", 
                            equipmentDetails[1], // Equipment name
                            equipmentDetails[2]  // Quantity
                        );
                    }
                }
                System.out.println("------------------------------------------------");
            }
        }
        
        if (!hasActiveRentals) {
            System.out.println("You have no active rentals.");
            return;
        }
        
        // Process return
        System.out.print("\nEnter Rental ID to return: ");
        int rentalID = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        // Verify rental exists and belongs to user
        if (!verifyRentalOwnership(rentalID, currentUserID)) {
            System.out.println("Invalid rental ID or rental does not belong to you.");
            return;
        }
        
        // Process the return
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rentalStatusFilePath, true))) {
            String data = String.format("%d;%s;Returned", rentalID, currentUserID);
            writer.write(data);
            writer.newLine();
            
            // Update equipment quantities
            returnEquipmentToInventory(rentalID);
            
            System.out.println("Equipment returned successfully.");
        }
        
    } catch (IOException e) {
        System.out.println("Error processing return: " + e.getMessage());
    }
}

// Helper method to check if a rental is already returned
private static boolean isRentalReturned(int rentalID) {
    try (BufferedReader reader = new BufferedReader(new FileReader(rentalStatusFilePath))) {
        String line;
        String lastStatus = null;
        
        // Find the latest status for this rental
        while ((line = reader.readLine()) != null) {
            String[] data = line.split(";");
            if (Integer.parseInt(data[0]) == rentalID) {
                lastStatus = data[2];
            }
        }
        
        return "Returned".equals(lastStatus);
    } catch (IOException e) {
        System.out.println("Error checking rental status: " + e.getMessage());
        return false;
    }
}

// Helper method to verify rental ownership
private static boolean verifyRentalOwnership(int rentalID, String customerID) {
    try (BufferedReader reader = new BufferedReader(new FileReader(rentalRequestFilePath))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] data = line.split(";");
            if (Integer.parseInt(data[0]) == rentalID && data[1].equals(customerID)) {
                return true;
            }
        }
    } catch (IOException e) {
        System.out.println("Error verifying rental ownership: " + e.getMessage());
    }
    return false;
}

// Helper method to return equipment to inventory
private static void returnEquipmentToInventory(int rentalID) {
    try (BufferedReader reader = new BufferedReader(new FileReader(rentalRequestFilePath))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] data = line.split(";");
            if (Integer.parseInt(data[0]) == rentalID) {
                // Process equipment returns
                if (data.length > 3) {
                    String[] equipmentList = data[3].split(",");
                    for (String equipment : equipmentList) {
                        String[] equipmentDetails = equipment.split(":");
                        int equipmentID = Integer.parseInt(equipmentDetails[0]);
                        int returnQuantity = Integer.parseInt(equipmentDetails[2]);
                        
                        // Get current quantity and update
                        Equipment currentEquipment = findEquipmentById(equipmentID);
                        if (currentEquipment != null) {
                            updateEquipmentQuantity(equipmentID, 
                                currentEquipment.getQuantity() + returnQuantity);
                        }
                    }
                }
                break;
            }
        }
    } catch (IOException e) {
        System.out.println("Error updating inventory: " + e.getMessage());
    }
}

    // Check rental status
    private static void checkRentalStatus() {
        try (BufferedReader reader = new BufferedReader(new FileReader(rentalStatusFilePath))) {
            String line;
            boolean found = false;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length >= 3 && data[1].trim().equals(currentUserID.trim())) {
                    System.out.println("Rental ID: " + data[0]);
                    System.out.println("Status: " + data[2]);
                    found = true;
                }
            }
            if (!found) {
                System.out.println("No rental requests found for this customer.");
            }
        } catch (IOException e) {
            System.out.println("Error checking rental status.");
        }
    }

    private static void manageEquipment() {
    while (true) {
        displayAllEquipment();
        System.out.println("\nEquipment Management");
        System.out.println("1. Add Equipment");          // New option
        System.out.println("2. Update Equipment Quantity");
        System.out.println("3. Remove Equipment");
        System.out.println("4. Back to Staff Menu");
        System.out.print("Enter your choice: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        if (choice == 4) {
            break;
        }

        // Handle Add Equipment option
        if (choice == 1) {
            addEquipment();
            continue;
        }
        
        System.out.print("Enter Equipment ID to modify: ");
        int equipmentID = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        try {
            // Read all equipment into a queue
            Queue equipmentQueue = new Queue();
            boolean found = false;
            
            BufferedReader reader = new BufferedReader(new FileReader(equipmentFilePath));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                int id = Integer.parseInt(data[0]);
                String name = data[1];
                int quantity = Integer.parseInt(data[2]);
                double rentalRate = Double.parseDouble(data[3]);
                boolean availability = Boolean.parseBoolean(data[4]);
                
                Equipment equipment = new Equipment(id, name, quantity, rentalRate, availability);
                equipmentQueue.enqueue(equipment);
            }
            reader.close();
            
            // Process the modification
            Queue tempQueue = new Queue();
            while (!equipmentQueue.isEmpty()) {
                Equipment eq = (Equipment) equipmentQueue.dequeue();
                if (eq.getEquipmentID() == equipmentID) {
                    found = true;
                    if (choice == 2) {  // Updated choice number
                        System.out.print("Enter new quantity: ");
                        int newQuantity = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        
                        if (newQuantity >= 0) {
                            eq = new Equipment(eq.getEquipmentID(), eq.getEquipmentName(), 
                                            newQuantity, eq.getRentalRate(), newQuantity > 0);
                            tempQueue.enqueue(eq);
                            System.out.println("Quantity updated successfully.");
                        } else {
                            System.out.println("Invalid quantity. Must be 0 or positive.");
                            tempQueue.enqueue(eq);
                        }
                    } else if (choice == 3) {  // Updated choice number
                        System.out.print("Are you sure you want to remove this equipment? (Y/N): ");
                        String confirm = scanner.nextLine().toUpperCase();
                        if (!confirm.equals("Y")) {
                            tempQueue.enqueue(eq);
                            System.out.println("Removal cancelled.");
                        } else {
                            System.out.println("Equipment removed successfully.");
                        }
                    }
                } else {
                    tempQueue.enqueue(eq);
                }
            }
            
            if (!found) {
                System.out.println("Equipment ID not found.");
                continue;
            }
            
            // Write back to file
            BufferedWriter writer = new BufferedWriter(new FileWriter(equipmentFilePath));
            while (!tempQueue.isEmpty()) {
                Equipment eq = (Equipment) tempQueue.dequeue();
                String equipmentData = String.format("%d;%s;%d;%.2f;%b", 
                    eq.getEquipmentID(), 
                    eq.getEquipmentName(), 
                    eq.getQuantity(), 
                    eq.getRentalRate(), 
                    eq.isAvailable());
                writer.write(equipmentData);
                writer.newLine();
            }
            writer.close();
            
        } catch (IOException e) {
            System.out.println("Error managing equipment: " + e.getMessage());
        }
    }
}

// New method to add equipment
private static void addEquipment() {
    try {
        // Get the highest existing equipment ID
        int nextId = 1;
        BufferedReader reader = new BufferedReader(new FileReader(equipmentFilePath));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] data = line.split(";");
            int id = Integer.parseInt(data[0]);
            if (id >= nextId) {
                nextId = id + 1;
            }
        }
        reader.close();

        // Get equipment details from user
        System.out.println("\nAdd New Equipment");
        System.out.print("Enter Equipment Name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter Initial Quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        System.out.print("Enter Rental Rate (per day): ");
        double rentalRate = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        // Validate input
        if (quantity < 0 || rentalRate < 0) {
            System.out.println("Invalid input. Quantity and rental rate must be positive.");
            return;
        }

        // Add new equipment to file
        BufferedWriter writer = new BufferedWriter(new FileWriter(equipmentFilePath, true));
        String equipmentData = String.format("%d;%s;%d;%.2f;%b", 
            nextId, 
            name, 
            quantity, 
            rentalRate, 
            quantity > 0);
        writer.write(equipmentData);
        writer.newLine();
        writer.close();

        System.out.println("Equipment added successfully with ID: " + nextId);
    } catch (IOException e) {
        System.out.println("Error adding equipment: " + e.getMessage());
    } catch (InputMismatchException e) {
        System.out.println("Invalid input format. Please enter numeric values for quantity and rental rate.");
        scanner.nextLine(); // Clear the invalid input
    }
}

    // Manage rental requests (staff only)
    private static void manageRentalRequests() {
        if (rentalQueue.isEmpty()) {
            System.out.println("No rental requests to process.");
            return;
        }

        // Dequeue the next rental request
        RentalRequest request = (RentalRequest) rentalQueue.dequeue();
        System.out.println("Processing Request ID: " + request.getRequestID());
        System.out.println("Customer ID: " + request.getCustomerID());
        System.out.println("Request Date: " + request.getRequestDate());
        System.out.println("Equipment List:");

        // Use temporary removal method to display equipment list
        Queue tempEquipmentList = new Queue();
        while (!request.getEquipmentList().isEmpty()) {
            Equipment eq = (Equipment) request.getEquipmentList().dequeue();
            System.out.println("  - " + eq.getEquipmentName() + " (Qty: " + eq.getQuantity() + ")");
            tempEquipmentList.enqueue(eq);
        }

        // Restore the equipment list
        while (!tempEquipmentList.isEmpty()) {
            request.getEquipmentList().enqueue(tempEquipmentList.dequeue());
        }

        // Staff decides to approve or reject
        System.out.print("Approve (A) or Reject (R): ");
        String decision = scanner.nextLine().toUpperCase();

        // Save the status to the rental status file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rentalStatusFilePath, true))) {
            String status = decision.equals("A") ? "Approved" : "Rejected";
            String data = String.format("%d;%s;%s", request.getRequestID(), request.getCustomerID(), status);
            writer.write(data);
            writer.newLine();
            System.out.println("Request " + status.toLowerCase() + ".");
        } catch (IOException e) {
            System.out.println("Error processing rental request.");
        }
    }

    // View customer history (staff only)
    private static void viewCustomerHistory() {
        System.out.print("Enter Customer ID: ");
        String customerID = scanner.nextLine();

        try (BufferedReader reader = new BufferedReader(new FileReader(rentalStatusFilePath))) {
            String line;
            boolean found = false;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                if (data[1].equals(customerID)) {
                    System.out.println("Rental ID: " + data[0]);
                    System.out.println("Status: " + data[2]);
                    found = true;
                }
            }
            if (!found) {
                System.out.println("No rental history found for this customer.");
            }
        } catch (IOException e) {
            System.out.println("Error viewing customer history.");
        }
    }

    // Check login credentials
    private static boolean checkLogin(String filePath, String userID, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                if (data[0].equals(userID) && data[2].equals(password)) {
                    return true; // Correct userID and password
                }
            }
        } catch (IOException e) {
            System.out.println("Error checking login credentials.");
        }
        return false; // Invalid login
    }

    // Check if user exists
    private static boolean checkIfUserExists(String filePath, String userID) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                if (data[0].equals(userID)) {
                    return true; // User already exists
                }
            }
        } catch (IOException e) {
            System.out.println("Error checking if user exists.");
        }
        return false; // User does not exist
    }

    // Load rental requests
    private static void loadRentalRequests() {
        Queue tempQueue = new Queue();

        try (BufferedReader reader = new BufferedReader(new FileReader(rentalRequestFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                int requestID = Integer.parseInt(data[0]);
                String customerID = data[1];
                LocalDate requestDate = LocalDate.parse(data[2]);

                Queue equipmentList = new Queue();
                if (data.length > 3) {
                    String[] equipmentData = data[3].split(",");
                    for (String equipmentInfo : equipmentData) {
                        String[] equipmentDetails = equipmentInfo.split(":");
                        int equipmentID = Integer.parseInt(equipmentDetails[0]);
                        String equipmentName = equipmentDetails[1];
                        int quantity = Integer.parseInt(equipmentDetails[2]);
                        double price = 0.0; // Set a default value or parse from your data if available
                        boolean isAvailable = true; // Default value for availability

                        equipmentList.enqueue(new Equipment(equipmentID, equipmentName, quantity, price, isAvailable));
                    }
                }

                RentalRequest request = new RentalRequest(requestID, customerID, equipmentList, requestDate);
                tempQueue.enqueue(request);
            }
        } catch (IOException e) {
            System.out.println("Error loading rental requests.");
        }

        // Restore rentalQueue from tempQueue
        while (!tempQueue.isEmpty()) {
            rentalQueue.enqueue((RentalRequest) tempQueue.dequeue());
        }
    }
}
