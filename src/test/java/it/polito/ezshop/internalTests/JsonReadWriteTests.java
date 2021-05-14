package it.polito.ezshop.internalTests;

import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonReadWriteTests {

    // CHANGE THIS VARIABLE TO AVOID DELETING THE RESULT OF THE TESTS
    private static final boolean persist = false;

    JsonRead read;
    JsonWrite write;

    @BeforeEach
    void startIO() throws IOException {
        write = new JsonWrite("persistent");
        read = new JsonRead("persistent");
    }

    @AfterEach
    void reset(){
        if(!persist)
            write.reset();
    }

    @Test
    void productTest() throws IOException {
        write = new JsonWrite("persistent");
        read = new JsonRead("persistent");
        ProductTypeModel o = new ProductTypeModel(1,"MockDescription", "MockCode", 2.0, "MockNote");
        ProductTypeModel o2 = new ProductTypeModel(1,"MockDescription2", "MockCode2", 3.0, "MockNote2");
        List<ProductTypeModel> l = new ArrayList<>();
        l.add(o);
        l.add(o2);
        write.writeProducts(l);
        Assertions.assertEquals(read.parseProductType().get(0).getProductDescription(), o.getProductDescription());
        Assertions.assertEquals(read.parseProductType().get(1).getProductDescription(), o2.getProductDescription());
    }

    @Test
    void usersTest() throws IOException, InvalidRoleException {
        write = new JsonWrite("persistent");
        read = new JsonRead("persistent");
        UserModel user1 = new UserModel("Paolo", "Rabs", "Administrator");
        UserModel user2 = new UserModel("Manuel", "man", "Administrator");
        UserModel user3 = new UserModel("Omar", "mar", "Administrator");
        UserModel user4 = new UserModel("Andrea", "and", "Administrator");
        ArrayList<UserModel> l = new ArrayList<>(Arrays.asList(user1, user3, user2, user4));
        Assertions.assertTrue(write.writeUsers(l));
        Assertions.assertArrayEquals(read.parseUsers().stream().map(UserModel::getUsername).toArray(), l.stream().map(UserModel::getUsername).toArray());
        Assertions.assertArrayEquals(read.parseUsers().stream().map(UserModel::getRole).toArray(), l.stream().map(UserModel::getRole).toArray());
    }

    @Test
    void customerTest() throws IOException {
        write = new JsonWrite("persistent");
        read = new JsonRead("persistent");

        CustomerModel c1 = new CustomerModel("Paulo", 2);
        CustomerModel c2 = new CustomerModel("Manuelo", 3);
        CustomerModel c3 = new CustomerModel("Omero", 4);
        CustomerModel c4 = new CustomerModel("Andro", 5);
        c1.setLoyalityCard(new LoyaltyCardModel(1, 2));
        ArrayList<CustomerModel> l = new ArrayList<>(Arrays.asList(c1,c2,c3,c4));
        Assertions.assertTrue(write.writeCustomers(l));
        Assertions.assertArrayEquals(read.parseCustomers().stream().map(CustomerModel::getCustomerName).toArray(), l.stream().map(CustomerModel::getCustomerName).toArray());
        Assertions.assertArrayEquals(read.parseCustomers().stream().map(CustomerModel::getCustomerName).toArray(), l.stream().map(CustomerModel::getCustomerName).toArray());
    }

    @Test
    void balanceTest() throws IOException {
        write = new JsonWrite("persistent");
        read = new JsonRead("persistent");
        BalanceModel balance = new BalanceModel();
        OrderTransactionModel order = new OrderTransactionModel(new OrderModel("ABCD", 2, 2.), LocalDate.now());
        balance.getOrderTransactionMap().put(order.getBalanceId(), order);
        List<TicketEntryModel> tlist= new ArrayList<>();
        tlist.add(new TicketEntryModel("code123", "description", 2,2.,0));
        Ticket ticket = new Ticket("NOT PAYED", 10., tlist);
        SaleTransactionModel sale = new SaleTransactionModel( 10., LocalDate.now(), "CREDIT", LocalDate.now().toString(), ticket, 0);
        balance.getSaleTransactionMap().put(sale.getBalanceId(),sale);
        ReturnTransactionModel returnT = new ReturnTransactionModel(100., LocalDate.now(), ticket);
        returnT.getReturnedProductList().add(new TicketEntryModel("stringa", "stringa", 1, 1,2.));
        balance.getReturnTransactionMap().put(returnT.getBalanceId(), returnT);
        write.writeBalance(balance);
        BalanceModel balance_read = read.parseBalance();
        Assertions.assertEquals(balance_read.getBalanceAmount(),balance.getBalanceAmount());
    }

}
