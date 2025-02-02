class Equipment {
    private int equipmentID;
    private String equipmentName;
    private int quantity;
    private double rentalRate;
    private boolean availability;

    public Equipment(int equipmentID, String equipmentName, int quantity, double rentalRate, boolean availability) {
        this.equipmentID = equipmentID;
        this.equipmentName = equipmentName;
        this.quantity = quantity;
        this.rentalRate = rentalRate;
        this.availability = availability;
    }

    public int getEquipmentID(){return equipmentID;}
    public String getEquipmentName() {return equipmentName;}

    public int getQuantity() {return quantity;}

    public double getRentalRate() {return rentalRate;}

    public boolean isAvailable() {return availability;}
    
    public void setEquipmentID(int equipmentID){this.equipmentID = equipmentID;}
    public void setQuantity(int quantity){this.quantity = quantity;}
}
