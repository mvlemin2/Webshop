package be.webshop.service;

import be.webshop.dao.PlantDAO;
import be.webshop.model.Plant;

import java.util.List;

public class PlantService {
    private final PlantDAO plantDAO;

    public PlantService() {
        this.plantDAO = new PlantDAO();
    }

    public List<Plant> getAllPlants() {
        return plantDAO.getAllPlants();
    }

    public List<Plant> getPlantsByCategory(String category) {
        return plantDAO.getPlantsByCategory(category);
    }

    public Plant getPlantDetails(int productId) {
        return plantDAO.getPlantDetails(productId);
    }

    public List<Plant> searchPlants(String keyword) {
        PlantDAO plantDAO = new PlantDAO();
        return plantDAO.searchPlants(keyword);
    }
}
