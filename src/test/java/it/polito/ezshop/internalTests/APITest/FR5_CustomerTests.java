package it.polito.ezshop.internalTests.APITest;

import it.polito.ezshop.data.Customer;
import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;

public class FR5_CustomerTests {
    EZShopInterface model;
    final String username = "Dummy";
    final String password = "Dummy";
    final String validName = "customer";
    final String invalidName = "";
    String validCard;

    void login() throws InvalidPasswordException, InvalidUsernameException {
        model.login(username, password);
    }

    @Before
    public void startEzShop() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, UnauthorizedException {
        model = new EZShop();
        model.reset();
        model.createUser(username, password, "Administrator");
        login();
        validCard = model.createCard();
        model.logout();
    }

    @After
    public void clean(){
        model.reset();
    }


    //defineCustomer()
    @Test
    public void correctDefineCustomer() throws InvalidCustomerNameException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidCustomerIdException {
        login();
        Integer customerId = model.defineCustomer(validName);
        assertTrue("Returned customer Id must be > 0", customerId > 0);
        assertNotEquals("Container should be updated with the new customer", null, model.getCustomer(customerId));
        assertEquals("Name should be equal with the one passed", validName, model.getCustomer(customerId).getCustomerName());
        assertNull("For new customers there should not be a card attached", model.getCustomer(customerId).getCustomerCard());
        assertEquals("For new customers points should be 0", 0, model.getCustomer(customerId).getPoints(), 0);
    }

    @Test
    public void invalidCustomerNameDuringDefine() throws InvalidPasswordException, InvalidUsernameException {
        login();
        assertThrows(InvalidCustomerNameException.class, ()->model.defineCustomer(invalidName));
        assertThrows(InvalidCustomerNameException.class, ()->model.defineCustomer(null));
    }

    //modifyCustomer
    @Test
    public void correctModifyName() throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException {
        login();
        String newName = "name";
        Integer id = model.defineCustomer(validName);
        model.modifyCustomer(id, validName, validCard);
        boolean res = model.modifyCustomer(id, newName, null);
        assertTrue("Result should be true", res);
        assertEquals(newName, model.getCustomer(id).getCustomerName());
        assertEquals(validCard, model.getCustomer(id).getCustomerCard());
    }

    @Test
    public void correctModifyCard() throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException {
        login();
        Integer id = model.defineCustomer(validName);
        boolean res = model.modifyCustomer(id, validName, validCard);
        assertTrue("Result should be true", res);
        assertEquals(validCard, model.getCustomer(id).getCustomerCard());
    }

    @Test
    public void invalidCardCode() throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException {
        login();
        String card1 = "abcdefghil";
        String card2 = "012345";
        String card3 = "111111111111111111111";
        Integer id = model.defineCustomer(validName);
        boolean res;

        assertThrows(InvalidCustomerCardException.class, ()->model.modifyCustomer(id, validName, card1));
        assertThrows(InvalidCustomerCardException.class,()->model.modifyCustomer(id, validName, card2));
        assertThrows(InvalidCustomerCardException.class,()->model.modifyCustomer(id, validName, card3));



        Integer id2 = model.defineCustomer(validName);
        model.modifyCustomer(id2, validName, validCard);
        res = model.modifyCustomer(id, validName, validCard);
        assertFalse("Returned value must be false since there can't be 2 customers with the same Card", res);

    }

    @Test
    public void invalidCustomerNameDuringModify() throws InvalidCustomerNameException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException {
        login();
        Integer id = model.defineCustomer(validName);
        assertThrows(InvalidCustomerNameException.class, ()-> model.modifyCustomer(id, null, null));
        assertThrows(InvalidCustomerNameException.class, ()->model.modifyCustomer(id, "", null));
    }

