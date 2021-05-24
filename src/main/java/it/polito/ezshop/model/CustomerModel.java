package it.polito.ezshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.polito.ezshop.data.Customer;

public class CustomerModel implements Customer {
    int id;
    //static int currentId = 0;
    String name;
    LoyaltyCardModel loyaltyCard;

    public CustomerModel(){}

    public CustomerModel(String name){
        this.id = 0;
        this.name = name;
        this.loyaltyCard = null;
    }

    public CustomerModel(String name, int id){
        this.id = id;
        this.name = name;
        this.loyaltyCard = null;
    }

    @Override
    public String getCustomerName() {
        return name;
    }

    @Override
    public void setCustomerName(String customerName) {
        this.name = customerName;
    }

    @JsonIgnore
    @Override
    public String getCustomerCard() {
        if(loyaltyCard == null)
            return null;
        return loyaltyCard.getId();
    }

    @JsonIgnore
    @Override
    public void setCustomerCard(String customerCard) {
        if(customerCard == null)
            this.loyaltyCard = null;
        else
            this.loyaltyCard = new LoyaltyCardModel(Integer.parseInt(customerCard));
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonIgnore
    @Override
    public Integer getPoints() {
        if(loyaltyCard==null)
            return 0;
        return loyaltyCard.getPoints();
    }

    @JsonIgnore
    @Override
    public void setPoints(Integer points) {
        if(loyaltyCard != null)
            loyaltyCard.updatePoints(points);
    }

    public LoyaltyCardModel getLoyaltyCard() {
        return loyaltyCard;
    }

    public void setLoyaltyCard(LoyaltyCardModel loyaltyCard) {
        this.loyaltyCard = loyaltyCard;
    }
}
