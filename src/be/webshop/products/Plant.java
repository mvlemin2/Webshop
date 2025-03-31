package be.webshop.products;

import java.util.Objects;

public class Plant {
    private final int PRODUCTID;
    private String plantName;
    private double plantPrice;
    private String plantNameLatin;
    private PlantCategory plantCategory;
    private PlantLocation plantLocation;
    private PlantColor plantColor;
    private String plantDescription;

    public Plant(int PRODUCTID, String plantName, double plantPrice, String plantNameLatin, PlantCategory plantCategory, PlantLocation plantLocation, PlantColor plantColor, String plantDescription) {
        this.PRODUCTID = PRODUCTID;
        this.plantName = plantName;
        this.plantPrice = plantPrice;
        this.plantNameLatin = plantNameLatin;
        this.plantCategory = plantCategory;
        this.plantLocation = plantLocation;
        this.plantColor = plantColor;
        this.plantDescription = plantDescription;
    }

    public int getPRODUCTID() {
        return PRODUCTID;
    }

    public String getPlantName() {
        return plantName;
    }

    public double getPlantPrice() {
        return plantPrice;
    }

    public String getPlantNameLatin() {
        return plantNameLatin;
    }

    public PlantCategory getPlantCategory() {
        return plantCategory;
    }

    public PlantLocation getPlantLocation() {
        return plantLocation;
    }

    public PlantColor getPlantColor() {
        return plantColor;
    }

    public String getPlantDescription() {
        return plantDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Plant plant = (Plant) o;
        return PRODUCTID == plant.PRODUCTID && Double.compare(plantPrice, plant.plantPrice) == 0 && Objects.equals(plantName, plant.plantName) && Objects.equals(plantNameLatin, plant.plantNameLatin) && plantCategory == plant.plantCategory && plantLocation == plant.plantLocation && plantColor == plant.plantColor && Objects.equals(plantDescription, plant.plantDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(PRODUCTID, plantName, plantPrice, plantNameLatin, plantCategory, plantLocation, plantColor, plantDescription);
    }

    @Override
    public String toString() {
        return "Plant{" +
                "PRODUCTID=" + PRODUCTID +
                ", plantName='" + plantName + '\'' +
                ", plantPrice=" + plantPrice +
                ", plantNameLatin='" + plantNameLatin + '\'' +
                ", plantCategory=" + plantCategory +
                ", plantLocation=" + plantLocation +
                ", plantColor=" + plantColor +
                ", plantDescription='" + plantDescription + '\'' +
                '}';
    }
}
