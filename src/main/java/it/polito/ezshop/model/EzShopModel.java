package it.polito.ezshop.model;

import it.polito.ezshop.data.BalanceOperation;
import it.polito.ezshop.data.User;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.data.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class EzShopModel {

    final static String folder = "persistent";
    final static boolean persistent = true;
    List<UserModel> UserList;
    Map<Integer, Integer> LoyaltyCardMap;
    Map<Integer, CustomerModel> CustomerMap;
    UserModel CurrentlyLoggedUser;
    Map<String, ProductTypeModel> ProductMap;  //K = productCode (barCode), V = ProductType
    Map<Integer, OrderModel> ActiveOrderMap;         //K = OrderId, V = Order
    Map<Integer, SaleModel> activeSaleMap;
    Map<Integer, ReturnModel> activeReturnMap;
    BalanceModel balance;
    JsonWrite writer;
    JsonRead reader;
    int maxProductId = 1;
    int maxCardId = 1;
    int maxCustomerId = 1;

    public EzShopModel() {
        UserList = new ArrayList<>();
        CustomerMap = new HashMap<>();
        LoyaltyCardMap = new HashMap<>();
        CurrentlyLoggedUser = null;
        ProductMap = new HashMap<>();
        ActiveOrderMap = new HashMap<>();
        activeSaleMap = new HashMap<>();
        activeReturnMap = new HashMap<>();
        balance = new BalanceModel();
        try {
            writer = new JsonWrite(folder);
            reader = new JsonRead(folder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadEZShop(){
    if(persistent) {
        UserList = reader.parseUsers();
        CustomerMap = reader.parseCustomers().stream().collect(Collectors.toMap(CustomerModel::getId, (c) -> c));
        ProductMap = reader.parseProductType().stream().collect(Collectors.toMap(ProductType::getBarCode, (cust) -> cust));
        balance = reader.parseBalance();
        ActiveOrderMap = reader.parseOrders().stream().collect(Collectors.toMap(OrderModel::getOrderId, (ord) -> ord));
        LoyaltyCardMap = reader.parseLoyalty();
        maxProductId = ProductMap.values().stream().map(ProductTypeModel::getId).max(Integer::compare).orElse(1);
        maxCardId = LoyaltyCardMap.keySet().stream().max(Integer::compare).orElse(1);
        maxCustomerId = CustomerMap.keySet().stream().max(Integer::compare).orElse(1);
        BalanceOperationModel.currentMaxId = balance.getAllBalanceOperations().stream().mapToInt(BalanceOperation::getBalanceId).max().orElse(1) + 1;
        UserModel.currentId = UserList.stream().mapToInt(UserModel::getId).max().orElse(1) + 1;
        }
    }

    public boolean reset(){
        return writer.reset();
    }

    public User getUserById(Integer Id) throws UnauthorizedException, InvalidUserIdException {
        checkAuthorization(Roles.Administrator);
        if (Id == null || Id <= 0 )
            throw new InvalidUserIdException("User Not Found");
        return UserList.stream().filter((us) -> us.getId().equals(Id)).findAny().orElse(null);
    }

    public List<User> getUserList() throws UnauthorizedException {
        checkAuthorization(Roles.Administrator);
        return new ArrayList<>(this.UserList);
    }


    public boolean deleteUserById(Integer id) throws UnauthorizedException, InvalidUserIdException {
        int ix = UserList.indexOf((UserModel) getUserById(id));
        if (ix == -1)
            return false;
        UserList.remove(ix);
        writer.writeUsers(this.UserList);
        return true;
    }


    /**
     * Made by PAOLO
     *
     * @param Username username string
     * @param Password password string
     * @return User class or null if user not found or the password was not correct
     * @throws InvalidPasswordException if password is empty or null
     * @throws InvalidUsernameException if username is empty or null
     */
    public User login(String Username, String Password) throws InvalidPasswordException, InvalidUsernameException {
        UserModel newLoggedUser;
        if (Username == null || Username.equals(""))
            throw new InvalidUsernameException("Username is null or empty");
        if (Password == null || Password.equals(""))
            throw new InvalidPasswordException("Password is null or empty");
        Optional<UserModel> userfound = this.UserList.stream().filter((user) -> user.getUsername().equals(Username)).findFirst();
        if (!userfound.isPresent())
            return null;
        else
            newLoggedUser = userfound.get().checkPassword(Password) ? userfound.get() : null;
        if (newLoggedUser != null)
            this.CurrentlyLoggedUser = newLoggedUser;
        return newLoggedUser;
    }

    public boolean logout() {
        if (this.CurrentlyLoggedUser == null)
            return false;
        this.CurrentlyLoggedUser = null;
        return true;
    }

    /**
     * Made by PAOLO
     *
     * @param username: String for Username, must be unique or null is returned
     * @param password: String for password
     * @param role:     String for role
     * @return UserModel class
     * @throws InvalidRoleException     null or empty string
     * @throws InvalidUsernameException null or empty string
     * @throws InvalidPasswordException null or empty string
     */
    public User createUser(String username, String password, String role) throws InvalidRoleException, InvalidUsernameException, InvalidPasswordException {

        if (role == null || role.equals(""))
            throw new InvalidRoleException("Role is empty or null");

        if (username == null || username.equals(""))
            throw new InvalidUsernameException("Username is empty or null");

        if (password == null || password.equals(""))
            throw new InvalidPasswordException("Password is empty or null");

        if (UserList.stream().anyMatch((user) -> (user.getUsername().equals(username))))
            return null;

        UserModel newUser = new UserModel(username, password, role);
        this.UserList.add(newUser);
        writer.writeUsers(UserList);
        return newUser;

    }

    public BalanceModel getBalance() {
        return this.balance;
    }


    /**
     * Made by OMAR
     *
     * @param productCode:  String , the code of the product that we should order as soon as possible
     * @param quantity:     int, the quantity of product that we should order
     * @param pricePerUnit: double, the price to correspond to the supplier
     * @return Integer, OrderID of the new Order, -1 if the ProductType doesn't exist
     */
    public Integer createOrder(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {

        checkOrderInputs(productCode, quantity, pricePerUnit);
        this.checkAuthorization(Roles.ShopManager, Roles.Administrator);

        if(!ProductTypeModel.checkBarCodeWithAlgorithm(productCode))
            throw new InvalidProductCodeException();
        if (this.ProductMap.get(productCode) == null) { //ProductType with productCode doesn't exist
            return -1;
        }

        OrderModel newOrder = new OrderModel(productCode, quantity, pricePerUnit);
        newOrder.setStatus("ISSUED");
        this.ActiveOrderMap.put(newOrder.getOrderId(), newOrder);
        writer.writeOrders(ActiveOrderMap);
        return newOrder.getOrderId();
    }

    /**
     * Made by OMAR
     *
     * @param productCode:  String, code of the product to order
     * @param quantity:     int, product's quantity to order
     * @param pricePerUnit: double, single price of each product
     * @return Integer: the id of the order (> 0)
     * -1 if the product does not exists, if the balance is not enough to satisfy the order
     */
    public Integer payOrderFor(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
        boolean result;
        BalanceModel bal = getBalance();
        if(!ProductTypeModel.checkBarCodeWithAlgorithm(productCode))
            throw new InvalidProductCodeException();
        checkOrderInputs(productCode, quantity, pricePerUnit);
        checkAuthorization(Roles.Administrator, Roles.ShopManager);
        if (this.ProductMap.get(productCode) == null) { //ProductType with productCode doesn't exist
            return -1;
        }

        OrderModel newOrder = new OrderModel(productCode, quantity, pricePerUnit);

        result = bal.checkAvailability(newOrder.getTotalPrice());
        if (result) {  //if it's possible to do this Order then...
           //if the balanceUpdate is successful then...
            newOrder.setStatus("PAYED");
            OrderTransactionModel orderTransactionModel = new OrderTransactionModel(newOrder);
            bal.addOrderTransaction(orderTransactionModel);
            this.ActiveOrderMap.put(newOrder.getOrderId(), newOrder);
            if(!writer.writeOrders(ActiveOrderMap))
                return -1;  //problem with db
            if(!writer.writeBalance(bal))
                return -1;
            return newOrder.getOrderId();
        }
        return -1;
    }

    /**
     * Made by OMAR
     *
     * @param orderId: Integer, id of the order to be ORDERED
     * @return boolean: true if success, else false
     */
    public boolean payOrder(Integer orderId) throws InvalidOrderIdException, UnauthorizedException {
        boolean result;
        if (orderId == null || orderId <= 0)
            throw new InvalidOrderIdException("orderId is not valid");

        checkAuthorization(Roles.Administrator, Roles.ShopManager);



        BalanceModel bal = this.getBalance();
        OrderModel ord = this.ActiveOrderMap.get(orderId);
        OrderTransactionModel orderTransactionModel;


        if (ord == null) {        //The order doesn't exist
            return false;
        }
        if (ord.getStatus().equals("PAYED")) { //NO EFFECT
            return false;
        }
        if (ord.getStatus().equals("ISSUED")) {
            result = bal.checkAvailability(ord.getTotalPrice());
            if (result) { //if the balanceUpdate is successful then...
                ord.setStatus("PAYED");
                orderTransactionModel = new OrderTransactionModel(ord);
                bal.addOrderTransaction(orderTransactionModel);
                if(!writer.writeOrders(ActiveOrderMap))
                    return false;
                return writer.writeBalance(bal);
            }
            return false;
        }
        return true;
    }

    /**
     * Made by Omar
     *
     * @param orderId the id of the order that has arrived
     * @return true if the operation was successful
     * false if the order does not exist or if it was not in an ORDERED/COMPLETED state
     */

    public boolean recordOrderArrival(Integer orderId) throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException {
        boolean result = false;
        OrderModel ord;
        ProductTypeModel product;
        int quantity;

        if (orderId == null || orderId <= 0) {
            throw new InvalidOrderIdException("orderId not valid");
        }
        ord = this.ActiveOrderMap.get(orderId);
        if (ord == null) {
            return false;
        }
        product = ProductMap.get(ord.getProductCode());
        if (product.getLocation() == null || product.getLocation().equals("")) {  //the product must have a location registered
            throw new InvalidLocationException("Product with invalid location");
        }
        checkAuthorization(Roles.ShopManager, Roles.Administrator);
        if (ord.getStatus().equals("COMPLETED")) {  //no effect
            return false;
        }
        if (ord.getStatus().equals("PAYED")) {
            ord.setStatus("COMPLETED");
            quantity = ord.getQuantity();
            product.updateAvailableQuantity(quantity);
            writer.writeProducts(ProductMap);
            if(!writer.writeOrders(ActiveOrderMap)) return false;  //problem with db
            result = true;
        }
        return result;
    }

    /**
     * Method for Checking the level of authorization of the user
     *
     * @param rs Role or multiple roles, variable number of arguments is supported
     * @throws UnauthorizedException thrown when CurrentlyLoggedUser is null or his role is not one authorized
     */
    public void checkAuthorization(Roles... rs) throws UnauthorizedException {
        if (this.CurrentlyLoggedUser == null)
            throw new UnauthorizedException("No logged user");
        if (Arrays.stream(rs).anyMatch((r) -> r == this.CurrentlyLoggedUser.getEnumRole()))
            return;
        throw new UnauthorizedException("User does not have right authorization");
    }

    /**
     * Made by Manuel
     *
     * @param toBeAdded the amount of money (positive or negative) to be added to the current balance. If this value
     *                  is >= 0 then it should be considered as a CREDIT, if it is < 0 as a DEBIT
     * @return true if the balance has been successfully updated
     * false if toBeAdded + currentBalance < 0.
     * @throws UnauthorizedException if there is no logged user or if it has not the rights to perform the operation
     */
    public boolean recordBalanceUpdate(double toBeAdded) throws UnauthorizedException {
        this.checkAuthorization(Roles.Administrator, Roles.ShopManager);
        double balance = this.computeBalance();
        if (balance + toBeAdded >= 0) {
            String operationType = toBeAdded >= 0 ? "CREDIT" : "DEBIT";
            BalanceOperationModel balanceOP = new BalanceOperationModel(operationType, toBeAdded, LocalDate.now());
            this.balance.addBalanceOperation(balanceOP);
            writer.writeBalance(this.balance);
            return true;
        }
        return false;
    }

    /**
     * Made by Manuel
     *
     * @param from the start date : if null it means that there should be no constraint on the start date
     * @param to   the end date : if null it means that there should be no constraint on the end date
     * @return All the operations on the balance whose date is <= to and >= from
     */
    public List<BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to) throws UnauthorizedException {
        this.checkAuthorization(Roles.Administrator, Roles.ShopManager);
        return this.balance.getCreditsAndDebits(from, to);
    }

    /**
     * Made by Manuel
     *
     * @return the value of the current balance
     * @throws UnauthorizedException if there is no logged user or if it has not the rights to perform the operation
     */
    public double computeBalance() throws UnauthorizedException {
        this.checkAuthorization(Roles.Administrator, Roles.ShopManager);
        return this.balance.computeBalance();
    }

    /**
     * Made by Omar
     *
     * @return the list of all Orders, which any status
     */
    public List<Order> getOrderList() throws UnauthorizedException {
        this.checkAuthorization(Roles.ShopManager, Roles.Administrator);
        return new ArrayList<>(ActiveOrderMap.values());
    }

    /**
     * Made by Andrea
     *
     * @return a CustomerId
     */

    public int createCustomer(String customerName) throws InvalidCustomerNameException, UnauthorizedException {
        this.checkAuthorization(Roles.Administrator, Roles.ShopManager, Roles.Cashier);
        if (checkString(customerName))
            throw new InvalidCustomerNameException();

        CustomerModel c = new CustomerModel(customerName, ++maxCustomerId);
        CustomerMap.put(c.getId(), c);
        writer.writeCustomers(new ArrayList<>(CustomerMap.values()));
        return c.getId();
    }

    /**
     * Made by Andrea
     *
     * @return a Customer given its id
     */

    public CustomerModel getCustomerById(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
        if(id==null || id<=0)
            throw new InvalidCustomerIdException();
        this.checkAuthorization(Roles.Administrator, Roles.ShopManager, Roles.Cashier);
        if (!CustomerMap.containsKey(id))
            return null;
        return CustomerMap.get(id);
    }

    /**
     * Made by Andrea
     *
     * @return the result of the operation
     */

    public boolean modifyCustomer(Integer id, String newCustomerName, String newCustomerCard) throws InvalidCustomerIdException, UnauthorizedException, InvalidCustomerNameException, InvalidCustomerCardException {
        this.checkAuthorization(Roles.Administrator, Roles.ShopManager, Roles.Cashier);
        if(id == null || id <= 0){
            throw new InvalidCustomerIdException();
        }
        CustomerModel c = this.getCustomerById(id);
        if (checkString(newCustomerName))
            throw new InvalidCustomerNameException();
        if(newCustomerCard != null && !newCustomerCard.equals("") && !LoyaltyCardModel.checkCard(newCustomerCard))
            throw new InvalidCustomerCardException();
        if (!CustomerMap.containsKey(id))
            return false;
        c.setCustomerName(newCustomerName);
        if(newCustomerCard != null && !newCustomerCard.equals("")) {
            if (!LoyaltyCardMap.containsKey(Integer.parseInt(newCustomerCard)))
                return false;
            if(CustomerMap.values().stream().filter((user)->user.loyaltyCard != null).anyMatch((user)->user.getCustomerCard().equals(newCustomerCard)))
                return false;
            if(c.getCustomerCard() == null)
                attachCardToCustomer(newCustomerCard, id);
            else
                c.setCustomerCard(newCustomerCard);
        }
        if(newCustomerCard != null && newCustomerCard.equals("")){
            c.setCustomerCard(null);
        }
        writer.writeLoyaltyCards(LoyaltyCardMap);
        writer.writeCustomers(new ArrayList<>(CustomerMap.values()));
        return true;
    }

    /**
     * Made by Andrea
     *
     * @return the result of the operation
     */

    public boolean deleteCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
        if(id == null || id <= 0)
            throw new InvalidCustomerIdException();
        this.checkAuthorization(Roles.Administrator, Roles.ShopManager, Roles.Cashier);
        if (!CustomerMap.containsKey(id))
            return false;

        this.CustomerMap.remove(id);
        writer.writeCustomers(new ArrayList<>(CustomerMap.values()));
        return true;
    }

    /**
     * Made by Andrea
     *
     * @return the list of customers
     */

    public List<Customer> getAllCustomer() throws UnauthorizedException {
        checkAuthorization(Roles.Administrator, Roles.ShopManager, Roles.Cashier);
        return new ArrayList<>(CustomerMap.values());
    }

    /**
     * Made by Andrea
     *
     * @return the loyalty card code
     */

    public String createCard() throws UnauthorizedException {
        this.checkAuthorization(Roles.Administrator, Roles.ShopManager, Roles.Cashier);
        LoyaltyCardMap.put(maxCardId, null);
        maxCardId++;
        writer.writeLoyaltyCards(this.LoyaltyCardMap);
        StringBuilder out = new StringBuilder();
        for(int i = 0; i < 10-(String.valueOf(maxCardId - 1)).length(); i++)
            out.append(0);
        return out.append(maxCardId - 1).toString();
    }

    /**
     * Made by Andrea
     *
     * @return the result of the operation
     */

    public boolean attachCardToCustomer(String customerCard, Integer userId) throws UnauthorizedException, InvalidCustomerIdException, InvalidCustomerCardException {
        this.checkAuthorization(Roles.Administrator, Roles.ShopManager, Roles.Cashier);
        if(userId == null || userId <= 0)
            throw new InvalidCustomerIdException();
        if (!LoyaltyCardModel.checkCard(customerCard))
            throw new InvalidCustomerCardException();
        if (!CustomerMap.containsKey(userId))
            return false;
        if(!LoyaltyCardMap.containsKey(Integer.parseInt(customerCard)))
            return false;
        if(LoyaltyCardMap.get(Integer.parseInt(customerCard)) != null)
            return false;
        CustomerMap.get(userId).setCustomerCard(customerCard);
        LoyaltyCardMap.put(Integer.parseInt(customerCard), userId);
        writer.writeCustomers(new ArrayList<>(CustomerMap.values()));
        writer.writeLoyaltyCards(LoyaltyCardMap);
        return true;
    }

    /**
     * Made by Andrea
     *
     * @return the result of the operation
     */

    public boolean modifyPointsOnCard(String customerCard, int pointsToBeAdded) throws InvalidCustomerCardException, UnauthorizedException {
        this.checkAuthorization(Roles.Administrator, Roles.ShopManager, Roles.Cashier);
        if(!LoyaltyCardModel.checkCard(customerCard))
            throw new InvalidCustomerCardException();
        if (!LoyaltyCardMap.containsKey(Integer.parseInt(customerCard)))
            return false;
        if(LoyaltyCardMap.get(Integer.parseInt(customerCard)) == null)
            return false;
        if(CustomerMap.get(LoyaltyCardMap.get(Integer.parseInt(customerCard))).getLoyaltyCard().getPoints() + pointsToBeAdded < 0)
            return false;
        CustomerMap.get(LoyaltyCardMap.get(Integer.parseInt(customerCard))).getLoyaltyCard().updatePoints(pointsToBeAdded);
        writer.writeCustomers((new ArrayList<>(CustomerMap.values())));
        writer.writeLoyaltyCards(LoyaltyCardMap);
        return true;
    }

    public ProductType createProduct(String description, String productCode, double pricePerUnit, String Note) throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
           if(checkString(description))
               throw new InvalidProductDescriptionException();
           if(checkString(productCode) || !ProductTypeModel.checkBarCodeWithAlgorithm(productCode))
               throw new InvalidProductCodeException();
           if(checkDouble(pricePerUnit))
               throw new InvalidPricePerUnitException();
           checkAuthorization(Roles.Administrator, Roles.ShopManager);
           ProductTypeModel product = new ProductTypeModel(++maxProductId,description, productCode, pricePerUnit, Note);
           this.ProductMap.put(product.getBarCode(), product);
           writer.writeProducts(ProductMap);
           return product;
    }

    public ProductType getProductByBarCode(String barCode) throws InvalidProductCodeException, UnauthorizedException {
        if(!ProductTypeModel.checkBarCodeWithAlgorithm(barCode)){
            throw new InvalidProductCodeException();
        }
        checkAuthorization(Roles.Administrator, Roles.ShopManager);
        return ProductMap.getOrDefault(barCode, null);
    }

    public List<ProductTypeModel> getAllProducts() throws UnauthorizedException {
        checkAuthorization(Roles.Administrator, Roles.ShopManager, Roles.Cashier);
        return new ArrayList<>(ProductMap.values());
    }

    public ProductTypeModel getProductById(Integer id) throws UnauthorizedException, InvalidProductIdException {
        if(id == null || id <= 0)
            throw new InvalidProductIdException();
        checkAuthorization(Roles.Administrator, Roles.ShopManager, Roles.Cashier);
        return ProductMap.values().stream().filter((prod)-> prod.getId().equals(id)).findAny().orElse(null);
    }

    public boolean updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote) throws UnauthorizedException, InvalidProductCodeException, InvalidProductIdException {
        this.checkAuthorization(Roles.Administrator, Roles.ShopManager);
        if (!ProductTypeModel.checkBarCodeWithAlgorithm(newCode))
            throw new InvalidProductCodeException();
        ProductTypeModel product = getProductById(id);
        if(product == null || (ProductMap.containsKey(newCode) && !newCode.equals(product.barCode)))
            return false;
        ProductMap.remove(product.getBarCode());
        product.setProductDescription(newDescription);
        product.setBarCode(newCode);
        product.setPricePerUnit(newPrice);
        product.setNote(newNote);
        ProductMap.put(product.getBarCode(), product);
        writer.writeProducts(ProductMap);
        return true;
    }

    public boolean updateProductPosition(Integer id, String newPos) throws UnauthorizedException, InvalidProductIdException, InvalidLocationException {
        checkAuthorization(Roles.Administrator, Roles.ShopManager);
        ProductTypeModel pdr = getProductById(id);
        if(newPos != null && !newPos.matches("^\\d+-\\w+-\\d+$"))
            throw new InvalidLocationException();
        if(pdr == null)
            return false;
        if(newPos == null) {
            pdr.setLocation(null);
            return true;
        }
        if(ProductMap.values().stream().filter((prod)->prod.location!=null).anyMatch((prod)-> prod.location.equals(newPos)))
            return false;
        pdr.setLocation(newPos);
        writer.writeProducts(ProductMap);
        return true;
    }

    public boolean deleteProduct(Integer id) throws InvalidProductIdException, UnauthorizedException {
        if(id <= 0)
            throw new InvalidProductIdException();
        checkAuthorization(Roles.Administrator, Roles.ShopManager);
        ProductTypeModel product = ProductMap.values().stream().filter((prod)->prod.getId().equals(id)).findAny().orElse(null);
        if(product == null)
            return false;
        ProductMap.remove(product.getBarCode());
        writer.writeProducts(ProductMap);
        return true;
    }

    private static boolean checkString(String st){
        return st == null || st.equals("");
    }
    private static boolean checkDouble(double price){
        return price <= 0;
    }
    /**
     * Made by Omar
     * @param transactionId the number of the transaction that the customer wants to pay
     * @param cash the cash received by the cashier
     */
    public double receiveCashPayment(Integer transactionId, double cash) throws InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException{
        double change;
        if (transactionId == null || transactionId <= 0) {
            throw new InvalidTransactionIdException("transactionID not valid");
        }
        checkAuthorization(Roles.Administrator, Roles.ShopManager, Roles.Cashier);
        if(cash <= 0){
            throw new InvalidPaymentException("cash value not valid");
        }
        BalanceModel bal = this.getBalance();
        SaleTransactionModel transaction = bal.getSaleTransactionById(transactionId);
        if(transaction == null ) return -1; //sale doesn't exist
        TicketModel ticket = transaction.getTicket();
        if(ticket == null ) return  -1; //ticket doesn't exist
        CashPaymentModel cashPayment = new CashPaymentModel(ticket.getAmount(),false,cash);
        change = cashPayment.computeChange();
        if (change < 0){  //the cash is not enough
            return -1;
        }
        ticket.setStatus("PAYED");
        transaction.setTicketPayment(cashPayment);
        if(!writer.writeBalance(bal)) return -1;  //problem with db
        return change;
    }

    /**
     * Made by Omar
     * @param transactionId the number of the transaction that the customer wants to pay
     * @param creditCard the credit card of the customer
     */
    public boolean receiveCreditCardPayment(Integer transactionId, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException{
        boolean outcome;
        if(creditCard == null || creditCard.equals("")) throw new InvalidCreditCardException("creditCard number empty or null");
        if(!CreditCardPaymentModel.validateCardWithLuhn(creditCard)) throw  new InvalidCreditCardException("creditCard not verified");
        checkAuthorization(Roles.Administrator, Roles.ShopManager, Roles.Cashier);

        if(transactionId==null || transactionId <= 0) throw new InvalidTransactionIdException("transactionID not valid");

        BalanceModel bal = getBalance();
        SaleTransactionModel saleTransaction = bal.getSaleTransactionById(transactionId);
        if(saleTransaction == null ) return false; //sale doesn't exist
        TicketModel ticket = saleTransaction.getTicket();
        if(ticket == null ) return  false; //ticket doesn't exist
        CreditCardPaymentModel creditCardPayment = new CreditCardPaymentModel(ticket.getAmount(),false);
        outcome=creditCardPayment.sendPaymentRequestThroughAPI(creditCard);
        if(!outcome) return false;  //problem with payment (not enough money or card doesn't exist)
        ticket.setStatus("PAYED");
        saleTransaction.setTicketPayment(creditCardPayment);
        return writer.writeBalance(bal); //false if problem with json
    }

    public double returnCashPayment(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException{
        if( returnId == null || returnId <= 0 ) throw new InvalidTransactionIdException("returnID not valid");
        checkAuthorization(Roles.Administrator,Roles.Cashier,Roles.ShopManager);

        ReturnTransactionModel ret = getBalance().getReturnTransactionById(returnId);
        if(ret == null) return -1;
        if(!(ret.getStatus().equals("closed"))) return -1; //returnTransaction not ended
        ret.setPayment(new CashPaymentModel(ret.getAmountToReturn(),true,ret.getAmountToReturn()));
        ret.setStatus("payed");
        balance.getSaleTransactionMap().get(ret.getSaleId()).setBeforeMoney(0);
        if(!writer.writeBalance(balance)) return -1; //problem with db
        return ret.getAmountToReturn();
    }

    public double returnCreditCardPayment(Integer returnId, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException{
        if(returnId == null || returnId <= 0) throw new InvalidTransactionIdException("returnID not valid");
        if(creditCard == null || creditCard.equals("")) throw new InvalidCreditCardException("creditCard number empty or null");
        if(!CreditCardPaymentModel.validateCardWithLuhn(creditCard)) throw  new InvalidCreditCardException("creditCard not verified");
        checkAuthorization(Roles.Administrator,Roles.Cashier,Roles.ShopManager);
        ReturnTransactionModel ret = getBalance().getReturnTransactionById(returnId);
        if(ret==null) return -1; //the return doesn't exist
        if(!(ret.getStatus().equals("closed"))) return -1; //returnTransaction not ended
        CreditCardPaymentModel cardPayment = new CreditCardPaymentModel(ret.getAmountToReturn(), true);
        if(!cardPayment.sendPaymentRequestThroughAPI(creditCard)) return -1;
        ret.setPayment(cardPayment);
        balance.getSaleTransactionMap().get(ret.getSaleId()).setBeforeMoney(0);
        if(!writer.writeBalance(getBalance())) return -1; //problem with db
        ret.setStatus("payed");
        return ret.getPayment().getAmount();
    }



    public Integer startSaleTransaction(){
        SaleModel sale = new SaleModel();
        activeSaleMap.put(sale.getId(), sale);
        return sale.getId();
    }

    /**
     * made by Manuel
     * @param saleId Identifier of an Active SaleModel
     * @param barCode barCode of the product that want to be added
     * @param amount the quantity to add
     * @return true if the operation is successful
     *         false   if the product code does not exist,
     *                 if the quantity of product cannot satisfy the request,
     *                 if the transaction id does not identify a started and open transaction.
     *
     *      @throws InvalidTransactionIdException if the transaction id less than or equal to 0 or if it is null
     *      @throws InvalidProductCodeException if the product code is empty, null or invalid
     *      @throws InvalidQuantityException if the quantity is less than 0
     */
    public boolean addProductToSale(Integer saleId, String barCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException {
        checkId(saleId);
        if(!ProductTypeModel.checkBarCodeWithAlgorithm(barCode) )
            throw new InvalidProductCodeException();
        if(amount <= 0)
            throw new InvalidQuantityException();
        if(activeSaleMap.get(saleId) == null)
            return false;
        ProductTypeModel p = ProductMap.get(barCode);

        if(p == null || p.quantity - amount < 0)
            return false;
        p.quantity -= amount;
        writer.writeProducts(ProductMap);
        return activeSaleMap.get(saleId).addProduct(new TicketEntryModel(barCode, p.getProductDescription(), amount, p.getPricePerUnit()));
    }

    public boolean deleteProductFromSale(Integer saleId, String barCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException {
        checkId(saleId);
        if(!ProductTypeModel.checkBarCodeWithAlgorithm(barCode))
            throw new InvalidProductCodeException();
        if(amount <= 0)
            throw new InvalidQuantityException();
        if(activeSaleMap.get(saleId) == null)
            return false;
        if(ProductMap.get(barCode) == null)
            return false;
        ProductMap.get(barCode).quantity += amount;
        writer.writeProducts(ProductMap);
        return activeSaleMap.get(saleId).removeProduct(barCode, amount);
    }

    public boolean applyDiscountRateToProduct(Integer saleId, String barCode, double discountRate) throws InvalidDiscountRateException, InvalidTransactionIdException, InvalidProductCodeException {
        checkId(saleId);
        if(!ProductTypeModel.checkBarCodeWithAlgorithm(barCode))
            throw new InvalidProductCodeException();
        if(discountRate < 0 || discountRate >= 1.00)
            throw new InvalidDiscountRateException();
        if(activeSaleMap.get(saleId) == null)
            return false;
        return activeSaleMap.get(saleId).setDiscountRateForProduct(barCode, discountRate);
    }

    public boolean applyDiscountRateToSale(Integer saleId, double discountRate) throws InvalidDiscountRateException, InvalidTransactionIdException {
        checkId(saleId);
        if(discountRate < 0 || discountRate >= 1.00)
            throw new InvalidDiscountRateException();
        if(activeSaleMap.get(saleId) == null)
            return false;
        return activeSaleMap.get(saleId).setDiscountRateForSale(discountRate);
    }

    public int computePointsForSale(Integer saleId) throws InvalidTransactionIdException {
        checkId(saleId);
        SaleTransactionModel sale;
        if(!activeSaleMap.containsKey(saleId)) {
            sale = balance.getSaleTransactionById(saleId);
            if(sale == null)
                return -1;
            return sale.computePoint();
        }
        else
            return (int) activeSaleMap.get(saleId).computeCost() / 10;
    }

    public boolean endSaleTransaction(Integer saleId) throws InvalidTransactionIdException {
        checkId(saleId);
        if(activeSaleMap.get(saleId) == null)
            return false;

        SaleModel activeSale = activeSaleMap.get(saleId);
        if(!activeSale.closeTransaction())
            return false;
        SaleTransactionModel sale = new SaleTransactionModel(activeSale);
        activeSaleMap.get(saleId).balanceOperationId = sale.getBalanceId();
        getBalance().addSaleTransactionModel(saleId, sale);
        activeSaleMap.remove(activeSale.getId());
        writer.writeBalance(balance);
        return true;
    }

    public boolean deleteSaleTransaction(Integer saleId) throws InvalidTransactionIdException {
        checkId(saleId);
        SaleTransactionModel sale = balance.getSaleTransactionById(saleId);
        if(sale == null)
            return false;
        if(sale.getTicket().getStatus().equals("PAYED"))
            return false;
        sale.getEntries().forEach((entry) -> ProductMap.get(entry.getBarCode()).updateAvailableQuantity(entry.getAmount()));
        if(sale.getTicket().getStatus().equals("CLOSED")) {
            balance.getSaleTransactionMap().remove(saleId);
            writer.writeBalance(balance);
            return true;
        }
        else
            return false;
    }

    public Integer startReturnTransaction(Integer saleId) throws InvalidTransactionIdException {
        checkId(saleId);
        if(!balance.getSaleTransactionMap().containsKey(saleId))
            return -1;
        SaleTransactionModel sale = getBalance().getSaleTransactionById(saleId);
        if(!sale.getTicket().getStatus().equals("PAYED"))
            return -1;
        ReturnModel retur = new ReturnModel(saleId, sale);
        activeReturnMap.put(retur.id, retur);
        return retur.id;
    }

    public boolean returnProduct(Integer returnId, String barCode, Integer amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException {
        checkId(returnId);
        if(!ProductTypeModel.checkBarCodeWithAlgorithm(barCode))
            throw new InvalidProductCodeException();
        if(amount <= 0)
            throw new InvalidQuantityException();
        if(!activeReturnMap.containsKey(returnId))
            return false;
        if(!ProductMap.containsKey(barCode))
            return false;
        List<TicketEntryModel> tick = activeReturnMap.get(returnId).getSale().getTicket().getTicketEntryModelList();
        if(tick.stream().noneMatch((en)->en.getBarCode().equals(barCode)))
            return false;
        if(tick.stream().filter((en)->en.getBarCode().equals(barCode)).mapToInt(TicketEntryModel::getAmount).sum() < amount)
            return false;
        ReturnModel activeReturn = activeReturnMap.get(returnId);
        tick.forEach((entry)-> {
            if(entry.getBarCode().equals(barCode))
                activeReturn.getProductList().add(new TicketEntryModel(ProductMap.get(barCode), amount, entry.getDiscountRate()));
        });
        writer.writeProducts(ProductMap);
        return true;
    }

    public boolean endReturnTransaction(Integer returnId, boolean commit) throws InvalidTransactionIdException {
        checkId(returnId);
        if(!activeReturnMap.containsKey(returnId))
            return false;
        ReturnModel returnTransaction = activeReturnMap.get(returnId);
        if(commit) {
            returnTransaction.commit(ProductMap);
            ReturnTransactionModel r = new ReturnTransactionModel(returnTransaction);
            balance.addReturnTransactionModel(returnId, r);
            writer.writeBalance(balance);
        }
        writer.writeProducts(ProductMap);
        activeReturnMap.remove(returnId);
        return true;
    }

    public boolean deleteReturnTransaction(Integer returnId) throws InvalidTransactionIdException {
        checkId(returnId);
        if(!balance.getReturnTransactionMap().containsKey(returnId))
            return false;
        if(balance.getReturnTransactionMap().get(returnId).getStatus().equals("payed"))
            return false;
        ReturnTransactionModel returnOperation = balance.getReturnTransactionMap().get(returnId);
        SaleTransactionModel saleOperation = balance.getSaleTransactionMap().get(returnOperation.getSaleId());
        for (TicketEntryModel entry : returnOperation.getReturnedProductList()) {
            ProductMap.get(entry.getBarCode()).updateAvailableQuantity(entry.getAmount());
        }
        for (TicketEntryModel entry : returnOperation.getReturnedProductList()) {
            for (TicketEntryModel saleEntry : saleOperation.getTicket().getTicketEntryModelList()) {
                if (saleEntry.getBarCode().equals(entry.getBarCode())) {
                    saleEntry.addAmount(entry.getAmount());
                    break;
                }
            }
        }
        saleOperation.updateAmount();
        saleOperation.setBeforeMoney(0);
        balance.getAllBalanceOperations().remove(balance.getReturnTransactionById(returnId));
        balance.getReturnTransactionMap().remove(returnId);
        writer.writeBalance(balance);
        writer.writeProducts(ProductMap);
        return true;
    }


    private void checkId(Integer id) throws InvalidTransactionIdException {
        if(id == null || id <= 0)
            throw new InvalidTransactionIdException();
    }

    private void checkOrderInputs(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException {
        if (productCode == null || productCode.equals(""))
            throw new InvalidProductCodeException("Product Code is null or empty");
        if (quantity <= 0)
            throw new InvalidQuantityException("Quantity must be greater than zero");
        if (pricePerUnit <= 0)
            throw new InvalidPricePerUnitException("Price per Unit must be greater than zero");
    }
}