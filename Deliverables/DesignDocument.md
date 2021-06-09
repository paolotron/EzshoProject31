# Design Document 


Authors: Paolo Rabino, Manuel Messina, Andrea Sindoni, Omar Gai

Date: 30/05/2021

Version: 2.0


# Contents

- [High level design](#package-diagram)
- [Low level design](#class-diagram)
- [Verification traceability matrix](#verification-traceability-matrix)
- [Verification sequence diagrams](#verification-sequence-diagrams)

# Instructions

The design must satisfy the Official Requirements document, notably functional and non functional requirements

# High level design 

```plantuml

    package it.polito.ezshop.exceptions
    package it.polito.ezshop.model
    package it.polito.ezshop.data
    package it.polito.ezshop.GUI
    
    it.polito.ezshop.GUI -> it.polito.ezshop.data
    it.polito.ezshop.data -> it.polito.ezshop.exceptions
    it.polito.ezshop.data -> it.polito.ezshop.model
```

## Package GUI
Package for interacting with data facade, contains GUI interfaces and the event controller used for registering the users actions. The GUI creates
views from the data received by the Data Package
## Package Data
Package consisting in  a single class that contains all the APIs used by the GUI, interacts with the model package to fulfill the functional requirements.
## Package Model
Package that contains all the classes and methods used to model the principal components of the EzShop system, the package also contains classes
for implementing persistency of datas by saving the main classes in a json file after every operation that would cause a critical failure if 
lost during a power outage or accidental power off of the system. At every restart the json file can be parsed and read to restore the system to the
last saved state.
## Package Exceptions
Package that contains all possible exceptions thrown by the data and model package's classes.

# Low level design

```plantuml

@startuml

scale 0.7
class EzShopModel {
loadEZShop()
reset()
getUserById()
getUserList()
deleteUserById()
login()
logout()
createUser()
getBalance()
createOrder()
payOrderFor()
payOrder()
recordOrderArrival()
checkAuthorization()
recordBalanceUpdate()
getCreditsAndDebits()
computeBalance()
getOrderList()
createCustomer()
getCustomerById()
modifyCustomer()
deleteCustomer()
getAllCustomer()
createCard()
attachCardToCustomer()
modifyPointsOnCard()
createProduct()
getProductByBarCode()
getAllProducts()
getProductById()
updateProduct()
updateProductPosition()
deleteProduct()
receiveCashPayment()
receiveCreditCardPayment()
returnCashPayment()
returnCreditCardPayment()
startSaleTransaction()
addProductToSale()
deleteProductFromSale()
applyDiscountRateToProduct()
applyDiscountRateToSale()
computePointsForSale()
endSaleTransaction()
deleteSaleTransaction()
startReturnTransaction()
returnProduct()
endReturnTransaction()
deleteReturnTransaction()
checkId()
checkOrderInputs()
updateUserRights()
recordOrderArrivalRFID()
addProductToSaleRFID()
deleteProductFromSaleRFID()
returnProductRFID()

UserList
LoyaltyCardMap
CustomerMap
CurrentlyLoggedUser
ProductMap
ActiveOrderMap
activeSaleMap
activeReturnMap
balance
writer
reader
}

class UserModel{
    Id
    Password
    Username
    Role
  
    checkPassword()
    getEnumRole()
    getRoleFromString()
    getStringFromRole()
}
class CustomerModel{
    Id
    Name
    loyaltyCard

    getPoints()
    setPoints()
    getCustomerCard()
    setCustomerCard()
}
class LoyaltyCardModel{
    Id
    Points
    updatePoints()
    checkCard()
}
class ProductTypeModel{
    Quantity
    Location
    Note
    ProductDescription
    BarCode
    PricePerUnit
    ProductId 
    RFIDset

    updateAvailableQuantity()
    checkBarCodeWithAlgorithm()
    checkRFID()
    valid_rfid_range()
    add_rfids()
}
class OrderModel{
    ProductCode
    orderId
    status
    pricePerUnit
    Quantity
    TotalPrice
    date
    
    getDateS()
    computeTotalPrice()
}
class SaleTransactionModel{
    DiscountRate
    Ticket
    beforeMoney
    RFIDmap

    deleteTicket()
    computePoint()
    getTicketNumber()
    setTicketNumber()
    getEntries()
    getRealMoney()
    computeCost()
    updateAmount()
}
class SaleModel{
    balanceOperationId
    Id
    Status
    ProductList
    saleDiscountRate
    RFIDset

    addProduct()
    removeProduct()
    setDiscountRateForProduct()
    setDiscountRateForSale()
    generateTicket()
    computeCost()
    closeTransaction()
}
class TicketModel{
    TicketEntriesList
    Id
    Amount
    Status
    Payment
    setNewPayment()
}
class TicketEntryModel{
    barCode
    productDescription
    amount
    pricePerUnit
    discountRate
    addAmount()
    removeAmount()
    computeCost()

}

class PaymentModel{
    Amount
    isReturn
}
class CashPaymentModel{
    Cash
    computeChange()
}
class CreditCardPaymentModel{
    Outcome
    sendPaymentRequestThroughAPI()
    validateCardWithLuhn()
}

class ReturnTransactionModel{
    Status
    ReturnedProductList
    SaleId
    Payment
    AmountToReturn
  
}
class ReturnModel{
    Id
    Sale 
    SaleId
    Status
    ProductList
    RfidMap
    returneAmount
    commit()
}
class BalanceModel{
    OrderTransactionMap
    ReturnTransactionMap
    SaleTransactionMap
    creditAndDebitsOperationMap
    BalanceOperationList
    
    getSaleTransactionById()
    getReturnTransactionById()
    getOrderTransactionById()
    getTransactionById()
    addBalanceOperation()
    getCreditsAndDebits()
    getAllBalanceOperations()
    computeBalance()
    checkAvailability()
    addOrderTransaction()
    addSaleTransactionModel()
    addReturnTransactionModel()
}
class BalanceOperationModel{
    balanceId
    operationType
    money
    date

    getDateS()
    setDateS()
    isReturn()
}
class OrderTransactionModel{
    Order
}
class JsonRead{
    ProductFile
    BalanceFile
    UserFile
    CustomerFile
    OrderFile
    LoyaltyFile
    
    parseLoyalty()
    parseOrders()
    parseCustomers()
    parseUsers()
    parseProductType()
    parseBalance()
}
class JsonWrite{
    ProductWriter
    BalanceWriter
    UserWriter
    CustomerWriter
    OrderWriter
    LoyaltyWriter
    ProductTypeFile
    BalanceFile
    UserFile
    CustomerFile
    OrderFile
    LoyaltyFile
    reset()
    writeOrders()
    writeProducts()
    writeUsers()
    writeBalance()
    writeLoyaltyCards()
    writeCustomers()
}
BalanceOperationModel <|-- SaleTransactionModel
BalanceOperationModel <|-- ReturnTransactionModel
TicketModel -- SaleTransactionModel
ReturnTransactionModel -- TicketModel
ReturnModel -- ReturnTransactionModel
PaymentModel <|-- CreditCardPaymentModel
PaymentModel <|-- CashPaymentModel
TicketModel -- PaymentModel
BalanceModel  -- "*" BalanceOperationModel
BalanceOperationModel <|-- OrderTransactionModel
OrderTransactionModel"0..1" -- OrderModel
CustomerModel"0..1" -- LoyaltyCardModel
EzShopModel -- "*" UserModel
EzShopModel -- "*" CustomerModel
EzShopModel -- BalanceModel
EzShopModel -- "*" ProductTypeModel
EzShopModel -- "*" OrderModel
EzShopModel -- "*" LoyaltyCardModel
EzShopModel -- ReturnModel
EzShopModel --  "*" SaleModel
SaleModel -- SaleTransactionModel
EzShopModel -- JsonRead
EzShopModel -- JsonWrite
ReturnModel -- TicketModel
ReturnModel -- PaymentModel
OrderModel -- ProductTypeModel
SaleModel -- "*" ProductTypeModel
ReturnModel -- "*" ProductTypeModel
TicketModel -- "*" TicketEntryModel
ReturnModel -- "*" TicketEntryModel


@enduml
```





# Verification traceability matrix

| FR ID | EzShop | Balance | BalanceOperation | OrderTransaction | SaleTransaction | ReturnTransaction | Order | Sale | Return | Customer | LoyalityCard | User | ProductType | Ticket | Payment | CashPayment | CreditCardPayment | JsonWrite | JsonRead |
| :----: | :----: | :----: |:----: | :----: | :----: | :----: | :----: | :----: | :----: | :----: | :----: | :----: | :----: | :----: | :----: | :----: | :----: | :----: | :----: |
| FR1 | X | | |  |  |  |  |  |  |  |  |X||||||X||
| FR3 | X || | | | | | | | | | X |X|||||X||
| FR4 | X |X|X|X|||X|||||X|X|||||X||
| FR5 |X|||||||||X|X|X||||||X||
| FR6 |X|X|X||X|X||X|X|||X|X|X||||X||
| FR7 |X|X|X||X|X||X|X|||X||X|X|X|X|X||
| FR8 |X|X|X|||||||||X||||||X||









# Verification sequence diagrams 


### SC1-1
``` plantuml

@startuml
title "Sequence Diagram 1"
actor User as U
participant EzShop
participant EzShopModel
participant ProductType as P
participant JsonWrite as JW

U->EzShop: 1: createProductType()
EzShop->EzShopModel: 2: createProduct()
EzShopModel -> P: 3: ProductType()
P -> EzShopModel: 4: return ProductType
EzShopModel->EzShopModel: 5 ProductTypeMap.put(Id, ProductType)
EzShopModel -> JW: 6: writeProducts()
EzShopModel->EzShop: 7: return ProductType
EzShop->P: 8: getId()
P->EzShop: 9: return Id
EzShop->U: 8: return ProductTypeId

@enduml

```

### SC1-3

``` plantuml
@startuml
title "Sequence Diagram 2"
actor User as U
participant EzShop
participant EzShopModel
participant ProductTypeModel as P
participant JsonWrite as JW

U->EzShop: 1: updateProduct()
EzShop->EzShopModel: 2: updateProduct()
EzShopModel->EzShopModel: 3: getProductById()
EzShopModel->EzShopModel: 4: PruductMap.remove()
EzShopModel->P: 5: setProductDescription()
EzShopModel->P: 6: setBarCode()
EzShopModel->P: 7: setPricePerUnit()
EzShopModel->P: 8: setNote()
EzShopModel->EzShopModel : 9: ProductMap.put()
EzShopModel -> JW: 10: writeProducts()
EzShopModel->EzShop: 8: return result(boolean)
EzShop->U: 9: return result(boolean)
@enduml

```

### SC2-1

``` plantuml
@startuml
title "Sequence Diagram 3"
actor User as U1
participant EzShop
participant EzShopModel
participant UserModel as U2
participant JsonWrite as JW

U1->EzShop: 1: createUser()
EzShop->EzShopModel: 2: createUser()
EzShopModel->U2: 3: UserModel()
U2->EzShopModel: 4: return UserModel
EzShopModel->EzShopModel: 5: UserList.Add(User)
EzShopModel -> JW: 6: writeUsers()
EzShopModel->EzShop: 7: return UserModel
EzShop->U2: 8: getId()
U2->EzShop: 9: return UserModelId
EzShop->U1: 10: return UserModelId
@enduml
```

### SC2-2

``` plantuml
@startuml
title "Sequence Diagram 4"
actor User as U1
participant EzShop
participant EzShopModel
participant JsonWrite as JW

U1->EzShop: 1: deleteUser()
EzShop->EzShopModel: 2: deleteUserById()
EzShopModel->EzShopModel: 3: getUserById()
EzShopModel->EzShopModel: 4: UserList.indexOf(UserModel)
EzShopModel->EzShopModel: 5: UserList.remove(UserModelIndex)
EzShopModel -> JW: 6: writeUsers()
EzShopModel->EzShop: 7: return result (boolean)
EzShop->U1: 8: return result(boolean)
@enduml
```

### SC3-1
``` plantuml
@startuml
title "Sequence Diagram 5"
actor User as U1
participant EzShop
participant EzShopModel
participant OrderModel as O

U1->EzShop: 1: issueOrder()
EzShop->EzShopModel: 2: createOrder()
EzShopModel->O: 3: OrderModel()
O->EzShopModel: 4 return OrderModel
EzShopModel->O: 5: setStatus("ISSUED")
EzShopModel->O: 6: getId()
O->EzShopModel: 7: return OrderModelId
EzShopModel->EzShopModel: 8: ActiveOrderMap.put(OrderModelId, OrderModel)
EzShopModel->EzShop: 8: return OrderModelId
EzShop->U1: 9: return OrderModelId
@enduml
```

### SC3-2
``` plantuml
@startuml
title "Sequence Diagram 6"
actor User as U1
participant EzShop
participant EzShopModel
participant OrderModel as O
participant OrderTransactionModel as O2
participant BalanceModel as Balance
participant JsonWrite as JW

U1->EzShop: 1: payOrder()
EzShop->EzShopModel: 2: payOrder()
EzShopModel->EzShopModel: 3: getBalance()
EzShopModel->EzShopModel: 4: activeOrderMap.get(OrderModelId)
EzShopModel->O: 5: getStatus()
O->EzShopModel: 6: return status
EzShopModel->O: 7: getTotalPrice()
O->EzShopModel: 8: return totalPrice
EzShopModel-> EzShopModel: 9: recordBalanceUpdate()
EzShopModel->O: 10: setStatus("PAYED")
EzShopModel->O: 11: getDate()
O->EzShopModel: 12: return date
EzShopModel->O2: 13: OrderTransactionModel()
O2->EzShopModel: 14: return OrderTransactionModel
EzShopModel->Balance: 15: addOrderTransaction()
EzShopModel -> JW: 16: writeBalance()
EzShopModel -> JW: 17: writeOrders()
EzShopModel->EzShop: 18: return result (boolean)
EzShop->U1: 19: return result (boolean)
@enduml
```

### SC3-3
``` plantuml
@startuml
title "Sequence Diagram 7"
actor User as U1
participant EzShop
participant EzShopModel
participant OrderModel as O
participant ProductTypeModel as P
participant JsonWrite as JW

U1->EzShop: 1: recordOrderArrival()
EzShop->EzShopModel: 2: recordOrderArrival()
EzShopModel->EzShopModel: 3: ActiveOrderMap.get()
EzShopModel->O: 4: getProductCode()
O->EzShopModel: 5: return productCode
EzShopModel->EzShopModel: 6: ProductMap.get()
EzShopModel->O: 7: getStatus()
O-> EzShopModel: 8: return status
EzShopModel->O: 9: setStatus("COMPLETED")
EzShopModel->O: 10: getQuantity()
O->EzShopModel: 11: return quantity
EzShopModel->P: 12: updateAvailableQuantity()
EzShopModel -> JW: 13: writeOrders()
EzShopModel->EzShop: 14: return result (boolean)
EzShop->U1: 15: return result (boolean)
@enduml
```

### SC4-1
``` plantuml
@startuml
title "Sequence Diagram 8"
actor User as U1
participant EzShop as E
participant EzShopModel as EM
participant CustomerModel as Customer 
participant JsonWrite as JW

U1->E: 1: defineCustomer()
E->EM: 2: createCustomer()
EM->Customer: 3: CustomerModel()
Customer->EM: 4: return CustomerModel
EM->EM: 5: CustomerMap.put(CustomerModel)
EM -> JW: 6: writeCustomers()
EM->Customer: 7: getId()
Customer->EM: 8: return CustomerModelId
EM->E: 9: return CustomerModelId
E->U1: 10: return CustomerModelId
@enduml
```

### SC5-1
``` plantuml
@startuml
title "Sequence Diagram 9"
actor User as U1
participant EzShop
participant EzShopModel
participant UserModel as U2

U1->EzShop: 1: login()
EzShop->EzShopModel: 2: login()
EzShopModel -> EzShopModel : 3: getUserModel()
EzShopModel -> U2 : 4: checkPassword()
EzShopModel->EzShopModel: 5: setCurrentlyLoggedUser(UserModel)
EzShopModel -> EzShop : 6: return UserModel
EzShop -> U1 : 6: return UserModel
@enduml
```

### SC6-1 and SC7-4

``` plantuml
@startuml
title "Sequence Diagram 10"
actor User
participant EzShop as E
participant EzShopModel as EM
participant SaleModel as S
participant ProductTypeModel as PM
participant SaleTransactionModel as SM
participant TicketModel as TM
participant CashPaymentModel AS CM
participant JsonWrite as JW
participant BalanceModel as B

User -> E: 1: startSaleTransaction()
E -> EM: 2: startSaleTransaction()
EM -> S: 3: SaleModel()
S -> EM: 4: return SaleModel
EM -> S: 5: getId()
EM -> EM: 6: ActiveSaleMap.put()
S -> EM: 7: return transactionId
EM -> E: 8: return transactionId
E -> User: 9: return transactionId
User -> E: 10: addProductToSale()
E -> EM: 11: addProductToSale()
EM -> EM: 12: productMap.get()
EM->PM: 13: updateQuantity()
EM -> EM: 14: activeSaleMap.get()
EM -> S: 15: addProduct()
EM -> E: 16: return result (boolean)
E -> User: 17: return result (boolean)
User -> E: 18: endSaleTransaction()
E -> EM: 19: endSaleTransaction()
EM->EM: 20: activeSaleMap.get()
EM -> S: 21: closeTransaction()
EM -> SM: 23: SalaTransactionModel()
SM -> EM: 24: return SalaTransactionModel
EM->EM: 25: acriveSaleMap.get()
EM->S : 26: setBalanceOperationId
EM->EM: 27: getBalance()
EM->B: 28: addSaleTransactionModel()
EM->E: 29: return result (boolean)
E->User: 30 return result (boolean)
User -> E: 31: receiveCashPayment()
E -> EM: 32: receiveCashPayment()
EM -> EM: 33: getBalance()
EM -> B: 34: getSaleTransactionById()
B -> EM: 35: return SaleTransactionModel
EM -> SM: 36: getTicket()
SM -> EM: 37: return TickeModel
EM->TM: 38: getAmount()
TM->EM: 39: return amount
EM -> CM: 40: CashPaymentModel()
CM -> EM: 41: return CashPaymentModel
EM -> CM: 42: computeChange()
CM -> EM: 43: return change (double)
EM->TM: 44: setStatus("PAYED")
EM -> SM: 45: setTicketPayment()
EM -> JW: 46: writeBalance()
EM->E: 47: return change (double)
E->User: 48: return change (double)

@enduml
```

### SC6-4 and SC7-1
```plantuml
@startuml
title "Sequence Diagram 10"
actor User
participant EzShop as E
participant EzShopModel as EM
participant SaleModel as S
participant ProductTypeModel as PM
participant SaleTransactionModel as SM
participant TicketModel as TM
participant CreditCardPaymentModel AS CM
participant JsonWrite as JW
participant BalanceModel as B

User -> E: 1: startSaleTransaction()
E -> EM: 2: startSaleTransaction()
EM -> S: 3: SaleModel()
S -> EM: 4: return SaleModel
EM -> S: 5: getId()
EM -> EM: 6: ActiveSaleMap.put()
S -> EM: 7: return transactionId
EM -> E: 8: return transactionId
E -> User: 9: return transactionId
User -> E: 10: addProductToSale()
E -> EM: 11: addProductToSale()
EM -> EM: 12: productMap.get()
EM->PM: 13: updateQuantity()
EM -> EM: 14: activeSaleMap.get()
EM -> S: 15: addProduct()
EM -> E: 16: return result (boolean)
E -> User: 17: return result (boolean)
User -> E: 18: endSaleTransaction()
E -> EM: 19: endSaleTransaction()
EM->EM: 20: activeSaleMap.get()
EM -> S: 21: closeTransaction()
EM -> SM: 23: SalaTransactionModel()
SM -> EM: 24: return SalaTransactionModel
EM->EM: 25: acriveSaleMap.get()
EM->S : 26: setBalanceOperationId
EM->EM: 27: getBalance()
EM->B: 28: addSaleTransactionModel()
EM->E: 29: return result (boolean)
E->User: 30 return result (boolean)
User -> E: 31: receiveCreditCardPayment()
E -> EM: 32: receiveCreditCardPayment()
EM -> EM: 33: getBalance()
EM -> B: 34: getSaleTransactionById()
B -> EM: 35: return SaleTransactionModel
EM -> SM: 36: getTicket()
SM -> EM: 37: return TickeModel
EM->TM: 38: getAmount()
TM->EM: 39: return amount
EM -> CM: 40: CreditCardPaymentModel()
CM -> EM: 41: return CreditCardPaymentModel
EM -> CM: 42: sendPaymentRequestThroughAPI()
CM -> EM: 43: return outcome (boolean)
EM->TM: 44: setStatus("PAYED")
EM -> SM: 45: setTicketPayment()
EM -> JW: 46: writeBalance()
JW -> EM: 47: return outcome(boolean)
EM->E: 48: return outcome (boolean)
E->User: 49: return change (boolean)

@enduml
```

### SC8-2 and SC10-2

```plantuml
@startuml
title "Sequence Diagram 10"
actor User
participant EzShop as E
participant EzShopModel as EM
participant SaleTransactionModel as SM
participant JsonWrite as JW
participant BalanceModel as B
participant ReturnModel as R
participant ReturnTransactionModel as RM
participant CashPaymentModel as CM
participant ProductTypeModel as PM
participant TicketModel as T

User -> E: 1: startReturnTransaction()
E -> EM: 2: startReturnTransaction()
EM -> R: 3: ReturnModel()
R -> EM: 4: return ReturnModel
EM -> EM: 5: activeReturnMap.put()
EM -> E: 6: return ReturnId
E -> User: 7: return ReturnId
User -> E: 8: returnProduct()
E -> EM: 9: returnProduct()
EM->EM: 10: activeReturnMap.get()
EM->R: 11: getSale()
R->EM: 12: return SaleTransactionModel
EM->SM: 13: getTicket()
SM->EM: 14: return Ticket
EM->T: 15: getTicketEntryModelList()
T->EM: 16: return TicketEntryModelList
EM->EM: 17: activeReturnMap.get()
EM->R: 18: addProducts()
R->EM: 19: return result (boolean)
EM->E: 20: return result (boolean)
E->User: 21: return result (boolean)
User -> E: 22: returnCashPayment()
E->EM: 23: returnCashPayment()
EM -> EM: 24: getBalance()
EM -> B: 25: getReturnTransactionById()
B->EM: 26: return ReturnTransactionModel
EM->RM: 27: getAmountToReturn()
RM->EM: 28: return amountToReturn
EM -> CM: 28: CashPaymentModel()
CM -> EM: 29: return CashPaymentModel
EM->RM: 30: setPayment()
EM->RM: 31: setStatus("PAYED")
EM->JW: 32: writeBalance()
EM->E: 33: return amountToReturn
E->User: 34 return amountToReturn

User -> E: 35: endReturnTransaction()
E -> EM: 36: endReturnTransaction()
EM -> EM: 37: activeReturnMap.get()
EM->R: 38: getSale()
R->EM: 39: return SaleTransactionModel
EM->SM: 40: getTicket()
SM->EM: 41: return Ticket
EM->T: 42: getTicketEntryModelList()
T->EM: 43: return TicketEntryModelList
EM->R: 44: commit()
EM->RM: 45: ReturnTransactionModel()
RM->EM: 46: return ReturnTransactionModel
EM -> B: 47: addReturnTransactionModel()
EM->JW: 48: writeBalance()
EM->EM: 49: activeReturnMap.remove()
EM->E: 50: return result (boolean)
E->User: 51: return result (boolean)


@enduml
```
### SC9-1

```plantuml
@startuml
title "Sequence Diagram 13"
actor User
participant EzShop
participant EzShopModel
participant BalanceModel as Balance
participant BalanceOperationModel as BM

User -> EzShop: 1: getCreditsAndDebits()
EzShop -> EzShopModel: 2:  getCreditsAndDebits()
EzShopModel -> Balance: 3:  getCreditsAndDebits()
Balance -> BM: 4: getDate()
BM -> Balance: 5: return Date
Balance -> EzShopModel: 6: return FilteredBalanceOperationList
EzShopModel -> EzShop: 7: return FilteredBalanceOperationList
EzShop -> User: 8: return FilteredBalanceOperationList

@enduml
```
