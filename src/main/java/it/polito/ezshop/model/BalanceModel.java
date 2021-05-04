package it.polito.ezshop.model;

import it.polito.ezshop.data.BalanceOperation;

import java.util.List;
import java.util.HashMap;
import java.util.Optional;

public class BalanceModel {
    HashMap<Integer, OrderTransaction> orderTransactionMap;
    HashMap<Integer, ReturnTransaction> returnTransactionMap;
    HashMap<Integer, SaleTransaction> saleTransactionMap;
    List<BalanceOperation> balanceOperationList;

    public SaleTransaction getSaleTransactionById(Integer id){
        return saleTransactionMap.get(id);
        /* SaleTransaction saleT = saleTransaction.get(id)
        if(saleT == null)
            throw new invalidBalanceOperationId();
         return saleT;
         */
    }

    public ReturnTransaction getReturnTransactionById(Integer id){
        return returnTransactionMap.get(id);
    }

    public OrderTransaction getOrderTransactionById(Integer id){
        return orderTransactionMap.get(id);
    }

    //TODO: test this code
    public Optional<BalanceOperation> getTransactionById(Integer id){
        return balanceOperationList.stream().filter((balanceOperation) -> balanceOperation.getBalanceId() == id).findFirst();
    }

    public List<BalanceOperation> getAllBalanceOperations(){
        return balanceOperationList;
    }

    public HashMap<Integer, OrderTransaction> getAllOrderTransactions(){
        return orderTransactionMap;
    }
    public HashMap<Integer, SaleTransaction> getAllSaleTransactions(){
        return saleTransactionMap;
    }
    public HashMap<Integer, ReturnTransaction> getAllReturnTransactions(){
        return returnTransactionMap;
    }


}