    @Test
    public void badModifyCustomer() throws InvalidPasswordException, InvalidUsernameException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException, InvalidCustomerCardException {
        login();
        Integer id = model.defineCustomer(validName);


        assertThrows(InvalidCustomerIdException.class, ()->model.modifyCustomer(null, validName, validCard));
        assertThrows(InvalidCustomerIdException.class, ()->model.modifyCustomer(-1, validName, validCard));

        assertFalse("Card has not been generated", model.modifyCustomer(id, validName, "0123456789"));
        assertFalse("customer id doesnt exist",model.modifyCustomer(23, validName, null));

    }

    //deleteCustomer()

    @Test
    public void correctDeleteCustomer() throws InvalidCustomerIdException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidCustomerNameException {
        login();
        Integer id = model.defineCustomer(validName);
        boolean res = model.deleteCustomer(id);
        assertTrue("Returned value must be true", res);
        assertNull("The customer has not been correctly deleted", model.getCustomer(id));
    }
    @Test
    public void invalidCustomerIdToDelete() throws InvalidCustomerIdException, UnauthorizedException, InvalidCustomerNameException, InvalidPasswordException, InvalidUsernameException {
        login();
        model.defineCustomer(validName);
        model.defineCustomer(validName);
        model.defineCustomer(validName);
        model.defineCustomer(validName);
        int nCustomer = model.getAllCustomers().size();
        boolean res = model.deleteCustomer(1000);
        assertFalse("Returned value must be false", res);
        assertEquals(nCustomer, model.getAllCustomers().size(), 0);
        assertThrows(InvalidCustomerIdException.class, ()-> model.deleteCustomer(null));
        assertThrows(InvalidCustomerIdException.class, ()-> model.deleteCustomer(0));
        assertThrows(InvalidCustomerIdException.class, ()-> model.deleteCustomer(-12));
    }

    // getCustomer()
    @Test
    public void correctGetCustomer() throws InvalidPasswordException, InvalidUsernameException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException {
        login();
        Integer id = model.defineCustomer(validName);
        Customer c = model.getCustomer(id);
        assertEquals(id, c.getId());
        assertEquals(validName, c.getCustomerName());
        assertNotEquals(null, c);
    }
    @Test
    public void customerDoesNotExist() throws InvalidCustomerIdException, UnauthorizedException, InvalidCustomerNameException, InvalidPasswordException, InvalidUsernameException {
        login();
        Integer id = model.defineCustomer(validName);
        Customer c = model.getCustomer(1000);
        assertNull(c);
        assertThrows(InvalidCustomerIdException.class, ()->model.getCustomer(null));
        assertThrows(InvalidCustomerIdException.class, ()->model.getCustomer(-12));
        assertThrows(InvalidCustomerIdException.class, ()->model.getCustomer(0));
    }

    // getAllCustomers
    @Test
    public void correctGetAllCustomers() throws UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidCustomerNameException {
        login();
        assertEquals(0, model.getAllCustomers().size());
        model.defineCustomer(validName);
        model.defineCustomer(validName);
        model.defineCustomer(validName);
        model.defineCustomer(validName);
        assertEquals(4, model.getAllCustomers().size());
    }

    // createCard()
    @Test
    public void correctCreateCard() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException {
        login();
        String code = model.createCard();
        assertNotEquals(null, code);
        assertNotEquals("", code);
    }

    // attachCardToCustomer()
    @Test
    public void correctAttachCardToCustomer() throws InvalidPasswordException, InvalidUsernameException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException, InvalidCustomerCardException {
        login();
        Integer id = model.defineCustomer(validName);
        boolean res = model.attachCardToCustomer(validCard, id);
        assertTrue(res);
        assertEquals(validCard, model.getCustomer(id).getCustomerCard());
    }
    @Test
    public void invalidAttachCardToCustomer() throws InvalidPasswordException, InvalidUsernameException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException, InvalidCustomerCardException {
        login();
        Integer id = model.defineCustomer(validName);
        model.attachCardToCustomer(validCard, id);
        id = model.defineCustomer(validName);
        boolean res;

        res = model.attachCardToCustomer(validCard, 1000);
        assertFalse("Returned value must be false when there is no customer with a given id", res);
        assertThrows((InvalidCustomerIdException.class),()->model.attachCardToCustomer(validCard, null));
        assertThrows((InvalidCustomerIdException.class),()->model.attachCardToCustomer(validCard, 0));
        assertThrows((InvalidCustomerIdException.class),()->model.attachCardToCustomer(validCard, -12));

        assertFalse(model.attachCardToCustomer("0123456789", id));

    }

