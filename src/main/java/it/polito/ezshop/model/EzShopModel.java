package it.polito.ezshop.model;

import it.polito.ezshop.data.Customer;
import it.polito.ezshop.data.User;
import it.polito.ezshop.exceptions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class EzShopModel {
    ArrayList<UserModel> UserList;
    HashMap<Integer, CustomerModel> CustomerMap;
    User CurrentlyLoggedUser;


    public EzShopModel(){
        UserList = new ArrayList<UserModel>();
        CustomerMap = new HashMap<Integer, CustomerModel>();
        CurrentlyLoggedUser = null;
    }

    public EzShopModel(String file){

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

    /**
     * Made by OMAR
     * @param productCode: String , the code of the product that we should order as soon as possible
     * @param quantity: int, the quantity of product that we should order
     * @param pricePerUnit: double, the price to correspond to the supplier
     * @return OrderModel class
     */
    public OrderModel createOrder(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException{
        if(productCode==null || productCode.equals("")){
            throw new InvalidProductCodeException("Product Code is null or empty");
        }

        OrderModel newOrder = new OrderModel(productCode, quantity, pricePerUnit);
        return newOrder;
    }



}
