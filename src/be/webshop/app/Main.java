package be.webshop.app;

import be.webshop.model.Plant;
import be.webshop.service.PlantService;
import be.webshop.service.UserService;
import be.webshop.service.WishlistService;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scannerText = new Scanner(System.in);
    private static final Scanner scannerInt = new Scanner(System.in);
    private static final UserService userService = new UserService();
    private static final WishlistService wishlistService = new WishlistService();
    private static final PlantService plantService = new PlantService();

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
        System.out.println("8. Bekijk alle planten");
        System.out.println("9. Bekijk planten per categorie");
        System.out.println("10. Bekijk plantdetails");
        System.out.println("11. Zoeken");
        System.out.print("Maak een keuze: ");
    }

    private static int readMenuOption() {
        try {
            return scannerInt.nextInt();
        } catch (InputMismatchException e) {
            scannerInt.nextLine();
            return -1;
        }
    }

    //1. Registreren
    private static void register() {
        System.out.println("Fijn dat je je wil registreren!");

        // Gebruikersnaam invoeren
        System.out.print("Voer een gebruikersnaam in: ");
        String username = scannerText.nextLine().trim();

        if (username.isBlank()) {
            System.out.println("De gebruikersnaam mag niet leeg zijn. De registratie werd geannuleerd.");
            return;
        }

        // Controleer of gebruikersnaam al bestaat
        if (userService.checkUser(username)) {
            System.out.println("Deze gebruikersnaam is al in gebruik. Probeer een andere.");
            return;
        }

        // Wachtwoord invoeren
        System.out.print("Voer een wachtwoord in: ");
        String password = scannerText.nextLine();

        if (password.isBlank()) {
            System.out.println("Het wachtwoord mag niet leeg zijn. De registratie werd geannuleerd.");
            return;
        }

        // Herhaal wachtwoord met maximum 3 pogingen
        int attempts = 0;
        while (true) {
            System.out.print("Geef het gekozen wachtwoord nogmaals in (of typ 'stop' om te annuleren): ");
            String password2 = scannerText.nextLine();

            if (password2.equalsIgnoreCase("stop")) {
                System.out.println("De registratie werd geannuleerd.");
                return;
            }

            if (password.equals(password2)) {
                break;
            }

            attempts++;
            if (attempts >= 3) {
                System.out.println("Te veel mislukte pogingen. De registratie werd geannuleerd.");
                return;
            }

            System.out.println("De wachtwoorden komen niet overeen. Probeer opnieuw.");
        }

        // Registratie uitvoeren
        boolean success = userService.register(username, password);
        if (success) {
            System.out.println("Bedankt, " + username + "! Je hebt nu een account aangemaakt op onze website.");
            System.out.println("Meld je aan met je logingegevens.");
        } else {
            System.out.println("Registreren is helaas niet gelukt.");
        }
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
        System.out.print("Voer je gebruikersnaam in: ");
        String username = scannerText.nextLine();
        System.out.print("Ben je zeker dat je je wil afmelden? (Y/N): ");
        String answer = scannerText.nextLine();
        switch (answer){
            case "Y": break;
            case "N": return;
            default:
                System.out.println("Ongeldige keuze. Terug naar het hoofdmenu.");
                return;
        }
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
        //System.out.println("\n");
        //id-nummers nog toevoegen aan wishlist!!!
        wishlistService.displayWishlist(username);
        System.out.println("\nGeef aan welke plant je wil verwijderen uit je verlanglijstje: ");
        int plant = scannerInt.nextInt();
        boolean success = wishlistService.removeFromWishlist(plant, username);
        System.out.println(success ? "De plant werd verwijderd uit je verlanglijstje!" : "Verwijderen mislukt. Je bent ofwel niet aangemeld, de plant die je opgaf bestaat niet, of de plant stond niet in je verlanglijstje.");
        //System.out.println("\n");
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
//    private static void displayWishlist() {
//        System.out.print("Voer je gebruikersnaam in: ");
//        String username = scannerText.nextLine();
//        System.out.println("\n");
//        //wishlistService.displayWishlist(username);
//        boolean success = wishlistService.displayWishlist(username);
//        if(!success){
//            System.out.println("Je bent niet aangemeld.");
//        }
//    }

    private static void displayWishlist() {
        System.out.print("Voer je gebruikersnaam in: ");
        String username = scannerText.nextLine();

        List<Plant> wishlist = wishlistService.displayWishlist(username);

        if (wishlist.isEmpty()) {
            System.out.println("De wishlist van " + username + " is leeg, of " + username + "is niet aangemeld.");
        } else {
            System.out.println("\n--- Wishlist van " + username + " ---");
            for (Plant plant : wishlist) {
                System.out.println("Naam: " + plant.getPlantName());
                System.out.println("Latijnse naam: " + plant.getPlantNameLatin());
                System.out.println("Categorie: " + plant.getPlantCategory());
                System.out.println("Locatie: " + plant.getPlantLocation());
                System.out.println("Kleur: " + plant.getPlantColor());
                System.out.println("Prijs: €" + plant.getPlantPrice());
                System.out.println("Beschrijving: " + plant.getPlantDescription());
                System.out.println("-----------------------------");
            }
        }
    }

    //8. Bekijk alle planten
    private static void displayAllPlants() {
        System.out.println("\n--- Alle Planten ---");
        for (Plant plant : plantService.getAllPlants()) {
            System.out.println(plant);
        }
    }

    // 9. Bekijk planten per categorie
    private static void displayPlantsByCategory() {
        while (true) {
            System.out.println("\n--- Kies een categorie ---");
            System.out.println("0. Terug naar hoofdmenu");
            System.out.println("1. 1-jarig");
            System.out.println("2. Klim");
            System.out.println("3. Vast");
            System.out.println("4. Water");
            System.out.println("5. Boom");
            System.out.println("6. Heester");
            System.out.println("7. Kruid");
            System.out.println("8. 2-jarig");
            System.out.print("Maak een keuze (0-8): ");

            int keuze;
            try {
                keuze = scannerInt.nextInt();
            } catch (InputMismatchException e) {
                scannerInt.nextLine(); // buffer leegmaken
                System.out.println("Verkeerde invoer. Probeer opnieuw.");
                continue;
            }

            if (keuze == 0) {
                return; // terug naar hoofdmenu
            }

            String categorie;
            switch (keuze) {
                case 1 -> categorie = "1-jarig";
                case 2 -> categorie = "klim";
                case 3 -> categorie = "vast";
                case 4 -> categorie = "water";
                case 5 -> categorie = "boom";
                case 6 -> categorie = "heester";
                case 7 -> categorie = "kruid";
                case 8 -> categorie = "2-jarig";
                default -> {
                    System.out.println("Verkeerde invoer. Probeer opnieuw.");
                    continue;
                }
            }

            List<Plant> planten = plantService.getPlantsByCategory(categorie);

            if (planten.isEmpty()) {
                System.out.println("Geen planten in deze categorie gevonden.");
            } else {
                System.out.println("\n--- Planten in categorie '" + categorie + "' ---");
                for (Plant plant : planten) {
                    System.out.println(plant);
                }
            }
            return; // na weergave terug naar hoofdmenu
        }
    }

    //10. Bekijk plantdetails
    private static void displayPlantDetails() {

        while (true) {
            displayAllPlants();
            System.out.println("\n--- Plantdetails bekijken ---");
            System.out.println("Geef het nummer van de plant in (of 0 om terug te keren naar het hoofdmenu): ");
            int productId;

            try {
                productId = scannerInt.nextInt();
            } catch (InputMismatchException e) {
                scannerInt.nextLine(); // buffer leegmaken
                System.out.println("Verkeerde invoer. Probeer opnieuw.");
                continue;
            }

            if (productId == 0) {
                return; // terug naar hoofdmenu
            }

            Plant plant = plantService.getPlantDetails(productId);

            if (plant == null) {
                System.out.println("Geen plant gevonden met product-ID: " + productId);
            } else {
                System.out.println("\n--- Plantdetails ---");
                System.out.println("Naam: " + plant.getPlantName());
                System.out.println("Latijnse naam: " + plant.getPlantNameLatin());
                System.out.println("Categorie: " + plant.getPlantCategory());
                System.out.println("Locatie: " + plant.getPlantLocation());
                System.out.println("Kleur: " + plant.getPlantColor());
                System.out.println("Prijs: €" + plant.getPlantPrice());
                System.out.println("Beschrijving: " + plant.getPlantDescription());
                return; // na succesvolle weergave terug naar hoofdmenu
            }
        }
    }

    //11. Zoeken
    private static void searchPlants() {
        System.out.print("Voer een zoekterm in (naam of deel van de naam van een plant): ");
        String keyword = scannerText.nextLine().toLowerCase();

        List<Plant> results = plantService.searchPlants(keyword);

        if (results.isEmpty()) {
            System.out.println("Geen planten gevonden die overeenkomen met: " + keyword);
        } else {
            System.out.println("\n--- Zoekresultaten ---");
            for (Plant plant : results) {
                System.out.println("Naam: " + plant.getPlantName());
                System.out.println("Latijnse naam: " + plant.getPlantNameLatin());
                System.out.println("Categorie: " + plant.getPlantCategory());
                System.out.println("Locatie: " + plant.getPlantLocation());
                System.out.println("Kleur: " + plant.getPlantColor());
                System.out.println("Prijs: €" + plant.getPlantPrice());
                System.out.println("Beschrijving: " + plant.getPlantDescription());
                System.out.println("-----------------------------");
            }

        }
    }



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
                case 8 -> displayAllPlants();
                case 9 -> displayPlantsByCategory();
                case 10 -> displayPlantDetails();
                case 11 -> searchPlants();
                case 0 -> {
                    System.out.println("Tot ziens!");
                    return;
                }
                default -> System.out.println("Ongeldige keuze. Probeer opnieuw.");
            }
        }

    }
}

