package it.polito.ezshop.internalTests.ScenarioTest;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.data.*;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.CreditCardPaymentModel;
import it.polito.ezshop.model.LoyaltyCardModel;
import it.polito.ezshop.model.ProductTypeModel;
import org.junit.*;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.Assert.*;

public class ScenarioTest {
    //EZShopInterface model;
    EZShop data;
    String username = "Paolo";
    String password = "pass";
    String barcode = "6291041500213";
    String creditCard = "5265807692";
    int productTypeId;
    int userId;
    int orderId;
    int customerId;
    Integer startingQuantity;
    Double startingBalance;
    Double startingBalanceCreditCard= 1000.00;


    void login() throws InvalidPasswordException, InvalidUsernameException {
        data.login(username, password);
    }

    @BeforeClass
    public static void CreditCardFile() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("PaymentGateway/cards.txt"));
        writer.write("#Comment\n#Comment\n5265807692;1000");
        writer.close();
    }

    @AfterClass
    public static void cleanupFile() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("PaymentGateway/cards.txt"));
        writer.close();
    }

    @Before
    public void startEzShop() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidProductIdException, InvalidLocationException, InvalidQuantityException, InvalidCustomerNameException {
        data = new it.polito.ezshop.data.EZShop();
        data.reset();
        data.createUser(username, password, "Administrator");
        login();
        //precondition usecase1
        productTypeId = data.createProductType("test product", barcode , 10, "");
        //precondition usecase2
        userId = data.createUser("precond_user", "passwd", "Cashier");
        data.recordBalanceUpdate(20000);
        startingBalance = data.computeBalance();
        startingQuantity = data.getProductTypeByBarCode(barcode).getQuantity();
        orderId = data.issueOrder(barcode, 10, 1.);
        data.updatePosition(productTypeId, "23-ABC-2");
        customerId = data.defineCustomer("precondCustomer");
        //model.updateQuantity(id, 10);
        //login();
        //model.logout();
    }
