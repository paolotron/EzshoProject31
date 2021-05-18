# Design Document 


Authors: Paolo Rabino, Manuel Messina, Andrea Sindoni, Omar Gai

Date: 30/05/2021

Version: 1.0


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
    Name
    Role
    Password
    checkPassword()
}
class CustomerModel{
    Id
    Name
}
class LoyaltyCardModel{
    Id
    Points
    updatePoints()
}
class ProductTypeModel{
    BarCode
    Description
    PricePerUnit
    Note
    DiscountRate
    Quantity
    Position
    updateAvailableQuantity()
    checkBarCodeWithAlgorithm()
    
}
class OrderModel{
    ProductCode
    orderId
    status
    pricePerUnit
    Quantity
    TotalPrice
    
    computeTotalPrice()
}
class SaleTransactionModel{
    PaymentType
    DiscountRate
    BalanceOperationId
    Ticket
    deleteTicket()
    computePoints()
}
class SaleModel{
    Id
    Status
    ProductList
    saleDiscountRate
    balanceOperationId
    Ticket
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
    setPayment()
    getPayment()
}
class TicketEntryModel{
    barCode
    productDescription
    amount
    pricePerUnit
    discountRate
    addAmount()
    computeCost()
    removeAmount()
}

class PaymentModel{
    Amount
    isReturn()
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
    ReturnedProductList
    amountToReturn
    Payment
    status
}
class ReturnModel{
    ProductTypeList
    Status
    Sale
    Id
    returnedAmount
    commit()
}
class BalanceModel{
    ReturnTransactionMap
    SaleTransactionMap
    OrderTransactionMap
    BalanceOperationList
    balanceAmount
    getReturnTransactionById()
    getTransactionById()
    addBalanceOperation()
    getCreditsAndDebits()
    getAllBalanceOperations()
    computeBalance()
    getSaleTransactionById()
    getOrderTransactionById()
    checkAvailability()
    addOrderTransaction()
    addSaleTransactionModel()
    addReturnTransactionModel()
}
class BalanceOperationModel{
    operationType
    money
    balanceId
    date
}
class OrderTransactionModel{
    Order
}
class JsonRead{
    LoyaltyFile
    OrderFile
    CustomerFile
    UserFile
    BalanceFile
    ProductFile
    parseLoyalty()
    parseOrders()
    parseCustomers()
    parseUsers()
    parseProductType()
    parseBalance()
}
class JsonWrite{
    LoyaltyFile
    OrderFile
    CustomerFile
    UserFile
    BalanceFile
    LoyaltyWriter
    ProductTypeFile
    OrderWriter
    CustomerWriter
    UserWriter
    BalanceWriter
    ProductWriter
    reset()
    writeOrders()
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
participant Data
participant EzShop
participant Sale
participant ProductType
participant SaleTransaction
participant Ticket
participant CashPayment
participant JsonWrite as JW

User -> Data: 1: startSaleTransaction()
Data -> EzShop: 2: createSale()
EzShop -> Sale: 3: Sale()
Sale -> EzShop: 4: return Sale
EzShop -> Sale: 5: getId()
Sale -> EzShop: 6: return transactionId
EzShop -> EzShop: 7: ActiveSaleMap.add(Id, Sale)
EzShop -> Data: 8: return transactionId
Data -> User: 9: return transactionId
User -> Data: 10: addProductToSale()
Data -> EzShop: 11: ActiveSaleMap.get(Id)
EzShop -> Data: 12: return Sale
Data -> Sale: 13: addProduct()
Sale -> EzShop: 14: getProductByBarCode()
EzShop -> Sale: 15: return ProductType
Sale -> Sale: 16: ProductList.push(ProductType)
Sale -> ProductType: 17: setQuantity()
Sale -> Data: 18: return outcome (boolean)
Data -> User: 19: return outcome (boolean)
User -> Data: 20: closeSaleTransaction()
Data -> Sale: 21: closeTransaction()
Sale -> SaleTransaction: 22: SaleTransaction()
SaleTransaction -> Ticket: 23: Ticket()
Ticket -> SaleTransaction: 24: return Ticket
SaleTransaction -> Sale: 25: return SaleTransaction
Sale -> EzShop: 26: getBalance()
EzShop -> Sale: 27: return Balance
Sale -> Balance: 28: SaleTransactionMap.add(Id, SaleTransaction)
Sale -> Balance: 29: BalanceOperationList.push(SaleTransaction)
Sale -> Data: 30: return outcome(boolean)
Data -> JW: 31: enableWrite()
Data -> JW: 32: writeAll()
Data -> JW: 33: disableWrite()
Data -> User: 34: return outcome(boolean)
User -> Data: 35: receiveCashPayment()
Data -> EzShop: 36: getBalance()
EzShop -> Data: 37: return Balance
Data -> Balance: 38: getSaleTransactionById()
Balance -> Data: 39: return SaleTransaction
Data -> SaleTransaction: 40: getTicket()
SaleTransaction -> Data: 41: return Ticket
Data -> Ticket: 42: setPayment()
Ticket -> CashPayment: 43: CashPayment()
CashPayment -> Ticket: 44: return CashPayment
Ticket -> Ticket: 45: setStatus()
Ticket -> CashPayment: 46: computeChange()
CashPayment -> Data: 47: return Change
Data -> JW: 48: enableWrite()
Data -> JW: 49: writeBalance()
Data -> JW: 50: disableWrite()
Data -> User: 51: return Change
User -> Data: 52: getSaleTicket()
Data -> EzShop: 53: getBalance()
EzShop -> Data: 54: return Balance
Data -> Balance: 55: getSaleTransactionById()
Balance -> Data: 56: return SaleTransaction
Data -> SaleTransaction: 57: getTicket()
SaleTransaction -> User: 58: return Ticket

@enduml
```

### SC6-4 and SC7-1
```plantuml
@startuml
scale 0.75
title "Sequence Diagram 11"
actor User
participant Data
participant EzShop
participant Sale
participant ProductType
participant SaleTransaction
participant Ticket
participant CashPayment
participant JsonWrite as JW

User -> Data: 1: startSaleTransaction()
Data -> EzShop: 2: createSale()
EzShop -> Sale: 3: Sale()
Sale -> EzShop: 4: return Sale
EzShop -> Sale: 5: getId()
Sale -> EzShop: 6: return transactionId
EzShop -> EzShop: 7: ActiveSaleMap.add(Id, Sale)
EzShop -> Data: 8: return transactionId
Data -> User: 9: return transactionId
User -> Data: 10: addProductToSale()
Data -> EzShop: 11: ActiveSaleMap.get(Id)
EzShop -> Data: 12: return Sale
Data -> Sale: 13: addProduct()
Sale -> EzShop: 14: getProductByBarCode()
EzShop -> Sale: 15: return ProductType
Sale -> Sale: 16: ProductList.push(ProductType)
Sale -> ProductType: 17: setQuantity()
Sale -> Data: 18: return outcome (boolean)
Data -> User: 19: return outcome (boolean)
User -> Data: 20: closeSaleTransaction()
Data -> Sale: 21: closeTransaction()
Sale -> SaleTransaction: 22: SaleTransaction()
SaleTransaction -> Ticket: 23: Ticket()
Ticket -> SaleTransaction: 24: return Ticket
SaleTransaction -> Sale: 25: return SaleTransaction
Sale -> EzShop: 26: getBalance()
EzShop -> Sale: 27: return Balance
Sale -> Balance: 28: SaleTransactionMap.add(Id, SaleTransaction)
Sale -> Balance: 29: BalanceOperationList.push(SaleTransaction)
Sale -> Data: 30: return outcome(boolean)
Data -> JW: 31: enableWrite()
Data -> JW: 32: writeAll()
Data -> JW: 33: disableWrite()
Data -> User: 34: return outcome(boolean)
User -> Data: 28: receiveCreditCardPayment()
Data -> EzShop: 29: validateCardWithLuhn()
EzShop -> Data: 30: return outcome(boolean)
Data -> EzShop: 31: getBalance()
EzShop -> Data: 32: return Balance
Data -> Balance: 33: getSaleTransactionById()
Balance -> Data: 34: return SaleTransaction
Data -> SaleTransaction: 35: getTicket()
SaleTransaction -> Data: 36: return Ticket
Data -> Ticket: 37: setPayment()
Ticket -> CreditCardPayment: 38: CreditCardPayment()
CreditCardPayment -> Ticket: 39: return CreditCardPayment
Ticket -> CreditCardPayment: 40: sendPaymentRequestThroughAPI()
CreditCardPayment -> CreditCardPayment: 41: setOutcome()
CreditCardPayment -> Ticket: 42: return outcome(boolean)
Ticket -> Ticket: 43: setStatus()
Ticket -> Data: 44: return outcome(boolean)
EzShop -> JW: 45: enableWrite()
EzShop -> JW: 46: writeBalance()
EzShop -> JW: 47: disableWrite()
Data -> User: 48: return outcome(boolean)
User -> Data: 49: computePointsForSale()
Data -> EzShop: 50: getBalance()
EzShop -> Data: 51: return Balance
Data -> Balance: 52: getSaleTransactionById()
Balance -> Data: 53: return SaleTransaction
Data -> SaleTransaction: 54: computePoints()
SaleTransaction -> User: 55: return points
User -> Data: 56: modifyPointsOnCard()
Data -> EzShop: 57: getCustomerById()
EzShop -> Data: 58: return Customer
Data -> Customer: 59: getLoyalityCard()
Customer -> Data: 60: return LoyalityCard
Data -> LoyalityCard: 61: addPoints()
LoyalityCard -> Data: 62: return outcome (boolean)
EzShop -> JW: 63: enableWrite()
EzShop -> JW: 64: writeCustomerList()
EzShop -> JW: 65: disableWrite()
Data -> User: 66: return outcome(boolean)
User -> Data: 67: getSaleTicket()
Data -> EzShop: 68: getBalance()
EzShop -> Data: 69: return Balance
Data -> Balance: 70: getSaleTransactionById()
Balance -> Data: 71: return SaleTransaction
Data -> SaleTransaction: 72: getTicket()
SaleTransaction -> User: 73: return Ticket

@enduml
```

### SC8-2 and SC10-2

```plantuml
@startuml
title "Sequence Diagram 12"
actor User
participant Data
participant EzShop
participant Return
participant JsonWrite as JW

User -> Data: 1: startReturnTransaction()
Data -> EzShop: 2: createReturnFromTicket()
EzShop -> Balance: 3: getSaleTransactionById()
Balance -> EzShop: 4: return SaleTransaction
EzShop -> SaleTransaction: 5: getTicket()
SaleTransaction -> EzShop: 6: return Ticket
EzShop -> Return: 7: Return()
Return -> EzShop: 8: return Return
EzShop -> EzShop: : ActiveReturnMap.add(Return)
EzShop -> Return: 9: getId()
Return -> User: 10: return Id
User -> Data: 11: returnProduct()
Data -> EzShop: 12: activeReturnMap.get()
EzShop -> Data: 13: return Return
Data -> Return: 14: addProduct()
Return -> Data: 15: return outcome
Data -> ProductType: 16: setQuantity()
Data -> User: 17: return outcome
User -> Data: 18: returnCashPayment()
Data -> EzShop: 19: activeReturnMap.get()
EzShop -> Data: 20:return Return
Data -> Return: 21: setPayment()
Return -> CashPayment: 22: CashPayment()
CashPayment -> Return: 23: return CashPayment
Return -> User: 24: return amount
User -> Data: 25: endReturnTransaction()
Data -> EzShop: 26: activeReturnMap.get()
EzShop -> Data: 27:return Return
Data -> Return: 28: closeTransaction()
Return -> ReturnTransaction: 29: ReturnTransaction()
ReturnTransaction -> Data: 30: return ReturnTransaction
Data -> EzShop: 31: getBalance()
EzShop -> Data: 32: return Balance
Data -> Balance: 33: returnTransactionMap.add()
Data -> Balance: 34: BalanceOperationList.push()
Data -> JW: 35: enableWrite()
Data -> JW: 36: writeAll()
Data -> JW: 37: disableWrite()
Data -> User: 38: return outcome(boolean)
@enduml
```
### SC9-1

```plantuml
@startuml
title "Sequence Diagram 13"
actor User
participant Data
participant EzShop
participant Balance
participant BalanceOperation

User -> Data: 1: getCreditsAndDebits()
Data -> EzShop: 2: getBalance()
EzShop -> Data: 3: return Balance
Data -> Balance: 4: getCreditsAndDebits()
Balance -> BalanceOperation: 5: getDate()
BalanceOperation -> Balance: 6: return Date
Balance -> User: 7: return FilteredBalanceOperationList

@enduml
```
