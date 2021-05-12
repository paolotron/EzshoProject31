package it.polito.ezshop.model;

import it.polito.ezshop.data.ProductType;

public class ProductTypeModel implements ProductType {
    Integer quantity;
    String location;
    String note;
    String productDescription;
    String barCode;
    Double pricePerUnit;
    Integer productId;

    //TODO: Check if values passed to setters are correct
    public ProductTypeModel() {}

    public ProductTypeModel(Integer productId, String description, String productCode, double pricePerUnit, String note){
        this.productDescription = description;
        this.barCode = productCode;
        this.pricePerUnit = pricePerUnit;
        this.note = note;
        this.productId = productId;
    }

    @Override
    public Integer getQuantity() {
        return quantity;
    }

    @Override
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String getNote() {
        return note;
    }

    @Override
    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String getProductDescription() {
        return productDescription;
    }

    @Override
    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    @Override
    public String getBarCode() {
        return barCode;
    }

    @Override
    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    @Override
    public Double getPricePerUnit() {
        return pricePerUnit;
    }

    @Override
    public void setPricePerUnit(Double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    @Override
    public Integer getId() {
        return productId;
    }

    @Override
    public void setId(Integer id) {
        productId = id;
    }

    //Made by Omar
    public boolean updateAvailableQuantity(Integer quantityToAdd){
        if(this.quantity + quantityToAdd < 0 || location == null)
            return false;
        this.quantity += quantityToAdd;
        return true;
    }
}
