package be.webshop.dao;

import be.webshop.connection.DatabaseConnection;
import be.webshop.model.Plant;
import be.webshop.util.DatabaseConstants;
import be.webshop.util.DatabaseUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlantDAO {

    //Alle planten op het scherm
    public List<Plant> getAllPlants() {
        List<Plant> plants = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();

            statement = connection.prepareStatement(
                    "SELECT * FROM " + DatabaseConstants.PLANTS_TABLE
            );
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Plant plant = new Plant(
                        resultSet.getInt("product_id"),
                        resultSet.getString("plantName"),
                        resultSet.getDouble("plantPrice"),
                        resultSet.getString("plantNameLatin"),
                        resultSet.getString("plantCategory"),
                        resultSet.getString("plantLocation"),
                        resultSet.getString("plantColor"),
                        resultSet.getString("plantDescription")
                );
                plants.add(plant);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseUtils.closeQuietly(resultSet);
            DatabaseUtils.closeQuietly(statement);
        }
        return plants;
    }

    //De planten per categorie op het scherm
    public List<Plant> getPlantsByCategory(String category) {
        List<Plant> plants = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();

            statement = connection.prepareStatement(
                    "SELECT * FROM " + DatabaseConstants.PLANTS_TABLE + " WHERE plantCategory = ?"
            );
            statement.setString(1, category);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Plant plant = new Plant(
                        resultSet.getInt("product_id"),
                        resultSet.getString("plantName"),
                        resultSet.getDouble("plantPrice"),
                        resultSet.getString("plantNameLatin"),
                        resultSet.getString("plantCategory"),
                        resultSet.getString("plantLocation"),
                        resultSet.getString("plantColor"),
                        resultSet.getString("plantDescription")
                );
                plants.add(plant);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseUtils.closeQuietly(resultSet);
            DatabaseUtils.closeQuietly(statement);
        }
        return plants;
    }

    public Plant getPlantDetails(int productId) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();

            statement = connection.prepareStatement(
                    "SELECT * FROM " + DatabaseConstants.PLANTS_TABLE + " WHERE product_id = ?"
            );
            statement.setInt(1, productId);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new Plant(
                        resultSet.getInt("product_id"),
                        resultSet.getString("plantName"),
                        resultSet.getDouble("plantPrice"),
                        resultSet.getString("plantNameLatin"),
                        resultSet.getString("plantCategory"),
                        resultSet.getString("plantLocation"),
                        resultSet.getString("plantColor"),
                        resultSet.getString("plantDescription")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseUtils.closeQuietly(resultSet);
            DatabaseUtils.closeQuietly(statement);
        }
        return null;
    }

    public List<Plant> searchPlants(String keyword) {
        List<Plant> plants = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();

            String sql = "SELECT * FROM " + DatabaseConstants.PLANTS_TABLE +
                    " WHERE LOWER(plantName) LIKE ? " +
                    "OR LOWER(plantNameLatin) LIKE ? " +
                    "OR LOWER(plantCategory) LIKE ? " +
                    "OR LOWER(plantLocation) LIKE ? " +
                    "OR LOWER(plantColor) LIKE ? " +
                    "OR LOWER(plantDescription) LIKE ?";

            statement = connection.prepareStatement(sql);
            String searchTerm = "%" + keyword.toLowerCase() + "%";
            for (int i = 1; i <= 6; i++) {
                statement.setString(i, searchTerm);
            }

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Plant plant = new Plant(
                        resultSet.getInt("product_id"),
                        resultSet.getString("plantName"),
                        resultSet.getDouble("plantPrice"),
                        resultSet.getString("plantNameLatin"),
                        resultSet.getString("plantCategory"),
                        resultSet.getString("plantLocation"),
                        resultSet.getString("plantColor"),
                        resultSet.getString("plantDescription")
                );
                plants.add(plant);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseUtils.closeQuietly(resultSet);
            DatabaseUtils.closeQuietly(statement);
        }

        return plants;
    }



}
