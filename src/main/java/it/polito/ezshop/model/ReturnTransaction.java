package it.polito.ezshop.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class ReturnTransaction extends BalanceOperationModel{
    ArrayList<ProductTypeModel> productTypeList;
    //Double returnedAmount; Maybe useless because inherit the attribute money
    //TODO: Add an attribute of type Ticket
    //TODO: Add methods getTicket(), deleteTicket()

    public ReturnTransaction(Double amount, LocalDate date){
        super("return", amount, date);
        productTypeList = new ArrayList<>();
    }

}
