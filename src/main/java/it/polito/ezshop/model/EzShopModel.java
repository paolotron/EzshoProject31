package it.polito.ezshop.model;

import it.polito.ezshop.data.User;
import it.polito.ezshop.exceptions.*;

import java.util.*;

public class EzShopModel {
    ArrayList<UserModel> UserList;
    HashMap<Integer, CustomerModel> CustomerMap;
    UserModel CurrentlyLoggedUser;
    TreeMap<String, ProductTypeModel> ProductMap;  //K = productCode (barCode), V = ProductType
    TreeMap<Integer, OrderModel> ActiveOrderMap;         //K = OrderId, V = Order
    TreeMap<Integer, OrderModel> OrderTransactionMap; //K = OrderId, V = Order


    public EzShopModel(){
        UserList = new ArrayList<>();
        CustomerMap = new HashMap<>();
        CurrentlyLoggedUser = null;
        ProductMap = new TreeMap<>();
        ActiveOrderMap = new TreeMap<>();
        OrderTransactionMap = new TreeMap<>();
    }

    public EzShopModel(String file){

    }


    public User getUserById(Integer Id) throws UnauthorizedException, InvalidUserIdException {
        checkAuthorization(Roles.Administrator);
        if ( Id == null || Id == 0)
            throw new InvalidUserIdException("User Not Found");
        return UserList.stream().filter((us)-> us.getId().equals(Id)).findAny().orElse(null);
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
        return true;
    }


    /**
     * Made by PAOLO
     * @param Username username string
     * @param Password password string
     * @return User class or null if user not found or the password was not correct
     * @throws InvalidPasswordException if password is empty or null
     * @throws InvalidUsernameException if username is empty or null
     */
    public User login(String Username, String Password) throws InvalidPasswordException, InvalidUsernameException {
        UserModel newloggedUser;
        if(Username == null || Username.equals(""))
            throw new InvalidUsernameException("Username is null or empty");
        if(Password == null || Password.equals(""))
            throw new InvalidPasswordException("Password is null or empty");
        Optional<UserModel> userfound = this.UserList.stream().filter((user)->user.getUsername().equals(Username)).findFirst();
        if(!userfound.isPresent())
            return null;
        else
            newloggedUser =  userfound.get().checkPassword(Password) ? userfound.get():null;
        if (newloggedUser != null)
            this.CurrentlyLoggedUser = newloggedUser;
        return newloggedUser;
    }

    public boolean logout(){
        if(this.CurrentlyLoggedUser == null)
            return false;
        this.CurrentlyLoggedUser = null;
        return true;
    }

    /**
     * Made by PAOLO
     * @param username: String for Username, must be unique or null is returned
     * @param password: String for password
     * @param role: String for role
     * @return UserModel class
     * @throws InvalidRoleException null or empty string
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

        if(UserList.stream().anyMatch((user)->(user.getUsername().equals(username))))
            return null;

        UserModel newUser = new UserModel(username, password, role);
        this.UserList.add(newUser);
        return newUser;

    }

    //TODO  method to be implemented
    public BalanceModel getBalance(){
        return null;
    }


    /**
     * Made by OMAR
     * @param productCode: String , the code of the product that we should order as soon as possible
     * @param quantity: int, the quantity of product that we should order
     * @param pricePerUnit: double, the price to correspond to the supplier
     * @return Integer, OrderID of the new Order, -1 if the ProductType doesn't exist
     */
    public Integer createOrder(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException{

        if(productCode==null || productCode.equals("")){
            throw new InvalidProductCodeException("Product Code is null or empty");
        }

        if(quantity <= 0){
            throw  new InvalidQuantityException("Quantity must be greater than zero");
        }
        if(pricePerUnit <= 0){
            throw  new InvalidPricePerUnitException("Price per Unit must be greater than zero");
        }

        // Added By Paolo
        checkAuthorization(Roles.ShopOwner, Roles.Administrator);
        // End Added By Paolo

        if(this.ProductMap.get(productCode) == null){ //ProductType with productCode doesn't exist
            return -1;
        }

        OrderModel newOrder = new OrderModel(productCode, quantity, pricePerUnit);
        newOrder.setStatus("Open");
        this.ActiveOrderMap.put(newOrder.getOrderId(), newOrder);
        return newOrder.getOrderId();
    }
    /**
        * Made by OMAR
        * @param orderId: Integer, id of the order to be ORDERED
        * @return boolean: true if success, else false
    */
    public boolean payOrder(Integer orderId) throws InvalidOrderIdException, UnauthorizedException {
        boolean result = false;
        if(orderId <= 0 || orderId == null) {
            throw new InvalidOrderIdException("orderId is not valid");
        }

        checkAuthorization(Roles.Administrator, Roles.Administrator);

        BalanceModel bal;
        OrderModel ord = this.ActiveOrderMap.get(orderId);

        if(ord == null){        //The order doesn't exist
            result = false;
            return result;
        }
        if(ord.getStatus().equals("PAYED")){ //NO EFFECT
            result = true;
        }else if(ord.getStatus().equals("ISSUED")){
            this.ActiveOrderMap.remove(orderId); //removed because I need to change status
            bal = getBalance();
            this.OrderTransactionMap.put(orderId, ord); // TODO FORSE NON NECESSARIO
            //TODO this.BalanceOperationList.put;
            //TODO verificare se l'operazione è andata a buon fine

            ord.setStatus("PAYED");
            result = true;
            //TODO JSON WRITE PART
            this.ActiveOrderMap.put(orderId,ord); //order present again
        }

        return result;
    }


    /**
     * Method for Checking the level of authorization of the user
     * @param rs Role or multiple roles, variable number of arguments is supported
     * @throws UnauthorizedException thrown when CurrentlyLoggedUser is null or his role is not one authorized
     */
    private void checkAuthorization(Roles ...rs) throws UnauthorizedException {
        if(this.CurrentlyLoggedUser == null)
            throw new UnauthorizedException("No logged user");
        if(Arrays.stream(rs).anyMatch((r)->r==this.CurrentlyLoggedUser.getEnumRole()))
            return;
        throw new UnauthorizedException("User does not have right authorization");
    }


}
