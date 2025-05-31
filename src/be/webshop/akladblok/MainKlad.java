/*
package be.webshop.akladblok;

import be.webshop.service.UserService;
import be.webshop.service.WishlistService;
import java.util.Scanner;

public class MainKlad {
    public static void main(String[] args) {
        UserService userService = new UserService();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welkom bij de Webshop CLI!");
        System.out.print("Voer je gebruikersnaam in: ");
        String username = scanner.nextLine();

        boolean signedIn = userService.isSignedIn(username);
        if (signedIn) {
            System.out.println("Gebruiker '" + username + "' is ingelogd.");
        } else {
            System.out.println("Gebruiker '" + username + "' is niet ingelogd.");
        }

        // Hier kun je uitbreiden met meer functionaliteiten
        // zoals wishlist bekijken, producten toevoegen, etc.


        private static final Scanner scanner = new Scanner(System.in);
        private static final UserService userService = new UserService();
        private static final WishlistService wishlistService = new WishlistService();


        public static void main(String[] args) {
                System.out.println("Welkom bij de Webshop CLI!");

                while (true) {
                    printMenu();
                    int keuze = leesKeuze();

                    switch (keuze) {
                        case 1 -> checkIngelogd();
                        case 2 -> loginGebruiker();
                        case 3 -> logoutGebruiker();
                        case 4 -> toonWishlist();
                        case 5 -> voegProductToeAanWishlist();
                        case 6 -> verwijderProductUitWishlist();
                        case 0 -> {
                            System.out.println("Tot ziens!");
                            return;
                        }
                        default -> System.out.println("Ongeldige keuze. Probeer opnieuw.");
                    }
                }
            }

            private static void printMenu() {
                System.out.println("\n--- Hoofdmenu ---");
                System.out.println("1. Controleer of gebruiker is ingelogd");
                System.out.println("2. Log gebruiker in");
                System.out.println("3. Log gebruiker uit");
                System.out.println("4. Bekijk wishlist");
                System.out.println("5. Voeg product toe aan wishlist");
                System.out.println("6. Verwijder product uit wishlist");
                System.out.println("0. Afsluiten");
                System.out.print("Maak een keuze: ");
            }

            private static int leesKeuze() {
                try {
                    return Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    return -1;
                }
            }

            private static void checkIngelogd() {
                System.out.print("Voer gebruikersnaam in: ");
                String username = scanner.nextLine();
                boolean isSignedIn = userService.isSignedIn(username);
                System.out.println("Gebruiker '" + username + "' is " + (isSignedIn ? "ingelogd." : "niet ingelogd."));
            }

            private static void loginGebruiker() {
                System.out.print("Voer gebruikersnaam in om in te loggen: ");
                String username = scanner.nextLine();
                boolean success = userService.login(username);
                System.out.println(success ? "Inloggen gelukt!" : "Inloggen mislukt.");
            }

            private static void logoutGebruiker() {
                System.out.print("Voer gebruikersnaam in om uit te loggen: ");
                String username = scanner.nextLine();
                boolean success = userService.logout(username);
                System.out.println(success ? "Uitloggen gelukt!" : "Uitloggen mislukt.");
            }


        private static void toonWishlist() {
            System.out.print("Voer gebruikersnaam in: ");
            String username = scanner.nextLine();
            var wishlist = wishlistService.getWishlist(username);
            if (wishlist.isEmpty()) {
                System.out.println("De wishlist van '" + username + "' is leeg.");
            } else {
                System.out.println("Wishlist van '" + username + "':");
                wishlist.forEach(product -> System.out.println("- " + product));
            }
        }

        private static void voegProductToeAanWishlist() {
            System.out.print("Gebruikersnaam: ");
            String username = scanner.nextLine();
            System.out.print("Productnaam: ");
            String product = scanner.nextLine();
            boolean success = wishlistService.addProduct(username, product);
            System.out.println(success ? "Product toegevoegd!" : "Toevoegen mislukt.");
        }

        private static void verwijderProductUitWishlist() {
            System.out.print("Gebruikersnaam: ");
            String username = scanner.nextLine();
            System.out.print("Productnaam: ");
            String product = scanner.nextLine();
            boolean success = wishlistService.removeProduct(username, product);
            System.out.println(success ? "Product verwijderd!" : "Verwijderen mislukt.");
        }





    }
}


*/
