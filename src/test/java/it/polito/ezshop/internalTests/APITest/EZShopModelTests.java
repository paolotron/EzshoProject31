package it.polito.ezshop.internalTests.APITest;

import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.EzShopModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class EZShopModelTests {

    EzShopModel model = new EzShopModel();

    @Before
    public void start(){
        EzShopModel model = new EzShopModel();
        model.reset();
    }

    @After
    public void end(){
        model.reset();
    }

    @Test
    public void ModelTest() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidUserIdException, UnauthorizedException {

        int id;
        id = model.createUser("Paolo", "Rabs", "Administrator").getId();
        model = new EzShopModel();
        model.loadEZShop();
        model.login("Paolo", "Rabs");
        assertEquals(model.getUserById(id).getUsername(),"Paolo");
    }
}
