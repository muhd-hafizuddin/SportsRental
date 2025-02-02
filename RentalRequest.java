import java.time.*;

class RentalRequest {
    private int requestID;
    private String customerID;
    private Queue equipmentList = new Queue();
    private LocalDate requestDate;

    public RentalRequest(int requestID, String customerID, Queue equipmentList, LocalDate requestDate) {
        this.requestID = requestID;
        this.customerID = customerID;
        this.equipmentList = equipmentList;
        this.requestDate = requestDate;
    }

    public int getRequestID() {
        return requestID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public Queue getEquipmentList() {
        return equipmentList;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Request ID: ").append(requestID).append("\n");
        sb.append("Customer ID: ").append(customerID).append("\n");
        sb.append("Request Date: ").append(requestDate).append("\n");
        sb.append("Equipment List:\n");
        Queue temp = new Queue();
        while (!equipmentList.isEmpty()) {
            Equipment eq = (Equipment) equipmentList.dequeue();
            sb.append("  - ").append(eq.getEquipmentName()).append(" (Qty: ").append(eq.getQuantity()).append(")\n");
            temp.enqueue(eq);
        }
        while (!temp.isEmpty()) {
            equipmentList.enqueue(temp.dequeue());
        }
        return sb.toString();
    }
}

