package it.polito.ezshop.internalTests.UnitTest;

import it.polito.ezshop.model.LoyaltyCardModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LoyaltyCardModelTest {
    @Test
    void addPointsTest(){
        LoyaltyCardModel l = new LoyaltyCardModel(1);
        l.updatePoints(1);
        Assertions.assertEquals(1, l.getPoints());
        l.updatePoints(Integer.MAX_VALUE);
        Assertions.assertEquals(1, l.getPoints(), "If the result is greater than MAXPOINTS points should remain unchaneged");

        l = new LoyaltyCardModel(1);
        l.updatePoints(50);
        l.updatePoints(-20);
        Assertions.assertEquals(30, l.getPoints(), "It should be possible to add negative numbers");
        l.updatePoints(-100);
        Assertions.assertEquals(30, l.getPoints(), "Points cannot be a negative number");

        l.updatePoints(0);
        Assertions.assertEquals(30, l.getPoints());
    }
}
