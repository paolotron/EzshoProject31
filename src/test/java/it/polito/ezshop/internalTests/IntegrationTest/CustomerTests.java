package it.polito.ezshop.internalTests.IntegrationTest;

import it.polito.ezshop.data.Customer;
import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CustomerTests {
    EZShopInterface model;
    final String username = "Dummy";
    final String password = "Dummy";
    final String validName = "customer";
    final String invalidName = "";
    String validCard;

    void login() throws InvalidPasswordException, InvalidUsernameException {
        model.login(username, password);
    }

    @BeforeEach
    void startEzShop() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, UnauthorizedException {
        model = new it.polito.ezshop.data.EZShop();
        model.reset();
        model.createUser(username, password, "Administrator");
        login();
        validCard = model.createCard();
        model.logout();
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
    void invalidCustomerNameDuringDefine() throws InvalidCustomerNameException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException {
        login();
        Assertions.assertThrows(InvalidCustomerNameException.class, ()->model.defineCustomer(invalidName), "Exception must be thrown");
        Assertions.assertThrows(InvalidCustomerNameException.class, ()->model.defineCustomer(null), "Exception must be thrown");
    }

    //modifyCustomer
    @Test
    void correctModifyName() throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException {
        login();
        String newName = "name";
        Integer id = model.defineCustomer(validName);
        model.modifyCustomer(id, validName, validCard);
        boolean res = model.modifyCustomer(id, newName, null);
        Assertions.assertTrue(res, "Result should be true");
        Assertions.assertEquals(newName, model.getCustomer(id).getCustomerName(), "Name has not been updated correctly");
        Assertions.assertEquals(validCard, model.getCustomer(id).getCustomerCard(), "Card code must be unchanged");
    }

    @Test
    void correctModifyCard() throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException {
        login();
        Integer id = model.defineCustomer(validName);
        boolean res = model.modifyCustomer(id, validName, validCard);
        Assertions.assertTrue(res, "Result should be true");
        Assertions.assertEquals(validCard, model.getCustomer(id).getCustomerCard(), "CustomerCard has not been updated correctly");
    }

    @Test
    void invalidCardCode() throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException {
        login();
        String card1 = "abcdefghil";
        String card2 = "012345";
        String card3 = "111111111111111111111";
        Integer id = model.defineCustomer(validName);
        Boolean res;

        Assertions.assertThrows(InvalidCustomerCardException.class, ()->model.modifyCustomer(id, validName, card1));
        Assertions.assertThrows(InvalidCustomerCardException.class,()->model.modifyCustomer(id, validName, card2));
        Assertions.assertThrows(InvalidCustomerCardException.class,()->model.modifyCustomer(id, validName, card3));



        Integer id2 = model.defineCustomer(validName);
        model.modifyCustomer(id2, validName, validCard);
        res = model.modifyCustomer(id, validName, validCard);
        Assertions.assertFalse(res, "Returned value must be false since there can't be 2 customers with the same Card");

    }

    @Test
    void invalidCustomerNameDuringModify () throws InvalidCustomerNameException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException {
        login();
        Integer id = model.defineCustomer(validName);
        Assertions.assertThrows(InvalidCustomerNameException.class, ()-> model.modifyCustomer(id, null, null), "Exception must be thrown");
        Assertions.assertThrows(InvalidCustomerNameException.class, ()->model.modifyCustomer(id, "", null), "Exception must be thrown");
    }

    //deleteCustomer()

    @Test

    void correctDeleteCustomer () throws InvalidCustomerIdException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidCustomerNameException {
        login();
        Integer id = model.defineCustomer(validName);
        boolean res = model.deleteCustomer(id);
        Assertions.assertTrue(res, "Returned value must be true");
        Assertions.assertNull(model.getCustomer(id), "The customer has not been correctly deleted");
    }
    @Test
    void invalidCustomerIdToDelete () throws InvalidCustomerIdException, UnauthorizedException, InvalidCustomerNameException, InvalidPasswordException, InvalidUsernameException {
        login();
        model.defineCustomer(validName);
        model.defineCustomer(validName);
        model.defineCustomer(validName);
        model.defineCustomer(validName);
        Integer nCustomer = model.getAllCustomers().size();
        boolean res = model.deleteCustomer(1000);
        Assertions.assertFalse(res, "Returned value must be false");
        Assertions.assertEquals(nCustomer, model.getAllCustomers().size(), "The number of customers should not be different");
    }

    // getCustomer()
    @Test
    void correctGetCustomer () throws InvalidPasswordException, InvalidUsernameException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException {
        login();
        Integer id = model.defineCustomer(validName);
        Customer c = model.getCustomer(id);
        Assertions.assertEquals(id, c.getId());
        Assertions.assertEquals(validName, c.getCustomerName());
        Assertions.assertNotEquals(null, c);
    }
    @Test
    void customerDoesNotExist () throws InvalidCustomerIdException, UnauthorizedException, InvalidCustomerNameException, InvalidPasswordException, InvalidUsernameException {
        login();
        Integer id = model.defineCustomer(validName);
        Customer c = model.getCustomer(1000);
        Assertions.assertNull(c);
        Assertions.assertThrows(InvalidCustomerIdException.class, ()->model.getCustomer(null));
        Assertions.assertThrows(InvalidCustomerIdException.class, ()->model.getCustomer(-12));
        Assertions.assertThrows(InvalidCustomerIdException.class, ()->model.getCustomer(0));
    }

    // getAllCustomers
    @Test
    void correctGetAllCustomers () throws UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidCustomerNameException {
        login();
        Assertions.assertEquals(0, model.getAllCustomers().size());
        model.defineCustomer(validName);
        model.defineCustomer(validName);
        model.defineCustomer(validName);
        model.defineCustomer(validName);
        Assertions.assertEquals(4, model.getAllCustomers().size());
    }

    // createCard()
    @Test
    void correctCreateCard () throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException {
        login();
        String code = model.createCard();
        Assertions.assertNotEquals(null, code);
        // TODO: discuss this
        Assertions.assertNotEquals("", code, "It should be returned only if db unreacheable, how it should be meant with Json?");
    }

    // attachCardToCustomer()
    @Test
    void correctAttachCardToCustomer () throws InvalidPasswordException, InvalidUsernameException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException, InvalidCustomerCardException {
        login();
        Integer id = model.defineCustomer(validName);
        boolean res = model.attachCardToCustomer(validCard, id);
        Assertions.assertTrue(res);
        Assertions.assertEquals(validCard, model.getCustomer(id).getCustomerCard());
    }
    @Test
    void invalidAttachCardToCustomer () throws InvalidPasswordException, InvalidUsernameException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException, InvalidCustomerCardException {
        login();
        Integer id = model.defineCustomer(validName);
        model.attachCardToCustomer(validCard, id);
        id = model.defineCustomer(validName);
        boolean res;

        res = model.attachCardToCustomer(validCard, 1000);
        Assertions.assertFalse(res, "Returned value must be false when there is no customer with a given id");
        Assertions.assertThrows((InvalidCustomerIdException.class),()->model.attachCardToCustomer(validCard, null));
        Assertions.assertThrows((InvalidCustomerIdException.class),()->model.attachCardToCustomer(validCard, 0));
        Assertions.assertThrows((InvalidCustomerIdException.class),()->model.attachCardToCustomer(validCard, -12));

        // TODO: If the Db is unreacheable returned value is false, it should mean that createCard didn't work
    }

    // modifyPointsOnCard()
    @Test
    void correctModifyPointsOnCard () throws InvalidPasswordException, InvalidUsernameException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException, InvalidCustomerCardException {
        login();
        Integer id = model.defineCustomer(validName);
        model.attachCardToCustomer(validCard, id);
        boolean res = model.modifyPointsOnCard(validCard, 10);
        Assertions.assertTrue(res, "Returned value must be true");
        Assertions.assertEquals(10, model.getCustomer(id).getPoints(), "Points should be added correctly");
        res = model.modifyPointsOnCard(validCard, -10);
        Assertions.assertTrue(res, "Returned value must be true even if points go to 0");
        Assertions.assertEquals(0, model.getCustomer(id).getPoints(), "Points should be removed correctly");
    }

    @Test
    void invalidModifyPointsOnCard () throws InvalidPasswordException, InvalidUsernameException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerCardException, InvalidCustomerIdException {
        login();
        Integer id = model.defineCustomer(validName);
        boolean res = model.modifyPointsOnCard(validCard, -10);
        Assertions.assertFalse(res);
        Assertions.assertEquals(0, model.getCustomer(id).getPoints(), "Points should remain unchanged");
        model.modifyPointsOnCard(validCard, 50);
        res = model.modifyPointsOnCard(validCard, -100);
        Assertions.assertFalse(res);
        Assertions.assertEquals(50, model.getCustomer(id).getPoints(), "Points should remain unchanged");

        res = model.modifyPointsOnCard("wrongcard", 1);
        Assertions.assertFalse(res);
        //TODO: return false if we cannot reach the db
    }

    //GENERAL
    @Test
    void invalidCustomerIdExceptThrower() throws InvalidPasswordException, InvalidUsernameException {
        login();
        Assertions.assertThrows(InvalidCustomerIdException.class, ()-> model.deleteCustomer(null), "null cannot be accepted ad CustomerId");
        Assertions.assertThrows(InvalidCustomerIdException.class, ()-> model.deleteCustomer(0), "0 cannot be accepted ad CustomerId");
        Assertions.assertThrows(InvalidCustomerIdException.class, ()-> model.deleteCustomer(-12), "negative cannot be accepted ad CustomerId");

        Assertions.assertThrows(InvalidCustomerIdException.class, ()-> model.getCustomer(null), "null cannot be accepted ad CustomerId");
        Assertions.assertThrows(InvalidCustomerIdException.class, ()-> model.getCustomer(0), "0 cannot be accepted ad CustomerId");
        Assertions.assertThrows(InvalidCustomerIdException.class, ()-> model.getCustomer(-12), "negative cannot be accepted ad CustomerId");

        Assertions.assertThrows(InvalidCustomerIdException.class, ()-> model.attachCardToCustomer(validCard, null), "null cannot be accepted ad CustomerId");
        Assertions.assertThrows(InvalidCustomerIdException.class, ()-> model.attachCardToCustomer(validCard, 0), "0 cannot be accepted ad CustomerId");
        Assertions.assertThrows(InvalidCustomerIdException.class, ()-> model.attachCardToCustomer(validCard, -12), "negative cannot be accepted ad CustomerId");
    }
    @Test
    void invaludCustomerCardExceptThrower() throws InvalidPasswordException, InvalidUsernameException {
        login();
        Integer id = 1;
        String card1 = "abcdefghil";
        String card2 = "012345";
        String card3 = "111111111111111111111";
        Assertions.assertThrows(InvalidCustomerCardException.class, ()->model.modifyCustomer(id, validName, card1), "Card must be 10 digits");
        Assertions.assertThrows(InvalidCustomerCardException.class, ()->model.modifyCustomer(id, validName, card2), "Card must be 10 digits");
        Assertions.assertThrows(InvalidCustomerCardException.class, ()->model.modifyCustomer(id, validName, card3), "Card must be 10 digits");

        Assertions.assertThrows(InvalidCustomerCardException.class, ()->model.attachCardToCustomer(card1, id), "Card must be 10 digits");
        Assertions.assertThrows(InvalidCustomerCardException.class, ()->model.attachCardToCustomer(card2, id), "Card must be 10 digits");
        Assertions.assertThrows(InvalidCustomerCardException.class, ()->model.attachCardToCustomer(card3, id), "Card must be 10 digits");

        Assertions.assertThrows(InvalidCustomerCardException.class, ()->model.modifyPointsOnCard(card1, 1), "Card must be 10 digits");
        Assertions.assertThrows(InvalidCustomerCardException.class, ()->model.modifyPointsOnCard(card2, 1), "Card must be 10 digits");
        Assertions.assertThrows(InvalidCustomerCardException.class, ()->model.modifyPointsOnCard(card3, 1), "Card must be 10 digits");
    }

    @Test
    void invalidLogin() {
        Assertions.assertThrows(UnauthorizedException.class, ()-> model.defineCustomer(validName), "If not logged exception must be thrown");
        Assertions.assertThrows(UnauthorizedException.class, ()-> model.modifyCustomer(1, validName, null), "If not logged exception must be thrown");
        Assertions.assertThrows(UnauthorizedException.class, ()-> model.deleteCustomer(1), "If not logged exception must be thrown");
        Assertions.assertThrows(UnauthorizedException.class, ()-> model.getCustomer(1), "If not logged exception must be thrown");
        Assertions.assertThrows(UnauthorizedException.class, ()-> model.getAllCustomers(), "If not logged exception must be thrown");
        Assertions.assertThrows(UnauthorizedException.class, ()-> model.createCard(), "If not logged exception must be thrown");
        Assertions.assertThrows(UnauthorizedException.class, ()-> model.attachCardToCustomer("111111111", 1), "If not logged exception must be thrown");
        Assertions.assertThrows(UnauthorizedException.class, ()-> model.modifyPointsOnCard("111111111", 1), "If not logged exception must be thrown");
    }

    @AfterEach
    void afterTest () {
        model.reset();
    }
}
