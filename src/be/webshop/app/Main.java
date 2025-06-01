package be.webshop.app;

import be.webshop.service.UserService;
import be.webshop.service.WishlistService;
import java.util.Scanner;

public class Main {
    private static final Scanner scannerText = new Scanner(System.in);
    private static final Scanner scannerInt = new Scanner(System.in);
    private static final UserService userService = new UserService();
    private static final WishlistService wishlistService = new WishlistService();

    private static void printMenu() {
        System.out.println("\n--- Hoofdmenu ---");
        System.out.println("0. Verlaat de website");
        System.out.println("1. Registreren");
        System.out.println("2. Aanmelden");
        System.out.println("3. Afmelden");
        System.out.println("4. Toevoegen aan verlanglijstje");
        System.out.println("5. Verwijderen uit verlanglijstje");
        System.out.println("6. Alle favorieten verwijderen uit verlanglijstje");
        System.out.println("7. Bekijk verlanglijstje");
        System.out.println("8. Bekijk planten per categorie");
        System.out.println("9. Bekijk plantdetails");
        System.out.println("10. Zoeken");
        System.out.print("Maak een keuze: ");
    }

    private static int readMenuOption() {
        try {
            return scannerInt.nextInt();
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    //1. Registreren
    private static void register() {
        System.out.print("Fijn dat je je wil registreren!");
        System.out.print("\nVoer een gebruikersnaam in: ");
        String username = scannerText.nextLine();
        System.out.print("Voer een wachtwoord in: ");
        String password = scannerText.nextLine();
        System.out.println("Geef het gekozen wachtwoord nogmaals in: ");
        String password2 = scannerText.nextLine();
        //iets verzinnen om uit loop te geraken bij typfout
        while(!password.equals(password2)){
            System.out.println("Het wachtwoord is niet correct, geef het gekozen wachtwoord nogmaals in: ");
            password2 = scannerText.nextLine();
        }
        boolean success = userService.register(username, password);
        System.out.println(success ? "Bedankt, " + username + "! Je hebt nu een account aangemaakt op onze website. Meld je nu voor de eerste keer aan met je logingegevens." : "Registreren is niet gelukt. De gebruikersnaam die je opgaf is reeds in gebruik. Probeer een andere gebruikersnaam.");
    }

    //2. Aanmelden
    private static void signIn() {
        System.out.print("Voer je gebruikersnaam in: ");
        String username = scannerText.nextLine();
        System.out.print("Voer je wachtwoord in: ");
        String password = scannerText.nextLine();
        boolean success = userService.signIn(username, password);
        System.out.println(success ? "Welkom, " + username + "! Je bent nu aangemeld." : "Aanmelden is niet gelukt. Je gebruikersnaam of wachtwoord is niet correct.");
    }

    //3. Afmelden
    private static void signOut() {
        System.out.print("Ben je zeker dat je wil uitloggen?");
        System.out.print("Voer je gebruikersnaam in: ");
        String username = scannerText.nextLine();
        boolean success = userService.signOut(username);
        System.out.println(success ? "Tot ziens, " + username + "! Je bent nu afgemeld." : "Afmelden is niet gelukt. Je was mogelijk reeds afgemeld.");
    }

    //4. Toevoegen aan verlanglijstje
    private static void addToWishlist() {
        System.out.print("Voer je gebruikersnaam in: ");
        String username = scannerText.nextLine();
        System.out.println("Geef aan welke plant je wil toevoegen aan je verlanglijstje: ");
        //Afdruk met overzicht planten invoegen
        int plant = scannerInt.nextInt();
        boolean success = wishlistService.addToWishlist(plant, username);
        System.out.println(success ? "De plant werd toegevoegd aan je verlanglijstje!" : "Toevoegen mislukt. Je bent ofwel niet aangemeld, de plant die je opgaf bestaat niet, of de plant stond al in je verlanglijstje.");
        System.out.println("\n");
        wishlistService.displayWishlist(username);
    }

    //5. Verwijderen uit verlanglijstje
    private static void removeFromWishlist() {
        System.out.print("Voer je gebruikersnaam in: ");
        String username = scannerText.nextLine();
        System.out.println("\n");
        //id-nummers nog toevoegen aan wishlist!!!
        wishlistService.displayWishlist(username);
        System.out.println("Geef aan welke plant je wil verwijderen uit je verlanglijstje: ");
        int plant = scannerInt.nextInt();
        boolean success = wishlistService.removeFromWishlist(plant, username);
        System.out.println(success ? "De plant werd verwijderd uit je verlanglijstje!" : "Verwijderen mislukt. Je bent ofwel niet aangemeld, de plant die je opgaf bestaat niet, of de plant stond niet in je verlanglijstje.");
        System.out.println("\n");
        wishlistService.displayWishlist(username);
    }

    //6. Alle favorieten verwijderen uit verlanglijstje
    private static void removeAllFromWishlist() {
        System.out.print("Voer je gebruikersnaam in: ");
        String username = scannerText.nextLine();
        System.out.println("\n");
        wishlistService.displayWishlist(username);
        System.out.print("Ben je zeker dat je je verlanglijstje wil leegmaken? (Y/N): ");
        String answer = scannerText.nextLine();
        switch (answer){
            case "Y": break;
            case "N": return;
            default:
                System.out.println("Ongeldige keuze. Terug naar het hoofdmenu.");
                return;
        }
        boolean success = wishlistService.removeAllFromWishlist(username);
        System.out.println(success ? "Je verlanglijstje werd leeggemaakt." : "Verlanglijstje leegmaken mislukt. Je bent niet aangemeld.");
        System.out.println("\n");
        wishlistService.displayWishlist(username);
    }

    //7. Bekijk verlanglijstje
    private static void displayWishlist() {
        System.out.print("Voer je gebruikersnaam in: ");
        String username = scannerText.nextLine();
        System.out.println("\n");
        wishlistService.displayWishlist(username);
        boolean success = wishlistService.displayWishlist(username);
        if(!success){
            System.out.println("Je bent niet aangemeld.");
        }
    }

    //8. Bekijk planten per categorie
    //9. Bekijk plantdetails
    //10. Zoeken

    public static void main(String[] args) {
        System.out.println("Welkom op onze webshop!");
        while (true) {
            printMenu();
            int menuOption = readMenuOption();

            switch (menuOption) {
                case 1 -> register();
                case 2 -> signIn();
                case 3 -> signOut();
                case 4 -> addToWishlist();
                case 5 -> removeFromWishlist();
                case 6 -> removeAllFromWishlist();
                case 7 -> displayWishlist();
//                case 8 -> verwijderProductUitWishlist();
//                case 9 -> verwijderProductUitWishlist();
//                case 10 -> verwijderProductUitWishlist();
                case 0 -> {
                    System.out.println("Tot ziens!");
                    return;
                }
                default -> System.out.println("Ongeldige keuze. Probeer opnieuw.");
            }
        }

    }
}

