package be.webshop.util;

public class DatabaseUtils {

    //Na elke connectie met de database de resources (ResultSet en PreparedStatement) afsluiten, zonder de DB-connectie te sluiten (gezien het gebruik van singleton)
    public static void closeQuietly(AutoCloseable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (Exception ignored) {
                System.err.println("Resources niet afgesloten!");
            }
        }
    }
}