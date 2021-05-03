package it.polito.ezshop.model;

import it.polito.ezshop.data.Customer;
import it.polito.ezshop.data.User;
import it.polito.ezshop.exceptions.InvalidPasswordException;
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
        CurrentlyLoggedUser = null;
    }

    public EzShopModel(String file){

    }

    public User login(String Username, String Password){
        // TODO Finish this
        this.UserList.stream().findAny();
        return null;
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

        User newUser = new UserModel(username, password, role);
        this.UserList.add(newUser);
        return newUser;

    }




}
