package it.polito.ezshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.polito.ezshop.data.Customer;

public class CustomerModel implements Customer {
    int id;
    //static int currentId = 0;
    String name;
    LoyaltyCardModel loyalityCard;

    public CustomerModel(){}

    public CustomerModel(String name){
        this.id = 0;
        this.name = name;
        this.loyalityCard = null;
    }

    public CustomerModel(String name, int id){
        this.id = id;
        this.name = name;
        this.loyalityCard = null;
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
        if(loyalityCard == null)
            return null;
        return loyalityCard.getId();
    }

    @JsonIgnore
    @Override
    public void setCustomerCard(String customerCard) {
        this.loyalityCard = new LoyaltyCardModel(Integer.parseInt(customerCard));
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
        if(loyalityCard==null)
            return 0;
        return loyalityCard.getPoints();
    }

    @JsonIgnore
    @Override
    public void setPoints(Integer points) {
        if(loyalityCard != null)
            loyalityCard.addPoints(points);
    }

    public LoyaltyCardModel getLoyalityCard() {
        return loyalityCard;
    }

    public void setLoyalityCard(LoyaltyCardModel loyalityCard) {
        this.loyalityCard = loyalityCard;
    }
}
