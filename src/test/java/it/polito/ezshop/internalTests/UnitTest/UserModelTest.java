package it.polito.ezshop.internalTests.UnitTest;

import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.model.UserModel;
import org.junit.Test;

import static org.junit.Assert.*;


public class UserModelTest {
    @Test
    public void testConstructor() throws InvalidRoleException {
        new UserModel("Paolo", "Password", "Cashier");
        new UserModel("Paolo", "Password", "Administrator");
        new UserModel("Paolo", "Password", "ShopManager");
        assertThrows(InvalidRoleException.class, ()->new UserModel("Paolo", "Password", "sopManag"));
    }
    @Test
    public void testInvalidPassword(){
        UserModel user = new UserModel();
        user.setPassword("Password");
        assertThrows(InvalidPasswordException.class,()->user.checkPassword(""));
        assertThrows(InvalidPasswordException.class,()->user.checkPassword(null));
    }
    @Test
    public void testCorrectPassword() throws InvalidPasswordException {
        UserModel user = new UserModel();
        user.setPassword("Password");
        assertTrue(user.checkPassword("Password"));
        assertFalse(user.checkPassword("else"));
    }
}
