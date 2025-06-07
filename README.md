# Projectbeschrijving en functionaliteiten
Deze Java-applicatie is een eenvoudige webshop voor planten. De applicatie wordt bediend vanuit de console. Gebruikers kunnen zich registreren, aanmelden en afmelden. Daarnaast kunnen ze planten bekijken (alles of per categorie), planten zoeken en de detailpagina van de planten bekijken. Tot slot kunnen de gebruikers planten toevoegen aan hun verlanglijstje, planten verwijderen (één plant of alles tegelijk) en het verlanglijstje bekijken. De applicatie maakt gebruik van een MySQL-database.

# Project management:
- Use cases: https://drive.google.com/file/d/1peH18BckgcB1917HT3Uqt6F639F-QZLK/view?usp=sharing
- Wireframes: https://drive.google.com/file/d/1bAvYWQRQKjBQKejADcrxVqiTdmiS2pr2/view?usp=sharing (tabbladen voor smartphone en tablet - voor de wireframes voor de smartphone moet je het bestand volledig openen in draw.io (want dit zit in een apart tabblad). De wireframes werden toegevoegd ter info als deel van de voorbereiding, maar aangezien ik het project niet in groep doe, zal ik ook geen userinterface maken)
- Planning (Trello): https://trello.com/invite/b/6818f869e989a320f9a5be5c/ATTIf913a847d148ca32ae0e1b9119560ebb0D285A97/programming-project

# Database:
- MySQL (schooldatabase)
- Schema's (ERD | tabellen | datamodel): https://drive.google.com/file/d/1wonkdA6u3q5tF2qCZKVvDk44oASOKd9d/view?usp=sharing

# Library:
- mysql-connector-j-9.3.0.jar

# Structuur:
## be.webshop.app
### Main
- Van hieruit kan je de applicatie runnen en gebruiken. Aangezien ik niet in groep werk, heb ik geen webuserinterface voorzien. Vanuit de Main-klasse kan de gebruiker via de console de app aansturen. 
- De Main-klasse roept de methodes op uit de service-package (UserService, WishlistService, PlantService).
- Het hoofdmenu toont alle functies van de applicatie:
>--- Hoofdmenu ---
>0. Verlaat de website
>1. Registreren
>2. Aanmelden
>3. Afmelden
>4. Toevoegen aan verlanglijstje
>5. Verwijderen uit verlanglijstje
>6. Alle favorieten verwijderen uit verlanglijstje
>7. Bekijk verlanglijstje
>8. Bekijk alle planten
>9. Bekijk planten per categorie
>10. Bekijk plantdetails
>11. Zoeken

## be.webshop.connection
- Deze package bevat de databaseverbinding van de applicatie.
### DatabaseConnection
- Ik heb gebruik gemaakt van het Singleton-patroon om ervoor te zorgen dat er slechts één actieve databaseverbinding bestaat.
- De klasse DatabaseConnection leest de databaseconfiguratie uit het db.properties-bestand, laadt de JDBC-driver en maakt verbinding met de MySQL-database.

## be.webshop.dao
- Deze package bevat de Data Access Objects van de applicatie. De klassen in deze package maken de verbinding met de database via SQL.
### PlantDAO
- Deze klasse bevat verschillende methodes met betrekking tot de planten in de database:
  - (8) **Bekijk alle planten**: *getAllPlants*
  - (9) **Bekijk planten per categorie**: *getPlantsByCategory*
  - (10) **Bekijk plantdetails**: *getPlantDetails*
  - (11) **Zoeken**: *searchPlants*
### UserDAO
- Deze klasse bevat verschillende methodes met betrekking tot de gebruikers van de applicatie:
  - (1) **Registreren**: *register*
  - (2) **Aanmelden**: *signIn*
  - (3) **Afmelden**: *signOut*
- De klasse bevat ook enkele hulpmethodes:
  - *hashPassword*: creëert een gehashte versie van het wachtwoord via SHA-256
  - *checkUser*: controleert of een gebruikersnaam bestaat in de database
  - *validateLogin*: controleert of de combinatie van de gebruikersnaam met het wachtwoord correct is
  - *isSignedIn*: controleert of de gebruiker is aangemeld
### WishlistDAO
- Deze klasse bevat verschillende methodes met betrekking tot het verlanglijstje van de gebruikers:
  - (4) **Toevoegen aan verlanglijstje**: *addToWishlist*
  - (5) **Verwijderen uit verlanglijstje**: *removeFromWishlist*
  - (6) **Alle favorieten verwijderen uit verlanglijstje**: *removeAllFromWishlist*
  - (7) **Bekijk verlanglijstje**: *displayWishlist*
- De klasse bevat ook enkele hulpmethodes:
  - *checkProductExists*: controleert of het product bestaat in de database
  - *checkInWishlist*: controleert of een plant al in het verlanglijstje staat

## be.webshop.model
- Deze package bevat momenteel slechts 1 object: Plant.
### Plant
- Deze klasse stelt een plant voor zoals die in de database geregistreerd is.

## be.webshop.resources
- Deze package bevat het configuratiebestand dat door de applicatie wordt gebruikt.
### db.properties
- Dit bestand bevat de databaseconfiguratie die nodig is om verbinding te maken met de database.
- De gegevens worden ingelezen door de DatabaseConnection-klasse.
- Het properties-bestand zorgt ervoor dat gebruikersnaam en het wachtwoord niet in de code terug te vinden is.

## be.webshop.service
- Deze package bevat de services die de connectie maken tussen de DAO en de console. Elke serviceklasse is verantwoordelijk voor een specifiek domein (planten, gebruikers, verlanglijstje).
### PlantService
- Deze klasse roept alle methodes aan uit de PlantDAO.
### UserService
- Deze klasse roept alle methodes aan uit de UserDAO, en wordt ook gebruikt door de WishlistService.
### WishlistService
- Deze klasse roept alle methodes aan uit de WishlistDAO.

## be.webshop.util
- Deze package bevat een hulpmethode en constante waarden.
### DatabaseConstants
- Deze klasse bevat de constante waarden voor de namen van de databasetabellen.
### DatabaseUtils
- Deze klasse bevat een methode om de PreparedStatement en ResultSet-objecten veilig af te sluiten zodat de applicatie niet crasht bij een fout. Door de Singleton kan de connectie zelf niet gesloten worden.

# Installatiehandleiding

1. Clone de repository.
2. Voeg mysql-connector-j-9.3.0.jar toe.
3. Gebruik je eigen gegevens in db.properties.
4. Start de applicatie via de Main-klasse.

