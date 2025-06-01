package be.webshop.app;

import be.webshop.service.UserService;
import be.webshop.service.WishlistService;

public class Test {

    public static void main(String[] args) {

        UserService testUserService = new UserService();
        WishlistService testWishlistService = new WishlistService();

        //Test sign in
    //        System.out.println(testUserService.signIn("usernamehashed3","passwordhashed3")); //gebruiker niet ingelogd - juist wachtwoord -> true
    //        System.out.println(testUserService.signIn("usernamehashed4","passwordhashed4")); //gebruiker al ingelogd - juist wachtwoord -> true
    //        System.out.println(testUserService.signIn("usernamehashed","passwordhashed4")); //gebruiker niet ingelogd - fout wachtwoord -> false
    //        System.out.println(testUserService.signIn("usernamebestaatniet","passwordhashed4")); //username bestaat niet -> false

        //Test sign out
    //        System.out.println(testUserService.signOut("usernamehashed3")); //gebruiker ingelogd -> true
    //        System.out.println(testUserService.signOut("usernamehashed2")); //gebruiker niet ingelogd -> false

        //Test register
    //        System.out.println(testUserService.register("usernamehashed3","passwordhashed3")); //username bestaat al -> false
    //        System.out.println(testUserService.register("usernamehashed5","passwordhashed5")); //nieuwe username -> true

        //Test toevoegen aan winkelmandje
    //        System.out.println(testWishlistService.addToWishlist(4,"usernamehashed4")); //product nog niet in mandje - gebruiker ingelogd -> true
    //        System.out.println(testWishlistService.addToWishlist(4,"usernamehashed")); //product nog niet in mandje - gebruiker niet ingelogd -> false
    //        System.out.println(testWishlistService.addToWishlist(22,"usernamehashed4")); //product bestaat niet - gebruiker ingelogd => !!!error!!!
    //        System.out.println(testWishlistService.addToWishlist(7,"usernamehashed4")); //product al in mandje  - gebruiker ingelogd -> geeft false

        //Test verwijderen uit winkelmandje
    //        System.out.println(testWishlistService.removeFromWishlist(4,"usernamehashed4")); //product in mandje - gebruiker ingelogd -> true
    //        System.out.println(testWishlistService.removeFromWishlist(4,"usernamehashed4")); //product niet in mandje - gebruiker ingelogd -> false
    //        System.out.println(testWishlistService.removeFromWishlist(7,"usernamehashed4")); //product in mandje - gebruiker niet ingelogd -> false
    //        System.out.println(testWishlistService.removeFromWishlist(26,"usernamehashed4")); //product bestaat niet - gebruiker ingelogd -> false

        //Test alles verwijderen uit winkelmandje
    //        System.out.println(testWishlistService.removeAllFromWishlist("username123")); //producten in mandje - gebruiker ingelogd -> true
    //        System.out.println(testWishlistService.removeAllFromWishlist("usernamehashed4")); //producten in mandje - gebruiker niet niet ingelogd -> false

        //Test wishlist weergeven
    //        testWishlistService.displayWishlist("usernamehashed4"); //producten in mandje - gebruiker niet ingelogd -> "Je bent niet ingelogd" (geen boolean)
    //        testWishlistService.displayWishlist("usernamehashed4"); //producten in mandje - gebruiker ingelogd -> Afdruk van wishlist op scherm (geen boolean)
    //        testWishlistService.displayWishlist("usernamehashed2"); //geen producten in mandje - gebruiker ingelogd -> Lege afdruk van wishlist op scherm "|| Wishlist van usernamehashed2 ||" (geen boolean)



//test nog te doen met winkelmandje volledig leegmaken als er geen producten inzitten (en tekst aanpassen in Main)

    }
}
