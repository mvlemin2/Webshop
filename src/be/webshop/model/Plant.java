package be.webshop.model;

public class Plant {
    private int productId;
    private String plantName;
    private double plantPrice;
    private String plantNameLatin;
    private String plantCategory;
    private String plantLocation;
    private String plantColor;
    private String plantDescription;

    public Plant(int productId, String plantName, double plantPrice, String plantNameLatin,
                 String plantCategory, String plantLocation, String plantColor, String plantDescription) {
        this.productId = productId;
        this.plantName = plantName;
        this.plantPrice = plantPrice;
        this.plantNameLatin = plantNameLatin;
        this.plantCategory = plantCategory;
        this.plantLocation = plantLocation;
        this.plantColor = plantColor;
        this.plantDescription = plantDescription;
    }

    public String getPlantName() {
        return plantName;
    }

    public String getPlantNameLatin() {
        return plantNameLatin;
    }

    public String getPlantCategory() {
        return plantCategory;
    }

    public String getPlantLocation() {
        return plantLocation;
    }

    public String getPlantColor() {
        return plantColor;
    }

    public double getPlantPrice() {
        return plantPrice;
    }

    public String getPlantDescription() {
        return plantDescription;
    }

    @Override
    public String toString() {
        return productId + ". " + plantName + " (" + plantCategory + ") - €" + plantPrice;
    }
}

