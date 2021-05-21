package it.polito.ezshop.internalTests.APITest;

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
        o.setLocation("qui");
        o.setQuantity(100);
        ProductTypeModel o2 = new ProductTypeModel(2,"MockDescription2", "MockCode2", 3.0, "MockNote2");
        o.setLocation("li");
        o.setQuantity(200);
        ProductTypeModel o3 = new ProductTypeModel(3,"MockDescription3", "MockCode3", 4.0, "MockNote3");
        o.setLocation("la");
        o.setBarCode("123123124");
        ProductTypeModel o4 = new ProductTypeModel(4,"MockDescription4", "MockCode4", 5.0, "MockNote4");
        o.setBarCode("123123126");
        List<ProductTypeModel> l = new ArrayList<>(Arrays.asList(o,o2,o3,o4));
        Assertions.assertTrue(write.writeProducts(l));
        Assertions.assertArrayEquals(read.parseProductType().stream().map(ProductTypeModel::getId).toArray(), l.stream().map(ProductTypeModel::getId).toArray());
        Assertions.assertArrayEquals(read.parseProductType().stream().map(ProductTypeModel::getProductDescription).toArray(), l.stream().map(ProductTypeModel::getProductDescription).toArray());
        Assertions.assertArrayEquals(read.parseProductType().stream().map(ProductTypeModel::getBarCode).toArray(), l.stream().map(ProductTypeModel::getBarCode).toArray());
        Assertions.assertArrayEquals(read.parseProductType().stream().map(ProductTypeModel::getLocation).toArray(), l.stream().map(ProductTypeModel::getLocation).toArray());
        Assertions.assertArrayEquals(read.parseProductType().stream().map(ProductTypeModel::getNote).toArray(), l.stream().map(ProductTypeModel::getNote).toArray());
        Assertions.assertArrayEquals(read.parseProductType().stream().map(ProductTypeModel::getPricePerUnit).toArray(), l.stream().map(ProductTypeModel::getPricePerUnit).toArray());
        Assertions.assertArrayEquals(read.parseProductType().stream().map(ProductTypeModel::getQuantity).toArray(), l.stream().map(ProductTypeModel::getQuantity).toArray());
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
        Assertions.assertArrayEquals(read.parseUsers().stream().map(UserModel::getPassword).toArray(), l.stream().map(UserModel::getPassword).toArray());
        Assertions.assertArrayEquals(read.parseUsers().stream().map(UserModel::getId).toArray(), l.stream().map(UserModel::getId).toArray());
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
        Assertions.assertArrayEquals(read.parseCustomers().stream().map(CustomerModel::getId).toArray(), l.stream().map(CustomerModel::getId).toArray());
    }

    @Test
    void balanceTest() throws IOException {
        write = new JsonWrite("persistent");
        read = new JsonRead("persistent");
        BalanceModel balance = new BalanceModel();
        OrderTransactionModel order = new OrderTransactionModel(new OrderModel("ABCD", 2, 2.), LocalDate.now());
        balance.getOrderTransactionMap().put(order.getBalanceId(), order);
        List<TicketEntryModel> tlist= new ArrayList<>();
        tlist.add(new TicketEntryModel("code123", "description", 2,2.));
        TicketModel ticket = new TicketModel("NOT PAYED", 10., tlist);
        SaleTransactionModel sale = new SaleTransactionModel( 10., LocalDate.now(), "CREDIT", LocalDate.now().toString(), ticket, 0);
        balance.getSaleTransactionMap().put(sale.getBalanceId(),sale);
        ReturnTransactionModel returnT = new ReturnTransactionModel(100., LocalDate.now(), ticket);
        returnT.getReturnedProductList().add(new TicketEntryModel("stringa", "stringa", 1, 1));
        balance.getReturnTransactionMap().put(returnT.getBalanceId(), returnT);
        write.writeBalance(balance);
        BalanceModel balance_read = read.parseBalance();
        Assertions.assertEquals(balance_read.computeBalance(),balance.computeBalance());
        Assertions.assertArrayEquals(balance.getReturnTransactionMap().values().stream().map(ReturnTransactionModel::getSaleId).toArray(), balance_read.getReturnTransactionMap().values().stream().map(ReturnTransactionModel::getSaleId).toArray(), "return transactions differ");
        Assertions.assertArrayEquals(balance.getSaleTransactionMap().values().toArray(), balance_read.getSaleTransactionMap().values().toArray(), "sale transactions differ");
        Assertions.assertArrayEquals(balance.getOrderTransactionMap().values().toArray(), balance_read.getOrderTransactionMap().values().toArray(), "order transactions differ");
    }

}
