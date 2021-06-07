package it.polito.ezshop.model;

import it.polito.ezshop.data.ProductType;

import java.util.HashSet;

public class ProductTypeModel implements ProductType {
    Integer quantity;
    String location;
    String note;
    String productDescription;
    String barCode;
    Double pricePerUnit;
    Integer productId;
    HashSet<Integer> RFIDset;

    public ProductTypeModel() {}

    public ProductTypeModel(Integer productId, String description, String productCode, double pricePerUnit, String note){
        this.productDescription = description;
        this.barCode = productCode;
        this.pricePerUnit = pricePerUnit;
        this.note = note;
        this.productId = productId;
        this.quantity = 0;
        this.RFIDset = new HashSet<>();
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


    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public HashSet<Integer> getRFIDset() {
        return RFIDset;
    }

    public void setRFIDset(HashSet<Integer> RFIDset) {
        this.RFIDset = RFIDset;
    }

    public void add_rfids(String Rfid, int n){
        for(int i=Integer.parseInt(Rfid); i<Integer.parseInt(Rfid)+n; i++){
            RFIDset.add(i);
        }
    }

    public boolean valid_rfid_range(String Rfid, int n){
        int rfid = Integer.parseInt(Rfid);
        return RFIDset.stream().noneMatch((r)->rfid<= r&& r <=rfid+n);
    }

    //Made by Omar
    public boolean updateAvailableQuantity(Integer quantityToAdd){

        if(this.quantity + quantityToAdd < 0 || location == null)
            return false;
        this.quantity += quantityToAdd;
        return true;
    }


    /**
     * @param st BarCode
     * @return True if BarCode complies with https://www.gs1.org/services/how-calculate-check-digit-manually
     */
    public static boolean checkBarCodeWithAlgorithm(String st){
        if(st==null || !st.matches("^\\d{12,14}$"))
            return false;
        int tot = 0;
        for (int i = 0; i < st.length()-1; i++)
            tot+=Character.getNumericValue(st.charAt(i))*((st.length()-i)%2 == 0 ? 3:1);

        char left = Integer.toString(((int) tot/10 + 1) * 10 - tot).charAt(0);
        char right = st.charAt(st.length()-1);
        return left == right;
    }

    public static boolean checkRFID(String rfid){
        return (rfid != null && rfid.matches("^\\d{12}$"));
    }
}
