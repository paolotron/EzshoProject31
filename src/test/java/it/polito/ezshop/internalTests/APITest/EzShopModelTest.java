package it.polito.ezshop.internalTests.APITest;

import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.data.User;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.EzShopModel;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;


public class EzShopModelTest {
    EzShopModel ez = new EzShopModel();

    @After
    public void reset(){
        ez.reset();
    }

    void login() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
        User u = ez.createUser("Palo", "lol", "Administrator");
        int id = u.getId();
        ez.login("Palo", "lol");
    }

    @Test
    public void correctGetUserById() throws UnauthorizedException, InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidUserIdException {
        login();
        User u = ez.createUser("Andrea", "lol", "Administrator");
        int id = u.getId();
        assertTrue("UserId must be > 0", id > 0);
        User newU = ez.getUserById(id);
        assertEquals("User not found", id, newU.getId(), 0);
    }

    @Test
    public void wrongGetUserById() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidUserIdException, UnauthorizedException {
        login();
        User u = ez.createUser("Andrea", "lol", "Administrator");
        assertThrows(InvalidUserIdException.class, ()->ez.getUserById(null));
        assertThrows(InvalidUserIdException.class, ()->ez.getUserById(-1));
        assertThrows(InvalidUserIdException.class, ()->ez.getUserById(0));
    }

    @Test
    public void correctDeleteUserById() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidUserIdException, UnauthorizedException {
        User u1 = ez.createUser("Andrea", "lol", "Administrator");
        User u2 = ez.createUser("Nndrea", "lol", "Administrator");
        User u3 = ez.createUser("Mndrea", "lol", "Administrator");
        login();
        assertTrue(ez.deleteUserById(u1.getId()));
        assertTrue(ez.deleteUserById(u2.getId()));
        assertTrue(ez.deleteUserById(u3.getId()));
        assertNull(ez.getUserById(u1.getId()));
        assertNull(ez.getUserById(u2.getId()));
        assertNull(ez.getUserById(u3.getId()));
    }

    @Test
    public void wrongDeleteUserById() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidUserIdException, UnauthorizedException {
        User u1 = ez.createUser("Andrea", "lol", "Administrator");
        login();
        ez.deleteUserById(u1.getId());
        assertFalse("User should have been already deleted", ez.deleteUserById(u1.getId()));
    }

    @Test
    public void correctLogin() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
        User u1 = ez.createUser("Andrea", "lol", "Administrator");
        User u2 = ez.login("Andrea", "lol");
        assertEquals(u1.getId(), u2.getId());
        assertEquals(u1.getUsername(), u2.getUsername());
    }

    @Test
    public void wrongPasswordLogin() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
        User u1 = ez.createUser("Andrea", "lol", "Administrator");
        assertThrows(InvalidPasswordException.class, ()->ez.login("Andrea", ""));
        assertThrows(InvalidPasswordException.class, ()->ez.login("Andrea", null));
    }

    @Test
    public void wrongUsernameLogin() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
        User u1 = ez.createUser("Andrea", "lol", "Administrator");
        assertThrows(InvalidUsernameException.class, ()->ez.login("", "lol"));
        assertThrows(InvalidUsernameException.class, ()->ez.login(null, "lol"));
        assertNull(ez.login("Andrea", "lel"));
        assertNull(ez.login("lel", "lol"));
        assertNull(ez.login("lel", "lel"));
    }

    @Test
    public void logoutTest() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
        User u1 = ez.createUser("Andrea", "lol", "Administrator");
        ez.login(u1.getUsername(), u1.getPassword());
        assertTrue(ez.logout());
    }

    @Test
    public void wrongUsernameCreateUser(){
        assertThrows(InvalidUsernameException.class, ()->ez.createUser(null, "lol", "lol"));
        assertThrows(InvalidUsernameException.class, ()->ez.createUser("", "lol", "lol"));

    }

    @Test
    public void wrongPasswordCreateUser(){
        assertThrows(InvalidPasswordException.class, ()->ez.createUser("lol", "", "lol"));
        assertThrows(InvalidPasswordException.class, ()->ez.createUser("lol", null, "lol"));
    }

    @Test
    public void wrongRoleCreateUser(){
        assertThrows(InvalidRoleException.class, ()->ez.createUser("lol", "lol", ""));
        assertThrows(InvalidRoleException.class, ()->ez.createUser("lol", "lol", null));
    }

    @Test
    public void correctCreateOrder() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidQuantityException {
        login();
        ProductType p = ez.createProduct("desc", "6291041500213", 10.0, null);
        ez.createUser("Andrea", "lol", "Administrator");
        ez.recordBalanceUpdate(100);
        int id = ez.createOrder(p.getBarCode(), 3, p.getPricePerUnit());
        assertTrue("newOrderId should be > 0", id > 0);
    }

    @Test
    public void wrongProductCodeCreateOrder() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException, InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
        login();
        ProductType p = ez.createProduct("desc", "6291041500213", 10.0, null);
        ez.createUser("Andrea", "lol", "Administrator");
        assertThrows(InvalidProductCodeException.class, ()->ez.createOrder("", 1, p.getPricePerUnit()));
        assertThrows(InvalidProductCodeException.class, ()->ez.createOrder(null, 1, p.getPricePerUnit()));
        assertEquals("returned value should be -1 since product doesn't exist", -1, ez.createOrder("6291041500214", 1, p.getPricePerUnit()), 0);
    }

    @Test
    public void wrongQuantityCreateOrder() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException, InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
        login();
        ProductType p = ez.createProduct("desc", "6291041500213", 10.0, null);
        ez.createUser("Andrea", "lol", "Administrator");
        assertThrows(InvalidQuantityException.class, ()->ez.createOrder("6291041500213", 0, p.getPricePerUnit()));
        assertThrows(InvalidQuantityException.class, ()->ez.createOrder("6291041500213", -1, p.getPricePerUnit()));
    }

    @Test
    public void wrongPriceCreateOrder() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
        login();
        ProductType p = ez.createProduct("desc", "6291041500213", 10.0, null);
        ez.createUser("Andrea", "lol", "Administrator");
        assertThrows(InvalidPricePerUnitException.class, ()->ez.createOrder("6291041500213", 1, -1.0));
        assertThrows(InvalidPricePerUnitException.class, ()->ez.createOrder("6291041500213", 1, 0));
    }

}
