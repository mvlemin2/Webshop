package be.webshop.dao;

import be.webshop.model.Plant;
import be.webshop.connection.DatabaseConnection;
import be.webshop.util.DatabaseConstants;
import be.webshop.util.DatabaseUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WishlistDAO {

    //4. Toevoegen aan verlanglijstje
    public boolean addToWishlist(int product_id, String username){
        PreparedStatement getUserID = null;
        PreparedStatement addToWishlist = null;
        ResultSet resultSet = null;

            //Controleren of product reeds werd toegevoegd tot verlanglijstje
            if(!checkInWishlist(product_id, username)){
                try {
                    Connection connection = DatabaseConnection.getInstance().getConnection();

                    getUserID = connection.prepareStatement(
                            "SELECT user_id FROM " + DatabaseConstants.USERS_TABLE + " WHERE username = ?");
                    getUserID.setString(1, username);

                    resultSet = getUserID.executeQuery();

                    int user_id_query = 0;
                    while(resultSet.next()){
                        user_id_query = resultSet.getInt("user_id");
                    }

                    //Controleren of product_id bestaat in database
                    boolean succes = checkProductExists(connection, product_id);
                    if(!succes){
                        return false;
                    }

                    addToWishlist = connection.prepareStatement(
                            "INSERT INTO " + DatabaseConstants.WISHLISTLINES_TABLE + "(product_id, user_id) VALUES(?, ?)");

                    addToWishlist.setInt(1, product_id);
                    addToWishlist.setInt(2, user_id_query);

                    addToWishlist.executeUpdate();
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    DatabaseUtils.closeQuietly(resultSet);
                    DatabaseUtils.closeQuietly(getUserID);
                    DatabaseUtils.closeQuietly(addToWishlist);
                }
            }
        return false;
    }

    //Controleren of het product bestaat
    private boolean checkProductExists(Connection connection, int product_id) throws SQLException {
        PreparedStatement checkProductExists = null;
        ResultSet productResult = null;

        try {
            checkProductExists = connection.prepareStatement(
                    "SELECT 1 FROM " + DatabaseConstants.PLANTS_TABLE + " WHERE product_id = ?");
            checkProductExists.setInt(1, product_id);
            productResult = checkProductExists.executeQuery();

            if (!productResult.next()) {
                return false;
            }
        } finally {
            DatabaseUtils.closeQuietly(productResult);
            DatabaseUtils.closeQuietly(checkProductExists);
        }
        return true;
    }

    //Controleren of het product zich reeds in het verlanglijstje bevindt
    private boolean checkInWishlist(int product_id, String username){
        PreparedStatement getUserID = null;
        PreparedStatement checkInWishList = null;
        ResultSet rs = null;
        ResultSet resultSet = null;
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();

            getUserID = connection.prepareStatement(
                    "SELECT user_id FROM " + DatabaseConstants.USERS_TABLE + " WHERE username = ?");
            getUserID.setString(1, username);
            rs = getUserID.executeQuery();

            int user_id_query = 0;
            while(rs.next()){
                user_id_query = rs.getInt("user_id");
            }

            checkInWishList = connection.prepareStatement(
                    "SELECT * FROM " + DatabaseConstants.WISHLISTLINES_TABLE + " WHERE PRODUCT_ID = ? AND USER_ID = ?");
            checkInWishList.setInt(1, product_id);
            checkInWishList.setInt(2, user_id_query);

            resultSet = checkInWishList.executeQuery();
            if(!resultSet.isBeforeFirst()){
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseUtils.closeQuietly(rs);
            DatabaseUtils.closeQuietly(getUserID);
            DatabaseUtils.closeQuietly(resultSet);
            DatabaseUtils.closeQuietly(checkInWishList);
        }
        return true;
    }

    //5. Verwijderen uit verlanglijstje
    public boolean removeFromWishlist(int product_id, String username){
        PreparedStatement getUserID = null;
        PreparedStatement removeFromWishlist = null;
        ResultSet resultSet = null;
            if(checkInWishlist(product_id, username)){
                try {
                    Connection connection = DatabaseConnection.getInstance().getConnection();

                    getUserID = connection.prepareStatement(
                            "SELECT user_id FROM " + DatabaseConstants.USERS_TABLE + " WHERE username = ?");
                    getUserID.setString(1, username);
                    resultSet = getUserID.executeQuery();

                    int user_id_query = 0;
                    while(resultSet.next()){
                        user_id_query = resultSet.getInt("user_id");
                    }

                    removeFromWishlist = connection.prepareStatement(
                            "DELETE FROM " + DatabaseConstants.WISHLISTLINES_TABLE + " WHERE product_id = ? AND user_id = ?");
                    removeFromWishlist.setInt(1, product_id);
                    removeFromWishlist.setInt(2, user_id_query);

                    removeFromWishlist.executeUpdate();
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    DatabaseUtils.closeQuietly(resultSet);
                    DatabaseUtils.closeQuietly(getUserID);
                    DatabaseUtils.closeQuietly(removeFromWishlist);
                }
            }
        return false;
    }

    //6. Alle favorieten verwijderen uit verlanglijstje
    public boolean removeAllFromWishlist(String username){
        PreparedStatement getUserID = null;
        PreparedStatement removeAllFromWishlist = null;
        ResultSet resultSet = null;
            try {
                Connection connection = DatabaseConnection.getInstance().getConnection();

                getUserID = connection.prepareStatement(
                        "SELECT user_id FROM " + DatabaseConstants.USERS_TABLE + " WHERE username = ?");
                getUserID.setString(1, username);
                resultSet = getUserID.executeQuery();

                int user_id_query = 0;
                while(resultSet.next()){
                    user_id_query = resultSet.getInt("user_id");
                }

                removeAllFromWishlist = connection.prepareStatement(
                        "DELETE FROM " + DatabaseConstants.WISHLISTLINES_TABLE + " WHERE user_id = ?");
                removeAllFromWishlist.setInt(1, user_id_query);

                removeAllFromWishlist.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DatabaseUtils.closeQuietly(resultSet);
                DatabaseUtils.closeQuietly(getUserID);
                DatabaseUtils.closeQuietly(removeAllFromWishlist);
            }
        return false;
    }

    //7. Bekijk verlanglijstje
    public List<Plant> displayWishlist(String username) {
        List<Plant> wishlist = new ArrayList<>();
        PreparedStatement getUserID = null;
        PreparedStatement getWishlist = null;
        ResultSet resultSet = null;
        ResultSet wishlistResult = null;

        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();

            getUserID = connection.prepareStatement(
                    "SELECT user_id FROM " + DatabaseConstants.USERS_TABLE + " WHERE username = ?"
            );
            getUserID.setString(1, username);
            resultSet = getUserID.executeQuery();

            int userId = -1;
            if (resultSet.next()) {
                userId = resultSet.getInt("user_id");
            }

            if (userId != -1) {
                getWishlist = connection.prepareStatement(
                        "SELECT p.* FROM " + DatabaseConstants.PLANTS_TABLE + " p " +
                                "JOIN " + DatabaseConstants.WISHLISTLINES_TABLE + " wl ON p.product_id = wl.product_id " +
                                "WHERE wl.user_id = ?"
                );
                getWishlist.setInt(1, userId);
                wishlistResult = getWishlist.executeQuery();

                while (wishlistResult.next()) {
                    Plant plant = new Plant(
                            wishlistResult.getInt("product_id"),
                            wishlistResult.getString("plantName"),
                            wishlistResult.getDouble("plantPrice"),
                            wishlistResult.getString("plantNameLatin"),
                            wishlistResult.getString("plantCategory"),
                            wishlistResult.getString("plantLocation"),
                            wishlistResult.getString("plantColor"),
                            wishlistResult.getString("plantDescription")
                    );
                    wishlist.add(plant);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseUtils.closeQuietly(resultSet);
            DatabaseUtils.closeQuietly(wishlistResult);
            DatabaseUtils.closeQuietly(getUserID);
            DatabaseUtils.closeQuietly(getWishlist);
        }
        return wishlist;
    }
}
