package it.polito.ezshop.internalTests.UnitTest;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.model.UserModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserModelTesting {
    @Test
    void testConstructor() throws InvalidRoleException {
        new UserModel("Paolo", "Password", "Cashier");
        new UserModel("Paolo", "Password", "Administrator");
        new UserModel("Paolo", "Password", "ShopManager");
        Assertions.assertThrows(InvalidRoleException.class, ()->new UserModel("Paolo", "Password", "sopManag"));
    }
    @Test
    void testInvalidPassword(){
        UserModel user = new UserModel();
        user.setPassword("Password");
        Assertions.assertThrows(InvalidPasswordException.class,()->user.checkPassword(""));
        Assertions.assertThrows(InvalidPasswordException.class,()->user.checkPassword(null));
    }
    @Test
    void testCorrectPassword() throws InvalidPasswordException {
        UserModel user = new UserModel();
        user.setPassword("Password");
        Assertions.assertTrue(user.checkPassword("Password"));
        Assertions.assertFalse(user.checkPassword("else"));
    }
}