/*
    @Test
    public void testCompleteTransaction() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException, InvalidCreditCardException {
        login();
        Integer id = model.startSaleTransaction();
        assertTrue(model.addProductToSale(id, "6291041500213", 2));
        assertEquals(8, model.getProductTypeByBarCode(barcode).getQuantity(), 0.01);
        assertTrue(model.endSaleTransaction(id));
        assertEquals(0.,model.computeBalance(), 0.01);
        assertTrue(model.receiveCreditCardPayment(id, creditCard));
        assertEquals(20,model.computeBalance(), 0.01);
        assertEquals(2, model.computePointsForSale(id));
    }

    @Test
    public void testCompleteReturn() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException, InvalidCreditCardException {
        login();
        Integer id = model.startSaleTransaction();
        assertTrue(model.addProductToSale(id, barcode, 2));
        assertEquals(8, model.getProductTypeByBarCode(barcode).getQuantity(), 0.01);
        model.endSaleTransaction(id);
        assertEquals(0.,model.computeBalance(), 0.01);
        assertTrue(model.receiveCreditCardPayment(id, creditCard));
        assertEquals(20, model.computeBalance(), 0.01);
        Integer rId = model.startReturnTransaction(id);
        model.returnProduct(rId, barcode, 1);
        assertEquals(8, model.getProductTypeByBarCode(barcode).getQuantity(), 0.01);
        assertTrue(model.endReturnTransaction(rId, true));
        assertEquals(9, model.getProductTypeByBarCode(barcode).getQuantity(), 0.01);
        assertEquals(10, model.returnCreditCardPayment(rId, creditCard), 0.01);
        assertEquals(10, model.computeBalance(), 0.01);
    }

    @Test
    public void testCompleteOrder() throws InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, UnauthorizedException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidOrderIdException {
        login();
        int id = model.issueOrder(barcode, 2, 2.);
        assertEquals(id, -1);
        model.recordBalanceUpdate(100);
        id = model.issueOrder(barcode, 2, 20.);
        assertEquals("ISSUED", model.getAllOrders().get(0).getStatus());
        assertTrue(model.payOrder(id));
        assertEquals("PAYED", model.getAllOrders().get(0).getStatus());
        assertTrue(model.recordOrderArrival(id));
        assertEquals("COMPLETED", model.getAllOrders().get(0).getStatus());
        assertEquals(60, model.computeBalance(), 0.01);
        id = model.payOrderFor(barcode, 2, 10);
        assertEquals("PAYED", model.getAllOrders().get(1).getStatus());
        assertTrue(model.recordOrderArrival(id));
        assertEquals(model.computeBalance(), 40, 0.01);
        assertEquals("COMPLETED", model.getAllOrders().get(1).getStatus());
    }
*/
    @Test
    public void scenario1_1() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException {
        int id = data.createProductType("desc", barcode, 10., "notes");
        assertTrue(id>0);
        assertTrue(data.updatePosition(id, "1-a-1"));
        assertNotNull("post condition not verified", data.getProductTypeByBarCode(barcode));
    }

    @Test
    public void scenario1_2_3() throws InvalidLocationException, UnauthorizedException, InvalidProductIdException, InvalidProductCodeException, InvalidProductDescriptionException, InvalidPricePerUnitException {
        Double newPrice = 20.;
        String location = "2-a-2";
        ProductType p = data.getProductTypeByBarCode(barcode);
        assertTrue(data.updatePosition(p.getId(), location));
        assertEquals("post condition 1_2 not verified", location, p.getLocation());
        assertTrue(data.updateProduct(p.getId(), p.getProductDescription(), p.getBarCode(), newPrice, p.getNote()));
        assertEquals("post condition 1_3 not verified", newPrice, p.getPricePerUnit());
    }

    @Test
    public void scenario2_1_3() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidUserIdException, UnauthorizedException {
        int id = data.createUser("user", "pass", "Cashier");
        assertTrue(id > 0);
        assertNotNull("post condition 2_1 not verified", data.getUser(id));
        assertTrue(data.deleteUser(id));
        assertNull("post condition 2_3 not verified", data.getUser(id));
    }

    @Test
    public void scenario2_2() throws InvalidUserIdException, UnauthorizedException, InvalidRoleException {
        String newRole = "ShopManager";
        assertTrue(data.updateUserRights(userId, newRole));
        assertEquals("post condition not verified", newRole, data.getUser(userId).getRole());
    }

    @Test
    public void scenario3_1() throws InvalidQuantityException, UnauthorizedException, InvalidPricePerUnitException, InvalidProductCodeException {
        int quantity = 10;
        double pricePerUnit = 2.;
        int id = data.issueOrder(barcode, quantity, pricePerUnit);
        assertTrue(id > 0);
        assertEquals("post condition not verified", "ISSUED", data.getAllOrders().get(0).getStatus());
    }

    @Test
    public void scenario3_2() throws UnauthorizedException, InvalidOrderIdException, InvalidProductCodeException {
        assertTrue(data.payOrder(orderId)); //all scenario's steps are done by payOrder
        assertEquals("post condition not verified", "PAYED", data.getAllOrders().get(0).getStatus());
        assertEquals("post condition not verified", startingBalance - 10, data.computeBalance(), 0.);
        assertEquals("post condition not verified", startingQuantity, data.getProductTypeByBarCode(barcode).getQuantity());
    }

    @Test
    public void scenario3_3() throws InvalidLocationException, UnauthorizedException, InvalidOrderIdException, InvalidProductCodeException {
        data.payOrder(orderId); //doing here the precondition
        assertTrue(data.recordOrderArrival(orderId));
        Integer newQuantity = startingQuantity + 10;
        assertEquals("post condition not verified", "COMPLETED", data.getAllOrders().get(0).getStatus());
        assertEquals("post condition not verified", newQuantity , data.getProductTypeByBarCode(barcode).getQuantity());
    }

    @Test
    public void scenario4_1_4() throws InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException, InvalidCustomerCardException {
        int id = data.defineCustomer("customer");
        assertTrue(id > 0);
        assertNotNull("post condition 4_1 not verified", data.getCustomer(id));

        assertTrue(data.modifyCustomer(id, "newName", null));
        assertEquals("post condition 4_4 not verified", "newName", data.getCustomer(id).getCustomerName());
        assertNull("post condition 4_4 not verified", data.getCustomer(id).getCustomerCard());
    }

    @Test
    public void scenario4_2_3() throws UnauthorizedException, InvalidCustomerIdException, InvalidCustomerCardException, InvalidCustomerNameException {
        String card = data.createCard();
        assertNotNull(card);
        assertTrue(data.attachCardToCustomer(card, customerId));
        assertEquals("post condition 4_2 not verified", card, data.getCustomer(customerId).getCustomerCard());
        data.modifyCustomer(customerId, data.getCustomer(customerId).getCustomerName(), "");
        assertNull("post condition 4_4 not verified", data.getCustomer(customerId).getCustomerCard());
    }

    @Test
    public void scenario5_1() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
        //precond
        Integer id = data.createUser("Omar", password, "Cashier");

        User u = data.login("Omar", password);

        //postcond
        assertEquals(id, u.getId());

    }

    @Test
    public void scenario5_2() throws InvalidPasswordException, InvalidUsernameException {
        //precond
        data.login(username, password);

        assertTrue(data.logout());
    }

    @Test
    public void scenario6_1() throws InvalidPasswordException, InvalidUsernameException, InvalidRoleException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductIdException, InvalidCreditCardException {
        //precond
        double pricePerUnit = 0.50;
        int initialQuantity = 100;
        data.createUser("Admin", password, "Administrator");
        data.login("Admin", password); //login with Administrator account to create a new product
        assertTrue(data.updateProduct(productTypeId,"desc", barcode, pricePerUnit, "note"));
        assertTrue(data.updateQuantity(productTypeId,initialQuantity));
        data.logout();
        data.createUser("Omar", password, "Cashier");
        data.login("Omar", password);

        int N = 4;
        Integer transactionID = data.startSaleTransaction();
        assertTrue(transactionID>0);
        assertTrue(data.addProductToSale(transactionID, barcode, N));
        assertTrue(data.endSaleTransaction(transactionID));
        assertTrue(data.receiveCreditCardPayment(transactionID, creditCard));

        //postcond
        data.logout();
        data.login("Admin", password);
        assertEquals((startingBalance+N*pricePerUnit),data.computeBalance(),0.01);
    }

       //TODO error in applyDiscountToProduct
    @Test
    public void scenario6_2() throws InvalidPasswordException, InvalidUsernameException, InvalidRoleException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductIdException, InvalidDiscountRateException, InvalidCreditCardException, InvalidPaymentException {
        //precond
        Double pricePerUnit = 1.20;
        Integer initialQuantity = 100;
        data.createUser("Admin", password, "Administrator");
        data.createUser("Omar", password, "Cashier");
        User A = data.login("Admin", password); //login with Administrator account to create a new product
        assertTrue(data.updateProduct(productTypeId,"desc", barcode, pricePerUnit, "note"));
        assertTrue(data.updateQuantity(productTypeId,initialQuantity));
        data.logout();
        data.login("Omar", password);

        Integer N = 10;
        Double productDiscount = 0.50;
        Integer transactionID = data.startSaleTransaction();
        assertTrue(transactionID>0);
        assertTrue(data.addProductToSale(transactionID, barcode, N));
        assertTrue(data.applyDiscountRateToProduct(transactionID,barcode,productDiscount));
        assertTrue(data.endSaleTransaction(transactionID));
        assertTrue(data.receiveCreditCardPayment(transactionID, creditCard));

        //postcond
        data.logout();
        data.login("Admin", password);
        assertEquals((startingBalance + (N*pricePerUnit - N*pricePerUnit*productDiscount)),data.computeBalance(),0.01);
    }

    @Test
    public void scenario6_3() throws InvalidPasswordException, InvalidUsernameException, InvalidRoleException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductIdException, InvalidDiscountRateException, InvalidCreditCardException {
        //precond
        Double pricePerUnit = 2.20;
        int initialQuantity = 100;
        data.createUser("Admin", password, "Administrator");
        data.login("Admin", password); //login with Administrator account to create a new product
        assertTrue(data.updateProduct(productTypeId,"desc", barcode, pricePerUnit, "note"));
        assertTrue(data.updateQuantity(productTypeId,initialQuantity));
        data.logout();
        data.createUser("Omar", password, "Cashier");
        data.login("Omar", password);

        Integer N = 10;
        double saleDiscount = 0.98;
        Integer transactionID = data.startSaleTransaction();
        assertTrue(transactionID>0);
        assertTrue(data.addProductToSale(transactionID, barcode, N));
        assertTrue(data.applyDiscountRateToSale(transactionID,saleDiscount));
        assertTrue(data.endSaleTransaction(transactionID));
        assertTrue(data.receiveCreditCardPayment(transactionID, creditCard));

        //postcond
        data.logout();
        data.login("Admin", password);
        assertEquals((startingBalance + (N*pricePerUnit - N*pricePerUnit*saleDiscount)),data.computeBalance(),0.01);
    }

    @Test
    public void scenario6_4() throws InvalidPasswordException, InvalidUsernameException, InvalidRoleException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductIdException, InvalidCreditCardException, InvalidCustomerCardException, InvalidCustomerNameException, InvalidCustomerIdException {
        //precond
        Double pricePerUnit = 2.20;
        int initialPoints = 10;
        int initialQuantity = 100;
        data.createUser("Admin", password, "Administrator");
        data.login("Admin", password); //login with Administrator account to create a new product
        assertTrue(data.updateProduct(productTypeId,"desc", barcode, pricePerUnit, "note"));
        assertTrue(data.updateQuantity(productTypeId,initialQuantity));
        String L = data.createCard();
        Integer customerID = data.defineCustomer("newCustomer");
        assertTrue(customerID>0);
        assertTrue(data.attachCardToCustomer(L,customerID));
        assertTrue(data.modifyPointsOnCard(L,initialPoints));
        data.logout();
        data.createUser("Omar", password, "Cashier");
        data.login("Omar", password);

        Integer N = 10;
        Integer transactionID = data.startSaleTransaction();
        assertTrue(transactionID>0);
        assertTrue(data.addProductToSale(transactionID, barcode, N));
        assertTrue(data.endSaleTransaction(transactionID));
        assertTrue(data.receiveCreditCardPayment(transactionID, creditCard));
        assertTrue(data.modifyPointsOnCard(L, data.computePointsForSale(transactionID)));

        //postcond
        data.logout();
        data.login("Admin", password);
        assertEquals(startingBalance + N*pricePerUnit,data.computeBalance(),0.01);
        ProductType p = data.getProductTypeByBarCode(barcode);
        assertEquals((initialQuantity-N),p.getQuantity(),0.01);
        assertEquals(((int) (initialPoints + (N * pricePerUnit / 10))),data.model.getCustomerById(customerID).getPoints(),0.01);
    }

    @Test
    public void scenario6_5() throws InvalidPasswordException, InvalidUsernameException, InvalidRoleException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductIdException,  InvalidCreditCardException {
        //precond
        double pricePerUnit = 2.20;
        int initialQuantity = 100;
        data.createUser("Admin", password, "Administrator");
        data.login("Admin", password); //login with Administrator account to create a new product
        assertTrue(data.updateProduct(productTypeId,"desc", barcode, pricePerUnit, "note"));
        assertTrue(data.updateQuantity(productTypeId,initialQuantity));
        data.logout();
        data.createUser("Omar", password, "Cashier");
        data.login("Omar", password);

        int N = 10;
        Integer transactionID = data.startSaleTransaction();
        assertTrue(transactionID>0);
        assertTrue(data.addProductToSale(transactionID, barcode, N));
        assertTrue(data.endSaleTransaction(transactionID));
        assertThrows(InvalidCreditCardException.class, ()->data.receiveCreditCardPayment(transactionID, ""));
        assertTrue(data.deleteSaleTransaction(transactionID));


        //postcond
        data.logout();
        data.login("Admin", password);
        assertEquals(startingBalance ,data.computeBalance(),0.01);
        ProductType p = data.getProductTypeByBarCode(barcode);
        assertEquals(initialQuantity,p.getQuantity(),0.01);

    }

    @Test
    public void scenario6_6() throws InvalidPasswordException, InvalidUsernameException, InvalidRoleException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductIdException, InvalidLocationException, InvalidDiscountRateException, InvalidCreditCardException, InvalidCustomerCardException, InvalidCustomerNameException, InvalidCustomerIdException, InvalidPaymentException {
        //precond
        double pricePerUnit = 2.20;
        int initialQuantity = 100;
        data.createUser("Admin", password, "Administrator");
        data.login("Admin", password); //login with Administrator account to create a new product
        assertTrue(data.updateProduct(productTypeId,"desc", barcode, pricePerUnit, "note"));
        assertTrue(data.updateQuantity(productTypeId,initialQuantity));
        data.logout();
        data.createUser("Omar", password, "Cashier");
        data.login("Omar", password);

        int N = 9;
        Integer transactionID = data.startSaleTransaction();
        assertTrue(transactionID>0);
        assertTrue(data.addProductToSale(transactionID, barcode, N));
        assertTrue(data.endSaleTransaction(transactionID));
        double change = data.receiveCashPayment(transactionID,20.0);
        assertTrue(change>0);

        //postcond
        data.logout();
        data.login("Admin", password);
        assertEquals((startingBalance+N*pricePerUnit) ,data.computeBalance(),0.01);
        ProductType p = data.getProductTypeByBarCode(barcode);
        assertEquals((initialQuantity-N),p.getQuantity(),0.01);

    }

    @Test
    public void scenario7_1() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidCreditCardException, InvalidTransactionIdException, InvalidQuantityException, InvalidProductCodeException, InvalidProductIdException {
        //precond
        data.login(username, password);  //ADMINISTRATOR
        Integer N = 10;
        Integer transactionID = data.startSaleTransaction();
        assertTrue(transactionID>0);
        assertTrue(data.updateQuantity(productTypeId,100));
        assertTrue(data.addProductToSale(transactionID, barcode, N));
        assertTrue(data.endSaleTransaction(transactionID));

        assertTrue(data.receiveCreditCardPayment(transactionID, creditCard));

    }

    @Test
    public void scenario7_2() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidTransactionIdException, InvalidQuantityException, InvalidProductCodeException, InvalidProductIdException {
        //precond
        String fakeCard = "5265807688"; //doesn't exist
        data.login(username, password);  //ADMINISTRATOR
        Integer N = 10;
        Integer transactionID = data.startSaleTransaction();
        assertTrue(transactionID>0);
        assertTrue(data.updateQuantity(productTypeId,100));
        assertTrue(data.addProductToSale(transactionID, barcode, N));
        assertTrue(data.endSaleTransaction(transactionID));

        assertThrows(InvalidCreditCardException.class,()->data.receiveCreditCardPayment(transactionID, fakeCard));
    }

    @Test
    public void scenario7_3() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidCreditCardException, InvalidTransactionIdException, InvalidQuantityException, InvalidProductCodeException, InvalidProductIdException, InvalidProductDescriptionException, InvalidPricePerUnitException {
        //precond
        data.login(username, password);  //ADMINISTRATOR
        Integer N = 100;
        assertTrue(data.updateQuantity(productTypeId,200));
        assertTrue(data.updateProduct(productTypeId,"newDescr",barcode,990.00,"newNote"));
        Integer transactionID = data.startSaleTransaction();
        assertTrue(transactionID>0);
        assertTrue(data.addProductToSale(transactionID, barcode, N));
        assertTrue(data.endSaleTransaction(transactionID));

        assertFalse(data.receiveCreditCardPayment(transactionID,creditCard));
        assertEquals(startingBalance,data.computeBalance(),0.01);
    }

    @Test
    public void scenario7_4() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidTransactionIdException, InvalidQuantityException, InvalidProductCodeException, InvalidProductIdException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidPaymentException {
        //precond
        data.login(username, password);  //ADMINISTRATOR
        int N = 10;
        double pricePerUnit = 2.90;
        double cash = 50.0;
        assertTrue(data.updateProduct(productTypeId,"newDescr",barcode,pricePerUnit,"newNote"));
        assertTrue(data.updateQuantity(productTypeId,100));
        Integer transactionID = data.startSaleTransaction();
        assertTrue(transactionID>0);
        assertTrue(data.addProductToSale(transactionID, barcode, N));
        assertTrue(data.endSaleTransaction(transactionID));

        double change = data.receiveCashPayment(transactionID, cash);
        assertEquals((50-N*pricePerUnit),change,0.01);
    }

    // Scenario 9-1 is covered by APITest/FR8/correctShowCreditsAndDebits

    @Test
    public void sc8_1_sc10_1() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, UnauthorizedException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException, InvalidPaymentException, InvalidProductIdException, InvalidCreditCardException {
        //Precondition
        String username = "Cashier";
        String password = "pass";
        data.createUser(username, password, "Cashier");
        login();
        data.updateQuantity(data.getProductTypeByBarCode(barcode).getId(), 10);
        data.login(username, password);

        Integer saleId = data.startSaleTransaction();
        assertTrue(data.addProductToSale(saleId, barcode, 3));
        assertTrue(data.endSaleTransaction(saleId));
        assertEquals(10, data.receiveCashPayment(saleId, 40), 0.01);
        login();
        assertEquals(startingBalance+ 30,data.computeBalance(), 0.01);
        data.login(username, password);
        Integer returnId = data.startReturnTransaction(saleId);
        login();
        Integer oldQuantity = data.getProductTypeByBarCode(barcode).getQuantity();
        assertTrue(data.returnProduct(returnId, barcode, 2));
        assertEquals(oldQuantity, data.getProductTypeByBarCode(barcode).getQuantity(), 0);
        data.endReturnTransaction(returnId, true);
        assertEquals(oldQuantity+2, data.getProductTypeByBarCode(barcode).getQuantity(), 0);
        assertEquals(startingBalance+30, data.computeBalance(), 0);
        data.returnCreditCardPayment(returnId, creditCard);
        assertEquals(startingBalance+10, data.computeBalance(), 0);
    }

    @Test
    public void sc8_2sc10_2() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, UnauthorizedException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException, InvalidPaymentException, InvalidProductIdException {
        //Precondition
        String username = "Cashier";
        String password = "pass";
        data.createUser(username, password, "Cashier");
        login();
        data.updateQuantity(data.getProductTypeByBarCode(barcode).getId(), 10);
        data.login(username, password);

        Integer saleId = data.startSaleTransaction();
        assertTrue(data.addProductToSale(saleId, barcode, 3));
        assertTrue(data.endSaleTransaction(saleId));
        assertEquals(10, data.receiveCashPayment(saleId, 40), 0.01);
        login();
        assertEquals(startingBalance+ 30,data.computeBalance(), 0.01);
        data.login(username, password);
        Integer returnId = data.startReturnTransaction(saleId);
        login();
        Integer oldQuantity = data.getProductTypeByBarCode(barcode).getQuantity();
        assertTrue(data.returnProduct(returnId, barcode, 2));
        assertEquals(oldQuantity, data.getProductTypeByBarCode(barcode).getQuantity(), 0);
        data.endReturnTransaction(returnId, true);
        assertEquals(oldQuantity+2, data.getProductTypeByBarCode(barcode).getQuantity(), 0);
        assertEquals(startingBalance+30, data.computeBalance(), 0);
        data.returnCashPayment(returnId);
        assertEquals(startingBalance+10, data.computeBalance(), 0);
    }

    @After
    public void cleanup(){
        data.reset();
    }
}
