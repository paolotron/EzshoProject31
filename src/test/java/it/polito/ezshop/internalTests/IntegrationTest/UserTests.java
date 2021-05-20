package it.polito.ezshop.internalTests.IntegrationTest;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.model.EzShopModel;
import it.polito.ezshop.data.User;
import it.polito.ezshop.exceptions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserTests {
    EZShopInterface model;
    final String username = "USER";
    final String password = "PSW";
    final String admin_username = "Admin";
    final String admin_psw = "password12345678";

    @BeforeEach
    void startEzShop() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
        model = new it.polito.ezshop.data.EZShop();
        model.reset();
        model.createUser(admin_username, admin_psw, "Administrator");
    }
    @AfterEach
    void closeEzShop(){
        model.reset();
    }

    @Test
    void InvalidUsername() {
        Assertions.assertThrows(InvalidUsernameException.class, () -> model.createUser("", password, "Administrator"));
        Assertions.assertThrows(InvalidUsernameException.class, () -> model.createUser(null, password, "Administrator"));
    }

    @Test
    void InvalidRole() {
        Assertions.assertThrows(InvalidRoleException.class, () -> model.createUser(username, password, ""));
        Assertions.assertThrows(InvalidRoleException.class, () -> model.createUser(username, password, null));
        Assertions.assertThrows(InvalidRoleException.class, () -> model.createUser(username, password, "Admin"));
        Assertions.assertThrows(InvalidRoleException.class, () -> model.createUser(username, password, "Casher"));
    }

    @Test
    void InvalidPassword() {
        Assertions.assertThrows(InvalidPasswordException.class, () -> model.createUser(username, "", "Administrator"));
        Assertions.assertThrows(InvalidPasswordException.class, () -> model.createUser(username, null, "Administrator"));
    }

    @Test
    void createUser() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
        Integer id = model.createUser(username,password,"ShopManager");
        Assertions.assertTrue(id > 0);
        id = model.createUser("newUsername","newPassword","Cashier");
        Assertions.assertTrue(id > 0);
        //try to create another users with same username
        Assertions.assertEquals(-1, model.createUser(username, "anotherpassword", "ShopManager"));
        Assertions.assertEquals(-1, model.createUser(username, password, "Cashier"));
    }

    @Test
    void InvalidUser() throws InvalidPasswordException, InvalidUsernameException {
        model.login(admin_username,admin_psw);
        Assertions.assertThrows(InvalidUserIdException.class, ()-> model.deleteUser(0));
        Assertions.assertThrows(InvalidUserIdException.class, ()-> model.deleteUser(-5));
        Assertions.assertThrows(InvalidUserIdException.class, ()-> model.deleteUser(null));
    }

    @Test
    void UnauthorizedUser() throws InvalidPasswordException, InvalidUsernameException, InvalidRoleException {
        Integer id = model.createUser(username, password, "Cashier");
        model.login(username, password);
        Assertions.assertThrows(UnauthorizedException.class, ()-> model.deleteUser(id));
        model.logout();
        Assertions.assertThrows(UnauthorizedException.class, ()-> model.deleteUser(10));

        model.login(username, password);
        Assertions.assertThrows(UnauthorizedException.class, ()-> model.getAllUsers());
        model.logout();
        Assertions.assertThrows(UnauthorizedException.class, ()-> model.getAllUsers());
    }

    @Test
    void deleteUser() throws InvalidPasswordException, InvalidUsernameException, InvalidRoleException, InvalidUserIdException, UnauthorizedException {
        Integer id;
        model.login(admin_username,admin_psw);
        id = model.createUser(username,password, "Cashier");
        Assertions.assertTrue(model.deleteUser(id));
        Assertions.assertFalse(model.deleteUser(id));
        Assertions.assertFalse(model.deleteUser(1000));
    }

    @Test
    void getAllUsers() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidRoleException, InvalidUserIdException {
        model.login(admin_username,admin_psw);
        Assertions.assertNotNull(model.getAllUsers());
        Integer id = model.createUser(username,password,"Cashier");
        Integer admin_id = model.getAllUsers().get(0).getId();
        Assertions.assertEquals(admin_username, model.getAllUsers().get(0).getUsername());
        Assertions.assertEquals(id, model.getAllUsers().get(1).getId());
        Assertions.assertEquals(username, model.getAllUsers().get(1).getUsername());
        Assertions.assertThrows(IndexOutOfBoundsException.class, ()->model.getAllUsers().get(2));
        model.deleteUser(id);
        model.deleteUser(admin_id);
        Assertions.assertTrue(model.getAllUsers().isEmpty());

    }

    @Test
    void GetUser () throws InvalidPasswordException, InvalidUsernameException, InvalidUserIdException, UnauthorizedException {
        model.logout();
        Assertions.assertThrows(UnauthorizedException.class, ()-> model.getUser(12));
        model.login(username,password);
        Assertions.assertThrows(UnauthorizedException.class, ()-> model.getUser(12));
        model.login(admin_username,admin_psw);
        Assertions.assertThrows(InvalidUserIdException.class, ()-> model.getUser(0));
        Assertions.assertThrows(InvalidUserIdException.class, ()-> model.getUser(-1));
        Assertions.assertThrows(InvalidUserIdException.class, ()-> model.getUser(null));
        Assertions.assertNull(model.getUser(1233647));

    }

    @Test
    void UserRights() throws InvalidPasswordException, InvalidUsernameException, InvalidRoleException, UnauthorizedException, InvalidUserIdException {
        model.logout();
        Assertions.assertThrows(UnauthorizedException.class, ()-> model.updateUserRights(2, "dummy"));
        model.login(username,password);
        Assertions.assertThrows(UnauthorizedException.class, ()-> model.updateUserRights(2, "dummy"));
        model.login(admin_username,admin_psw);
        final Integer id = model.createUser("dummy", "dummy","ShopManager");
        Assertions.assertThrows(InvalidRoleException.class, ()->model.updateUserRights(id, ""));
        Assertions.assertThrows(InvalidRoleException.class, ()->model.updateUserRights(id, null));
        Assertions.assertThrows(InvalidRoleException.class, ()->model.updateUserRights(id, "cas"));

        Assertions.assertThrows(InvalidUserIdException.class, ()->model.updateUserRights(0, "ShopManager"));
        Assertions.assertThrows(InvalidUserIdException.class, ()->model.updateUserRights(-3, "ShopManager"));
        Assertions.assertThrows(InvalidUserIdException.class, ()->model.updateUserRights(null, "ShopManager"));

        Assertions.assertTrue(model.updateUserRights(id, "Cashier"));
        Assertions.assertFalse(model.updateUserRights(100,"Cashier"));
    }
}
