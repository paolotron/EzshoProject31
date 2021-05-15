package it.polito.ezshop.internalTests.UnitTest;

import it.polito.ezshop.data.User;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.CustomerModel;
import it.polito.ezshop.model.EzShopModel;
import it.polito.ezshop.model.Roles;
import it.polito.ezshop.model.UserModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EzShopModelTest {
    EzShopModel ez = new EzShopModel();

    @Test
    void correctGetUserById() throws UnauthorizedException, InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidUserIdException {
        User u = ez.createUser("Andrea", "lol", "Administrator");
        int id = u.getId();
        Assertions.assertTrue(id > 0, "UserId must be > 0");
        User newU = ez.getUserById(id);
        Assertions.assertEquals(id, newU.getId(), "User not found");
    }

    @Test
    void wrongGetUserById() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidUserIdException, UnauthorizedException {
        User u = ez.createUser("Andrea", "lol", "Administrator");
        Assertions.assertThrows(InvalidUserIdException.class, ()->ez.getUserById(null));
        Assertions.assertThrows(InvalidUserIdException.class, ()->ez.getUserById(-1));
        Assertions.assertThrows(InvalidUserIdException.class, ()->ez.getUserById(0));
    }

    @Test
    void correctDeleteUserById() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidUserIdException, UnauthorizedException {
        User u1 = ez.createUser("Andrea", "lol", "Administrator");
        User u2 = ez.createUser("Andrea", "lol", "Administrator");
        User u3 = ez.createUser("Andrea", "lol", "Administrator");

        Assertions.assertTrue(ez.deleteUserById(u1.getId()));
        Assertions.assertTrue(ez.deleteUserById(u2.getId()));
        Assertions.assertTrue(ez.deleteUserById(u3.getId()));
    }

    @Test
    void wrongDeleteUserById() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidUserIdException, UnauthorizedException {
        User u1 = ez.createUser("Andrea", "lol", "Administrator");
        ez.deleteUserById(u1.getId());

        Assertions.assertThrows(InvalidUserIdException.class, ()->ez.deleteUserById(u1.getId()), "User should have been already deleted");
    }

    @Test
    void correctLogin(){

    }

    @Test
    void wrongLogin(){

    }

}