    // modifyPointsOnCard()
    @Test
    public void correctModifyPointsOnCard() throws InvalidPasswordException, InvalidUsernameException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException, InvalidCustomerCardException {
        login();
        Integer id = model.defineCustomer(validName);
        model.attachCardToCustomer(validCard, id);
        boolean res = model.modifyPointsOnCard(validCard, 10);
        assertTrue("Returned value must be true", res);
        assertEquals(10, model.getCustomer(id).getPoints(), 0);
        res = model.modifyPointsOnCard(validCard, -10);
        assertTrue("Returned value must be true even if points go to 0", res);
        assertEquals(0, model.getCustomer(id).getPoints(), 0);
    }

    @Test
    public void invalidModifyPointsOnCard() throws InvalidPasswordException, InvalidUsernameException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerCardException, InvalidCustomerIdException {
        login();
        Integer id = model.defineCustomer(validName);
        boolean res = model.modifyPointsOnCard(validCard, -10);
        model.attachCardToCustomer(validCard, id);
        assertFalse(res);
        assertEquals(0, model.getCustomer(id).getPoints(), 0);
        model.modifyPointsOnCard(validCard, 50);
        res = model.modifyPointsOnCard(validCard, -100);
        assertFalse(res);
        assertEquals(50, model.getCustomer(id).getPoints(), 0);
        assertThrows(InvalidCustomerCardException.class, ()->model.modifyPointsOnCard("wrongcard", 1));

        assertFalse(model.modifyPointsOnCard("0123456789", 10));
    }

    //GENERAL
    @Test
    public void invaludCustomerCardExceptThrower() throws InvalidPasswordException, InvalidUsernameException {
        login();
        Integer id = 1;
        String card1 = "abcdefghil";
        String card2 = "012345";
        String card3 = "111111111111111111111";
        assertThrows(InvalidCustomerCardException.class, ()->model.modifyCustomer(id, validName, card1));
        assertThrows(InvalidCustomerCardException.class, ()->model.modifyCustomer(id, validName, card2));
        assertThrows(InvalidCustomerCardException.class, ()->model.modifyCustomer(id, validName, card3));

        assertThrows(InvalidCustomerCardException.class, ()->model.attachCardToCustomer(card1, id));
        assertThrows(InvalidCustomerCardException.class, ()->model.attachCardToCustomer(card2, id));
        assertThrows(InvalidCustomerCardException.class, ()->model.attachCardToCustomer(card3, id));

        assertThrows(InvalidCustomerCardException.class, ()->model.modifyPointsOnCard(card1, 1));
        assertThrows(InvalidCustomerCardException.class, ()->model.modifyPointsOnCard(card2, 1));
        assertThrows(InvalidCustomerCardException.class, ()->model.modifyPointsOnCard(card3, 1));
    }

    @Test
    public void invalidLogin() {
        assertThrows(UnauthorizedException.class, ()-> model.defineCustomer(validName));
        assertThrows(UnauthorizedException.class, ()-> model.modifyCustomer(1, validName, null));
        assertThrows(UnauthorizedException.class, ()-> model.deleteCustomer(1));
        assertThrows(UnauthorizedException.class, ()-> model.getCustomer(1));
        assertThrows(UnauthorizedException.class, ()-> model.getAllCustomers());
        assertThrows(UnauthorizedException.class, ()-> model.createCard());
        assertThrows(UnauthorizedException.class, ()-> model.attachCardToCustomer("111111111", 1));
        assertThrows(UnauthorizedException.class, ()-> model.modifyPointsOnCard("111111111", 1));
    }

    @After
    public void afterTest() {
        model.reset();
    }
}
