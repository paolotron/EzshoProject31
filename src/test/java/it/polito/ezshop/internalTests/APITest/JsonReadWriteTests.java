package it.polito.ezshop.internalTests.APITest;

import it.polito.ezshop.data.Order;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.model.*;
import org.junit.*;


import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class JsonReadWriteTests {

    // CHANGE THIS VARIABLE TO AVOID DELETING THE RESULT OF THE TESTS
    private static final boolean persist = false;

    JsonRead read;
    JsonWrite write;

    @Before
    public void startIO() throws IOException {
        write = new JsonWrite("persistent");
        read = new JsonRead("persistent");
    }

    @After
    public void reset(){
        if(!persist)
            write.reset();
    }

    @Test
    public void productTest() throws IOException {
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
        assertTrue(write.writeProducts(l));
        assertArrayEquals(read.parseProductType().stream().map(ProductTypeModel::getId).toArray(), l.stream().map(ProductTypeModel::getId).toArray());
        assertArrayEquals(read.parseProductType().stream().map(ProductTypeModel::getProductDescription).toArray(), l.stream().map(ProductTypeModel::getProductDescription).toArray());
        assertArrayEquals(read.parseProductType().stream().map(ProductTypeModel::getBarCode).toArray(), l.stream().map(ProductTypeModel::getBarCode).toArray());
        assertArrayEquals(read.parseProductType().stream().map(ProductTypeModel::getLocation).toArray(), l.stream().map(ProductTypeModel::getLocation).toArray());
        assertArrayEquals(read.parseProductType().stream().map(ProductTypeModel::getNote).toArray(), l.stream().map(ProductTypeModel::getNote).toArray());
        assertArrayEquals(read.parseProductType().stream().map(ProductTypeModel::getPricePerUnit).toArray(), l.stream().map(ProductTypeModel::getPricePerUnit).toArray());
        assertArrayEquals(read.parseProductType().stream().map(ProductTypeModel::getQuantity).toArray(), l.stream().map(ProductTypeModel::getQuantity).toArray());
    }

    @Test
    public void usersTest() throws IOException, InvalidRoleException {
        write = new JsonWrite("persistent");
        read = new JsonRead("persistent");
        UserModel user1 = new UserModel("Paolo", "Rabs", "Administrator");
        UserModel user2 = new UserModel("Manuel", "man", "Administrator");
        UserModel user3 = new UserModel("Omar", "mar", "Administrator");
        UserModel user4 = new UserModel("Andrea", "and", "Administrator");
        ArrayList<UserModel> l = new ArrayList<>(Arrays.asList(user1, user3, user2, user4));
        assertTrue(write.writeUsers(l));
        assertArrayEquals(read.parseUsers().stream().map(UserModel::getUsername).toArray(), l.stream().map(UserModel::getUsername).toArray());
        assertArrayEquals(read.parseUsers().stream().map(UserModel::getRole).toArray(), l.stream().map(UserModel::getRole).toArray());
        assertArrayEquals(read.parseUsers().stream().map(UserModel::getPassword).toArray(), l.stream().map(UserModel::getPassword).toArray());
        assertArrayEquals(read.parseUsers().stream().map(UserModel::getId).toArray(), l.stream().map(UserModel::getId).toArray());
    }

    @Test
    public void customerTest() throws IOException {
        write = new JsonWrite("persistent");
        read = new JsonRead("persistent");

        CustomerModel c1 = new CustomerModel("Paulo", 2);
        CustomerModel c2 = new CustomerModel("Manuelo", 3);
        CustomerModel c3 = new CustomerModel("Omero", 4);
        CustomerModel c4 = new CustomerModel("Andro", 5);
        c1.setLoyaltyCard(new LoyaltyCardModel(1, 2));
        ArrayList<CustomerModel> l = new ArrayList<>(Arrays.asList(c1,c2,c3,c4));
        assertTrue(write.writeCustomers(l));
        assertArrayEquals(read.parseCustomers().stream().map(CustomerModel::getCustomerName).toArray(), l.stream().map(CustomerModel::getCustomerName).toArray());
        assertArrayEquals(read.parseCustomers().stream().map(CustomerModel::getId).toArray(), l.stream().map(CustomerModel::getId).toArray());
    }

    @Test
    public void balanceTest() throws IOException {
        write = new JsonWrite("persistent");
        read = new JsonRead("persistent");
        BalanceModel balance = new BalanceModel();
        OrderTransactionModel order = new OrderTransactionModel(new OrderModel("ABCD", 2, 2.), LocalDate.now());
        OrderTransactionModel order2 = new OrderTransactionModel(new OrderModel("ABCE", 2, 2.), LocalDate.now());
        OrderTransactionModel order3 = new OrderTransactionModel(new OrderModel("ABDC", 2, 2.), LocalDate.now());
        balance.addOrderTransaction(order);
        balance.addOrderTransaction(order2);
        balance.addOrderTransaction(order3);

        List<TicketEntryModel> tlist= new ArrayList<>();
        tlist.add(new TicketEntryModel("code123", "description", 2,2.));
        tlist.add(new TicketEntryModel("code423", "description2", 2,2.));
        TicketModel ticket = new TicketModel("NOT PAYED", 10., tlist);
        tlist.add(new TicketEntryModel("code223", "description3", 2,2.));
        SaleTransactionModel sale = new SaleTransactionModel( 10., LocalDate.now(), "CREDIT", LocalDate.now().toString(), ticket, 0);
        SaleTransactionModel sale2 = new SaleTransactionModel( 10., LocalDate.now(), "CASH", LocalDate.now().toString(), ticket, 0);
        balance.addSaleTransactionModel(sale.getBalanceId(), sale);
        balance.addSaleTransactionModel(sale.getBalanceId(), sale2);

        ReturnTransactionModel returnT = new ReturnTransactionModel(100., LocalDate.now(), ticket);
        returnT.getReturnedProductList().add(new TicketEntryModel("stringa", "stringa", 1, 1));
        balance.addReturnTransactionModel(returnT.getBalanceId(), returnT);

        write.writeBalance(balance);
        BalanceModel balance_read = read.parseBalance();

        assertEquals("compute balance error", balance_read.computeBalance(),balance.computeBalance(), 0.01);
        assertArrayEquals("return transactions saleId differ", balance.getReturnTransactionMap().values().stream().map(ReturnTransactionModel::getSaleId).toArray(), balance_read.getReturnTransactionMap().values().stream().map(ReturnTransactionModel::getSaleId).toArray());
        assertArrayEquals("return transactions amountToReturn differ", balance.getReturnTransactionMap().values().stream().map(ReturnTransactionModel::getAmountToReturn).toArray(), balance_read.getReturnTransactionMap().values().stream().map(ReturnTransactionModel::getAmountToReturn).toArray());
        assertArrayEquals("return transactions status differ", balance.getReturnTransactionMap().values().stream().map(ReturnTransactionModel::getStatus).toArray(), balance_read.getReturnTransactionMap().values().stream().map(ReturnTransactionModel::getStatus).toArray());
        assertArrayEquals("return transactions payment differ", balance.getReturnTransactionMap().values().stream().map(ReturnTransactionModel::getPayment).toArray(), balance_read.getReturnTransactionMap().values().stream().map(ReturnTransactionModel::getPayment).toArray());
        assertArrayEquals("return transactions BalanceId differ", balance.getReturnTransactionMap().values().stream().map(ReturnTransactionModel::getBalanceId).toArray(), balance_read.getReturnTransactionMap().values().stream().map(ReturnTransactionModel::getBalanceId).toArray());
        //assertArrayEquals("return transactions differ", balance.getReturnTransactionMap().values().stream().map(ReturnTransactionModel::getReturnedProductList).toArray(), balance_read.getReturnTransactionMap().values().stream().map(ReturnTransactionModel::getReturnedProductList).toArray());
        assertArrayEquals("sale transactions paymentType differ", balance.getSaleTransactionMap().values().stream().map(SaleTransactionModel::getPaymentType).toArray(), balance_read.getSaleTransactionMap().values().stream().map(SaleTransactionModel::getPaymentType).toArray());
        assertArrayEquals("sale transactions discountRate differ", balance.getSaleTransactionMap().values().stream().map(SaleTransactionModel::getDiscountRate).toArray(), balance_read.getSaleTransactionMap().values().stream().map(SaleTransactionModel::getDiscountRate).toArray());
        assertArrayEquals("sale transactions Ticket differ", balance.getSaleTransactionMap().values().stream().map(SaleTransactionModel::getTicket).map(TicketModel::getId).toArray(), balance_read.getSaleTransactionMap().values().stream().map(SaleTransactionModel::getTicket).map(TicketModel::getId).toArray());
        assertArrayEquals("order transactions balanceId differ", balance.getOrderTransactionMap().values().stream().map(OrderTransactionModel::getBalanceId).toArray(), balance_read.getOrderTransactionMap().values().stream().map(OrderTransactionModel::getBalanceId).toArray());
        assertArrayEquals("order transactions money differ", balance.getOrderTransactionMap().values().stream().map(OrderTransactionModel::getMoney).toArray(), balance_read.getOrderTransactionMap().values().stream().map(OrderTransactionModel::getMoney).toArray());
        assertArrayEquals("order transactions type differ", balance.getOrderTransactionMap().values().stream().map(OrderTransactionModel::getType).toArray(), balance_read.getOrderTransactionMap().values().stream().map(OrderTransactionModel::getType).toArray());
    }

}
