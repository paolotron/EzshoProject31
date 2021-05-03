package it.polito.ezshop.model;

import it.polito.ezshop.EZShop;
import it.polito.ezshop.data.Customer;
import it.polito.ezshop.data.User;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidUsernameException;

import java.util.ArrayList;
import java.util.HashMap;

public class EzShopModel {
    ArrayList<User> UserList;
    HashMap<Integer, Customer> CustomerMap;
    User CurrentlyLoggedUser;


    public EzShopModel(){
        UserList = new ArrayList<User>();
        CustomerMap = new HashMap<Integer, Customer>();
    }

    public EzShopModel(String file){

    }

    public User createUser(String username, String password, String role) throws InvalidRoleException, InvalidUsernameException {
        // TODO: Add password validation and throw InvalidPasswordException
        if(UserList.stream().anyMatch((a)->(a.getUsername().equals(username)))){
            throw new InvalidUsernameException();
        }
        return new UserModel(username, password, role);
    }


}
