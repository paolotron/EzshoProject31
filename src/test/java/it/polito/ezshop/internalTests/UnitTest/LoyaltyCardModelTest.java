package it.polito.ezshop.internalTests.UnitTest;

import it.polito.ezshop.model.LoyaltyCardModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LoyaltyCardModelTest {
    @Test
    void addPointsTest(){
        LoyaltyCardModel l = new LoyaltyCardModel(1);
        l.addPoints(1);
        Assertions.assertEquals(1, l.getPoints());
        l.addPoints(Integer.MAX_VALUE);
        Assertions.assertEquals(Integer.signum(1), Integer.signum(l.getPoints()), "The sum of 2 values that is greater than maxint must not be negative");
        Assertions.assertEquals(Integer.MAX_VALUE, l.getPoints(), "Max points that can be stored in the card are maxint");

        l = new LoyaltyCardModel(1);
        l.addPoints(50);
        l.addPoints(-20);
        Assertions.assertEquals(30, l.getPoints(), "It should be possible to add negative numbers");
        l.addPoints(-100);
        Assertions.assertEquals(30, l.getPoints(), "Points cannot be a negative number");

        l.addPoints(0);
        Assertions.assertEquals(30, l.getPoints());
    }
}
