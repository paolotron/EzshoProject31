package it.polito.ezshop.internalTests.APITest;

import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.EzShopModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EZShopModelTests {

    EzShopModel model = new EzShopModel();

    @BeforeEach
    void start(){
        EzShopModel model = new EzShopModel();
        model.reset();
    }

    @AfterEach
    void end(){
        model.reset();
    }

    @Test
    void ModelTest() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidUserIdException, UnauthorizedException {

        int id;
        id = model.createUser("Paolo", "Rabs", "Administrator").getId();
        model = new EzShopModel();
        model.loadEZShop();
        model.login("Paolo", "Rabs");
        Assertions.assertEquals(model.getUserById(id).getUsername(),"Paolo");
    }
}
