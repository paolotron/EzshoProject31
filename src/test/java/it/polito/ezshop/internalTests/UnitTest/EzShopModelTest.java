package it.polito.ezshop.internalTests.UnitTest;

import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.data.User;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.CustomerModel;
import it.polito.ezshop.model.EzShopModel;
import it.polito.ezshop.model.Roles;
import it.polito.ezshop.model.UserModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EzShopModelTest {
    EzShopModel ez = new EzShopModel();

    @AfterEach
    void reset(){
        ez.reset();
    }

    void login() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
        User u = ez.createUser("Palo", "lol", "Administrator");
        int id = u.getId();
        ez.login("Palo", "lol");
    }

    @Test
    void correctGetUserById() throws UnauthorizedException, InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidUserIdException {
        login();
        User u = ez.createUser("Andrea", "lol", "Administrator");
        int id = u.getId();
        Assertions.assertTrue(id > 0, "UserId must be > 0");
        User newU = ez.getUserById(id);
        Assertions.assertEquals(id, newU.getId(), "User not found");
    }

    @Test
    void wrongGetUserById() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidUserIdException, UnauthorizedException {
        login();
        User u = ez.createUser("Andrea", "lol", "Administrator");
        Assertions.assertThrows(InvalidUserIdException.class, ()->ez.getUserById(null));
        Assertions.assertThrows(InvalidUserIdException.class, ()->ez.getUserById(-1));
        Assertions.assertThrows(InvalidUserIdException.class, ()->ez.getUserById(0));
    }

    @Test
    void correctDeleteUserById() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidUserIdException, UnauthorizedException {
        User u1 = ez.createUser("Andrea", "lol", "Administrator");
        User u2 = ez.createUser("Nndrea", "lol", "Administrator");
        User u3 = ez.createUser("Mndrea", "lol", "Administrator");
        login();
        Assertions.assertTrue(ez.deleteUserById(u1.getId()));
        Assertions.assertTrue(ez.deleteUserById(u2.getId()));
        Assertions.assertTrue(ez.deleteUserById(u3.getId()));
        Assertions.assertNull(ez.getUserById(u1.getId()));
        Assertions.assertNull(ez.getUserById(u2.getId()));
        Assertions.assertNull(ez.getUserById(u3.getId()));
    }

    @Test
    void wrongDeleteUserById() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidUserIdException, UnauthorizedException {
        User u1 = ez.createUser("Andrea", "lol", "Administrator");
        login();
        ez.deleteUserById(u1.getId());
        Assertions.assertFalse(ez.deleteUserById(u1.getId()), "User should have been already deleted");
    }

    @Test
    void correctLogin() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
        User u1 = ez.createUser("Andrea", "lol", "Administrator");
        User u2 = ez.login("Andrea", "lol");
        Assertions.assertEquals(u1.getId(), u2.getId());
        Assertions.assertEquals(u1.getUsername(), u2.getUsername());
    }

    @Test
    void wrongPasswordLogin() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
        User u1 = ez.createUser("Andrea", "lol", "Administrator");
        Assertions.assertThrows(InvalidPasswordException.class, ()->ez.login("Andrea", ""));
        Assertions.assertThrows(InvalidPasswordException.class, ()->ez.login("Andrea", null));
    }

    @Test
    void wrongUsernameLogin() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
        User u1 = ez.createUser("Andrea", "lol", "Administrator");
        Assertions.assertThrows(InvalidUsernameException.class, ()->ez.login("", "lol"));
        Assertions.assertThrows(InvalidUsernameException.class, ()->ez.login(null, "lol"));
        Assertions.assertNull(ez.login("Andrea", "lel"), "password is wrong, the result should be null");
        Assertions.assertNull(ez.login("lel", "lol"), "username is wrong, the result should be null");
        Assertions.assertNull(ez.login("lel", "lel"), "username and password are wrong, the result should be null");
    }

    @Test
    void logoutTest() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
        User u1 = ez.createUser("Andrea", "lol", "Administrator");
        ez.login(u1.getUsername(), u1.getPassword());
        Assertions.assertTrue(ez.logout());
    }

    @Test
    void wrongUsernameCreateUser(){
        Assertions.assertThrows(InvalidUsernameException.class, ()->ez.createUser(null, "lol", "lol"));
        Assertions.assertThrows(InvalidUsernameException.class, ()->ez.createUser("", "lol", "lol"));

    }

    @Test
    void wrongPasswordCreateUser(){
        Assertions.assertThrows(InvalidPasswordException.class, ()->ez.createUser("lol", "", "lol"));
        Assertions.assertThrows(InvalidPasswordException.class, ()->ez.createUser("lol", null, "lol"));
    }

    @Test
    void wrongRoleCreateUser(){
        Assertions.assertThrows(InvalidRoleException.class, ()->ez.createUser("lol", "lol", ""));
        Assertions.assertThrows(InvalidRoleException.class, ()->ez.createUser("lol", "lol", null));
    }

    //TODO: getBalanceTest

    @Test
    void correctCreateOrder() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidQuantityException {
        login();
        ProductType p = ez.createProduct("desc", "6291041500213", 10.0, null);
        ez.createUser("Andrea", "lol", "Administrator");
        ez.recordBalanceUpdate(100);
        int id = ez.createOrder(p.getBarCode(), 3, p.getPricePerUnit());
        Assertions.assertTrue(id > 0, "newOrderId should be > 0");
    }

    @Test
    void wrongProductCodeCreateOrder() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException, InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
        login();
        ProductType p = ez.createProduct("desc", "6291041500213", 10.0, null);
        ez.createUser("Andrea", "lol", "Administrator");
        Assertions.assertThrows(InvalidProductCodeException.class, ()->ez.createOrder("", 1, p.getPricePerUnit()));
        Assertions.assertThrows(InvalidProductCodeException.class, ()->ez.createOrder(null, 1, p.getPricePerUnit()));
        Assertions.assertEquals(-1, ez.createOrder("6291041500214", 1, p.getPricePerUnit()), "returned value should be -1 since product doesn't exist");
    }

    @Test
    void wrongQuantityCreateOrder() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException, InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
        login();
        ProductType p = ez.createProduct("desc", "6291041500213", 10.0, null);
        ez.createUser("Andrea", "lol", "Administrator");
        Assertions.assertThrows(InvalidQuantityException.class, ()->ez.createOrder("6291041500213", 0, p.getPricePerUnit()));
        Assertions.assertThrows(InvalidQuantityException.class, ()->ez.createOrder("6291041500213", -1, p.getPricePerUnit()));
    }

    @Test
    void wrongPriceCreateOrder() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException, InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
        login();
        ProductType p = ez.createProduct("desc", "6291041500213", 10.0, null);
        ez.createUser("Andrea", "lol", "Administrator");
        Assertions.assertThrows(InvalidPricePerUnitException.class, ()->ez.createOrder("6291041500213", 1, -1.0));
        Assertions.assertThrows(InvalidPricePerUnitException.class, ()->ez.createOrder("6291041500213", 1, 0));
    }

}
