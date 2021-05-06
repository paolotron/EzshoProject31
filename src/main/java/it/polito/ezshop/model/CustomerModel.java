package it.polito.ezshop.model;

import it.polito.ezshop.data.Customer;

public class CustomerModel implements Customer {
    int id;
    static int currentId = 0;
    String name;
    LoyalityCard loyalityCard;

    @Override
    public String getCustomerName() {
        return name;
    }

    @Override
    public void setCustomerName(String customerName) {
        this.name = customerName;
    }

    @Override
    public String getCustomerCard() {
        return loyalityCard.getId();
    }

    @Override
    public void setCustomerCard(String customerCard) {
        this.loyalityCard = new LoyalityCard(customerCard);
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getPoints() {
        return loyalityCard.getPoints();
    }

    @Override
    public void setPoints(Integer points) {
        if(loyalityCard != null)
            loyalityCard.addPoints(points);
    }
}
