package it.polito.ezshop.internalTests.APITest;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.data.User;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.EzShopModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FR1_UserTests {
    EZShopInterface model;
    EzShopModel ez;
    final String username = "USER";
    final String password = "PSW";
    final String admin_username = "Admin";
    final String admin_psw = "password12345678";


    @Before
    public void startEzShop() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
        model = new it.polito.ezshop.data.EZShop();
        ez = new EzShopModel();
        model.reset();
        model.createUser(admin_username, admin_psw, "Administrator");

    }
    @After
    public void closeEzShop(){
        model.reset();
    }


    @Test
    public void InvalidUsername() {
        assertThrows(InvalidUsernameException.class, () -> model.createUser("", password, "Administrator"));
        assertThrows(InvalidUsernameException.class, () -> model.createUser(null, password, "Administrator"));
    }

    @Test
    public void InvalidRole() {
        assertThrows(InvalidRoleException.class, () -> model.createUser(username, password, ""));
        assertThrows(InvalidRoleException.class, () -> model.createUser(username, password, null));
        assertThrows(InvalidRoleException.class, () -> model.createUser(username, password, "Admin"));
        assertThrows(InvalidRoleException.class, () -> model.createUser(username, password, "Casher"));
    }

    @Test
    public void InvalidPassword() {
        assertThrows(InvalidPasswordException.class, () -> model.createUser(username, "", "Administrator"));
        assertThrows(InvalidPasswordException.class, () -> model.createUser(username, null, "Administrator"));
    }

    @Test
    public void createUser() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
        int id = model.createUser(username,password,"ShopManager");
        assertTrue(id > 0);
        id = model.createUser("newUsername","newPassword","Cashier");
        assertTrue(id > 0);
        //try to create another users with same username
        int none = -1;
        assertEquals(none, model.createUser(username, "anotherpassword", "ShopManager"), 0);
        assertEquals(none, model.createUser(username, password, "Cashier"), 0);
    }

    @Test
    public void login() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
        model.createUser(username,password,"Cashier");
        assertNull(model.login("Omar", "psw346"));
        assertNull(model.login("Luca", password));
        User user = model.login(username,password);
        assertEquals(user.getUsername(),username);
        assertEquals(user.getPassword(),password);
    }

    @Test
    public void invalidUsername() {
        assertThrows(InvalidUsernameException.class, () -> model.login("", password));
        assertThrows(InvalidUsernameException.class, () -> model.login(null, password));
    }
    @Test
    public void invalidPassword() {
        assertThrows(InvalidPasswordException.class, ()-> model.login(username, ""));
        assertThrows(InvalidPasswordException.class, ()-> model.login(username, null));
    }


    @Test
    public void InvalidUser() throws InvalidPasswordException, InvalidUsernameException {
        model.login(admin_username,admin_psw);
        assertThrows(InvalidUserIdException.class, ()-> model.deleteUser(0));
        assertThrows(InvalidUserIdException.class, ()-> model.deleteUser(-5));
        assertThrows(InvalidUserIdException.class, ()-> model.deleteUser(null));
    }

    @Test
    public void UnauthorizedUser() throws InvalidPasswordException, InvalidUsernameException, InvalidRoleException {
        Integer id = model.createUser(username, password, "Cashier");
        model.login(username, password);
        assertThrows(UnauthorizedException.class, ()-> model.deleteUser(id));
        model.logout();
        assertThrows(UnauthorizedException.class, ()-> model.deleteUser(10));

        model.login(username, password);
        assertThrows(UnauthorizedException.class, ()-> model.getAllUsers());
        model.logout();
        assertThrows(UnauthorizedException.class, ()-> model.getAllUsers());
    }

    @Test
    public void deleteUser() throws InvalidPasswordException, InvalidUsernameException, InvalidRoleException, InvalidUserIdException, UnauthorizedException {
        Integer id;
        model.login(admin_username,admin_psw);
        id = model.createUser(username,password, "Cashier");
        assertTrue(model.deleteUser(id));
        assertFalse(model.deleteUser(id));
        assertFalse(model.deleteUser(1000));
    }

    @Test
    public void getAllUsers() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidRoleException, InvalidUserIdException {
        model.login(admin_username,admin_psw);
        assertNotNull(model.getAllUsers());
        Integer id = model.createUser(username,password,"Cashier");
        Integer admin_id = model.getAllUsers().get(0).getId();
        assertEquals(admin_username, model.getAllUsers().get(0).getUsername());
        model = new EZShop();
        model.login(admin_username,admin_psw);
        assertEquals(id, model.getAllUsers().get(1).getId());
        assertEquals(username, model.getAllUsers().get(1).getUsername());
        assertThrows(IndexOutOfBoundsException.class, ()->model.getAllUsers().get(2));
        model.deleteUser(id);
        model.deleteUser(admin_id);
        assertTrue(model.getAllUsers().isEmpty());

    }

    @Test
    public void GetUser() throws InvalidPasswordException, InvalidUsernameException, InvalidUserIdException, UnauthorizedException {
        model.logout();
        assertThrows(UnauthorizedException.class, ()-> model.getUser(12));
        model.login(username,password);
        assertThrows(UnauthorizedException.class, ()-> model.getUser(12));
        model.login(admin_username,admin_psw);
        assertThrows(InvalidUserIdException.class, ()-> model.getUser(0));
        assertThrows(InvalidUserIdException.class, ()-> model.getUser(-1));
        assertThrows(InvalidUserIdException.class, ()-> model.getUser(null));
        assertNull(model.getUser(1233647));

    }

    @Test
    public void UserRights() throws InvalidPasswordException, InvalidUsernameException, InvalidRoleException, UnauthorizedException, InvalidUserIdException {
        model.logout();
        assertThrows(UnauthorizedException.class, ()-> model.updateUserRights(2, "dummy"));
        model.login(username,password);
        assertThrows(UnauthorizedException.class, ()-> model.updateUserRights(2, "dummy"));
        model.login(admin_username,admin_psw);
        final Integer id = model.createUser("dummy", "dummy","ShopManager");
        assertThrows(InvalidRoleException.class, ()->model.updateUserRights(id, ""));
        assertThrows(InvalidRoleException.class, ()->model.updateUserRights(id, null));
        assertThrows(InvalidRoleException.class, ()->model.updateUserRights(id, "cas"));

        assertThrows(InvalidUserIdException.class, ()->model.updateUserRights(0, "ShopManager"));
        assertThrows(InvalidUserIdException.class, ()->model.updateUserRights(-3, "ShopManager"));
        assertThrows(InvalidUserIdException.class, ()->model.updateUserRights(null, "ShopManager"));

        assertTrue(model.updateUserRights(id, "Cashier"));
        assertFalse(model.updateUserRights(100,"Cashier"));
    }

    @Test
    public void correctGetUserById() throws UnauthorizedException, InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidUserIdException {

        User u = ez.createUser("Andrea", "lol", "Administrator");
        ez.login("Andrea", "lol");
        int id = u.getId();
        assertTrue("UserId must be > 0", id > 0);
        User newU = ez.getUserById(id);
        assertEquals("User not found", id, newU.getId(), 0);
    }

    @Test
    public void wrongGetUserById() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidUserIdException, UnauthorizedException {
        User u = ez.createUser("Andrea", "lol", "Administrator");
        ez.login("Andrea", "lol");
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
        ez.login("Andrea", "lol");
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
        ez.login("Andrea", "lol");
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
    public void wrongUsernameLogin() throws InvalidRoleException, InvalidUsernameException, InvalidPasswordException {
        User u1 = ez.createUser("Andrea", "lol", "Administrator");
        assertThrows(InvalidUsernameException.class, ()->ez.login("", "lol"));
        assertThrows(InvalidUsernameException.class, ()->ez.login(null, "lol"));
        assertNull(ez.login("Andrea", "lel"));
        assertNull(ez.login("lel", "lol"));
        assertNull(ez.login("lel", "lel"));
    }

    @Test
    public void logoutTest() throws InvalidRoleException, InvalidUsernameException, InvalidPasswordException {
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

}
