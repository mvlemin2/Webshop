import be.webshop.products.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Tester {
    private Plant p1;

    @BeforeEach
    void setUp(){
        p1 = new Plant(2332, "Magnolia", 56.50,"Magnolia Magnifica Alba", PlantCategory.BOMEN, PlantLocation.HALFSCHADUW, PlantColor.VIOLET, "De Magnolia is een mooie bloemende boom die eind maart reeds in bloei staat.");
    }

    @Test
    void createPlant(){
        assertEquals("Magnolia", p1.getPlantName());
    }

}
