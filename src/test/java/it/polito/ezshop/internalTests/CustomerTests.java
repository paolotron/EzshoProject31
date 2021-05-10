package it.polito.ezshop.internalTests;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CustomerTests {
    EZShopInterface model;
    final String username = "Dummy";
    final String password = "Dummy";
    final String validName = "customer";
    final String invalidName = "";

    void login() throws InvalidPasswordException, InvalidUsernameException {
        model.login(username, password);
    }

    //defineCustomer()
    @Test

    void correctDefineCustomer() throws InvalidCustomerNameException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidCustomerIdException {
        login();
        Integer customerId = model.defineCustomer(validName);
        Assertions.assertTrue(customerId > 0, "Returned customer Id must be > 0");
        Assertions.assertNotEquals(null, model.getCustomer(customerId), "Container should be updated with the new customer");
        Assertions.assertEquals(validName, model.getCustomer(customerId).getCustomerName(), "Name should be equal with the one passed");
        Assertions.assertNull(model.getCustomer(customerId).getCustomerCard(), "For new customers there should not be a card attached");
        Assertions.assertEquals(0, model.getCustomer(customerId).getPoints(), "For new customers points should be 0");
    }

    @Test
    void invalidLogin() {
        Assertions.assertThrows(UnauthorizedException.class, ()-> model.defineCustomer(validName), "If not logged exception must be thrown");
    }

    @Test
    void invalidCustomerName() throws InvalidCustomerNameException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException {
        login();
        Assertions.assertThrows(InvalidCustomerNameException.class, ()->model.defineCustomer(invalidName), "Exception must be thrown");
        Integer res = model.defineCustomer(invalidName);
        Assertions.assertEquals(-1, res, "Returned value must be -1");
        Assertions.assertThrows(InvalidCustomerNameException.class, ()->model.defineCustomer(null), "Exception must be thrown");
        Integer res2 = model.defineCustomer(null);
        Assertions.assertEquals(-1, res2, "Returned value must be -1");
    }

    //modifyCustomer
    @Test
    void correctModifyName() throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException {
        login();
        String newName = "name";
        String card = "0123456789";
        Integer id = model.defineCustomer(validName);
        model.modifyCustomer(id, validName, card);
        boolean res = model.modifyCustomer(id, newName, null);
        Assertions.assertTrue(res, "Result should be true");
        Assertions.assertEquals(newName, model.getCustomer(id).getCustomerName(), "Name has not been updated correctly");
        Assertions.assertEquals(card, model.getCustomer(id).getCustomerName(), "Card code must be unchanged");
    }

    @Test
    void correctModifyCard() throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException {
        login();
        String card = "0123456789";
        Integer id = model.defineCustomer(validName);
        boolean res = model.modifyCustomer(id, validName, card);
        Assertions.assertTrue(res, "Result should be true");
        Assertions.assertEquals(card, model.getCustomer(id).getCustomerCard(), "CustomerCard has not been updated correctly");
    }

    @Test
    void invalidCardCode() throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException {
        login();
        String validCard = "0123456789";
        String card1 = "abcdefghil";
        String card2 = "012345";
        String card3 = "111111111111111111111";
        Integer id = model.defineCustomer(validName);
        boolean res = model.modifyCustomer(id, validName, card1);
        Assertions.assertFalse(res, "CardCode must be 10 digits not any 10 characters");
        res = model.modifyCustomer(id, validName, card2);
        Assertions.assertFalse(res, "CardCode must be 10 digits long Not less");
        res = model.modifyCustomer(id, validName, card3);
        Assertions.assertFalse(res, "CardCode must be 10 digits long Not more");
        Assertions.assertThrows(InvalidCustomerCardException.class, ()->model.modifyCustomer(id, validName, card1));
        Assertions.assertThrows(InvalidCustomerCardException.class, ()->model.modifyCustomer(id, validName, card2));
        Assertions.assertThrows(InvalidCustomerCardException.class, ()->model.modifyCustomer(id, validName, card3));

        Integer id2 = model.defineCustomer(validName);
        model.modifyCustomer(id2, validName, validCard);
        res = model.modifyCustomer(id, validName, validCard);
        Assertions.assertFalse(res, "Returned value must be false since there can't be 2 customers with the same Card");

    }
}
