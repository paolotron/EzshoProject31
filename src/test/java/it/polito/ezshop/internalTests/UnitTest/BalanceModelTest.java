package it.polito.ezshop.internalTests.UnitTest;

import it.polito.ezshop.model.BalanceModel;
import it.polito.ezshop.model.BalanceOperationModel;
import it.polito.ezshop.model.SaleModel;
import it.polito.ezshop.model.SaleTransactionModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

public class BalanceModelTest {

    @Test
    public void getTransactionByIdTest(){
        BalanceModel b = new BalanceModel();
        BalanceOperationModel credit = new BalanceOperationModel("CREDIT", 50., LocalDate.now());
        BalanceOperationModel debit = new BalanceOperationModel("DEBIT", -50., LocalDate.now());
        SaleTransactionModel sale = new SaleTransactionModel(new SaleModel());

        b.addBalanceOperation(credit);
        b.addBalanceOperation(debit);
        b.addBalanceOperation(sale);

        Optional<BalanceOperationModel> result = b.getTransactionById(credit.getBalanceId());
        Assertions.assertEquals(credit, result.orElse(null));
        result = b.getTransactionById(debit.getBalanceId());
        Assertions.assertEquals(debit, result.orElse(null));
        result = b.getTransactionById(sale.getBalanceId());
        Assertions.assertEquals(sale, result.orElse(null));
        result = b.getTransactionById(100);
        Assertions.assertNull(result.orElse(null));
        result = b.getTransactionById(-10);
        Assertions.assertNull(result.orElse(null));
    }

}
