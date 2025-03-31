package be.webshop.wishlist;

import be.webshop.products.Plant;
import be.webshop.users.User;

import java.util.Set;
import java.util.HashSet;

public class Wishlist {
    private Set<Plant> wishlist;

    public Wishlist() {
        this.wishlist = new HashSet<Plant>();
    }

    public void addToWishlist(User user, Plant plant){
        if(user.isSignedIn()){
            this.wishlist.add(plant);
        }
    }

    public void removeFromWishlist(User user, Plant plant){
        if(user.isSignedIn()){
            this.wishlist.remove(plant);
        }
    }

    public void clearWishlist(User user){
        if(user.isSignedIn()){
            this.wishlist.clear();
        }
    }

    public Set<Plant> getWishlist(User user){
        if(user.isSignedIn()){
            return this.wishlist;
        }
    }

    @Override
    public String toString() {
        return "Wishlist{" +
                "wishlist=" + wishlist +
                '}';
    }
}


