package it.polito.ezshop.internalTests;

import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PersistenceTests {


    @Test
    void productTest() throws IOException {
        JsonWrite j = new JsonWrite("persistent");
        JsonRead r = new JsonRead("persistent");
        ProductTypeModel o = new ProductTypeModel(1,"MockDescription", "MockCode", 2.0, "MockNote");
        ProductTypeModel o2 = new ProductTypeModel(1,"MockDescription2", "MockCode2", 3.0, "MockNote2");
        List<ProductTypeModel> l = new ArrayList<>();
        l.add(o);
        l.add(o2);
        j.enableWrite();
        j.writeProducts(l);
        j.disableWrite();
        Assertions.assertEquals(r.parseProductType().get(0).getProductDescription(), o.getProductDescription());
        Assertions.assertEquals(r.parseProductType().get(1).getProductDescription(), o2.getProductDescription());
    }

    @Test
    void usersTest() throws IOException, InvalidRoleException {
        JsonWrite write = new JsonWrite("persistent");
        JsonRead read = new JsonRead("persistent");

        UserModel user1 = new UserModel("Paolo", "Rabs", "Administrator");
        UserModel user2 = new UserModel("Manuel", "man", "Administrator");
        UserModel user3 = new UserModel("Omar", "mar", "Administrator");
        UserModel user4 = new UserModel("Andrea", "and", "Administrator");
        ArrayList<UserModel> l = new ArrayList<>(Arrays.asList(user1, user3, user2, user4));
        write.enableWrite();
        Assertions.assertTrue(write.writeUsers(l));
        write.disableWrite();
        Assertions.assertArrayEquals(read.parseUsers().stream().map(UserModel::getUsername).toArray(), l.stream().map(UserModel::getUsername).toArray());
        Assertions.assertArrayEquals(read.parseUsers().stream().map(UserModel::getRole).toArray(), l.stream().map(UserModel::getRole).toArray());
    }

    @Test
    void customerTest() throws IOException {
        JsonWrite write = new JsonWrite("persistent");
        JsonRead read = new JsonRead("persistent");

        CustomerModel c1 = new CustomerModel("Paulo", 2);
        CustomerModel c2 = new CustomerModel("Manuelo", 3);
        CustomerModel c3 = new CustomerModel("Omero", 4);
        CustomerModel c4 = new CustomerModel("Andro", 5);
        c1.setLoyalityCard(new LoyalityCard("1", 2));
        ArrayList<CustomerModel> l = new ArrayList<>(Arrays.asList(c1,c2,c3,c4));
        write.enableWrite();
        Assertions.assertTrue(write.writeCustomers(l));
        write.disableWrite();
        Assertions.assertArrayEquals(read.parseCustomers().stream().map(CustomerModel::getCustomerName).toArray(), l.stream().map(CustomerModel::getCustomerName).toArray());
        Assertions.assertArrayEquals(read.parseCustomers().stream().map(CustomerModel::getCustomerName).toArray(), l.stream().map(CustomerModel::getCustomerName).toArray());
    }
}
