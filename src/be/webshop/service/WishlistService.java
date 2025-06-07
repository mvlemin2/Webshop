package be.webshop.service;

import be.webshop.dao.WishlistDAO;
import be.webshop.model.Plant;

import java.util.ArrayList;
import java.util.List;

public class WishlistService {

    private final WishlistDAO wishlistDAO;
    private final UserService userService;

    public WishlistService() {
        this.wishlistDAO = new WishlistDAO();
        this.userService = new UserService();
    }

    public boolean addToWishlist(int productId, String username) {
        if (userService.isSignedIn(username)) {
            return wishlistDAO.addToWishlist(productId, username);
        }
        return false;
    }

    public boolean removeFromWishlist(int productId, String username) {
        if (userService.isSignedIn(username)) {
            return wishlistDAO.removeFromWishlist(productId, username);
        }
        return false;
    }

    public boolean removeAllFromWishlist(String username) {
        if (userService.isSignedIn(username)) {
            return wishlistDAO.removeAllFromWishlist(username);
        }
        return false;
    }

    public List<Plant> displayWishlist(String username){
        if(!userService.isSignedIn(username)){
            return new ArrayList<>(); //Indien de gebruiker niet ingelogd is, is de lijst leeg.
        }
        return wishlistDAO.displayWishlist(username);
    }
}
