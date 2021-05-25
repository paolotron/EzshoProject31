# Integration and API Test Documentation

Authors:

Date:

Version:

# Contents

- [Dependency graph](#dependency graph)

- [Integration approach](#integration)

- [Tests](#tests)

- [Scenarios](#scenarios)

- [Coverage of scenarios and FR](#scenario-coverage)
- [Coverage of non-functional requirements](#nfr-coverage)



# Dependency graph 

```plantuml
@startuml
scale 2/3
UserModel -- Roles
CustomerModel -- LoyaltyCardModel
TicketModel -- TicketEntryModel
TicketModel -- PaymentModel
BalanceModel -- BalanceOperationModel
BalanceModel -- ReturnTransactionModel
BalanceModel -- OrderTransactionModel
BalanceModel -- SaleTransactionModel
OrderTransactionModel -- OrderModel
SaleModel -- TicketModel
SaleModel -- TicketEntryModel
SaleModel -- ProductTypeModel
SaleTransactionModel -- TicketModel
SaleTransactionModel -- PaymentModel
ReturnTransactionModel -- PaymentModel
ReturnTransactionModel -- ReturnModel
ReturnTransactionModel -- TicketEntryModel
ReturnModel -- SaleTransactionModel
ReturnModel -- TicketEntryModel
ReturnModel -- ProductTypeModel
JsonRead -- BalanceModel
JsonRead -- ProductTypeModel
JsonRead -- UserModel
JsonRead -- CustomerModel
JsonRead -- OrderModel
JsonRead -- LoyaltyCardModel
JsonWrite -- BalanceModel
JsonWrite -- ProductTypeModel
JsonWrite -- UserModel
JsonWrite -- CustomerModel
JsonWrite -- OrderModel
JsonWrite -- LoyaltyCardModel
EzShopModel -- JsonRead
EzShopModel -- JsonWrite
EzShopModel -- CustomerModel
EzShopModel -- LoyaltyCardModel
EzShopModel -- ProductTypeModel
EzShopModel -- UserModel
EzShopModel -- BalanceModel
EzShopModel -- OrderModel
EzShopModel -- SaleModel
EzShopModel -- ReturnModel
EzShopModel -- ReturnTransactionModel
EzShopModel -- SaleTransactionModel
EzShopModel -- CashPaymentModel
EzShopModel -- CreditCardPaymentModel
EzShopModel -- Roles


class UserModel { 
       getRole()
       setRole()
       getEnumRole()
       getRoleFromString()
       getStringFromRole()
}

class CustomerModel { 
               getCustomerCard()
	       setCustomerCard()
	       getPoints()
	       setPoints()
	       getLoyaltyCard()
		   setLoyaltyCard()
}

class TicketModel {
	TicketModel()
	getPayment()
	setPayment()
	setNewPayment()
	getTicketEntryModelList()
	setTicketEntryModelList()
}

class BalanceModel {
	BalanceModel()
	getOrderTransactionMap()
	setOrderTransactionMap()
	getReturnTransactionMap()
	setReturnTransactionMap()
	getSaleTransactionMap()
	setSaleTransactionMap()
	getCreditAndDebitsOperationMap()
	setCreditAndDebitsOperationMap()
	getSaleTransactionMapById()
	getReturnTransactionById()
	getOrderTransactionById()
	getTransactionById()
	getAllBalanceOperations()
	addBalanceOperation()
	getCreditsAndDebits()
	computeBalance()
	checkAvailability() 
	addOrderTransaction()
	addSaleTransactionModel()
	addReturnTransactionModel()
}

class OrderTransactionModel {
	OrderTransactionModel()
}


class SaleModel {
	getProductList()
	setProductList()
	addProduct()
	removeProduct()
	setDiscountRateForProduct()
	setDiscountRateForSale()
	computeCost()
	generateTicket()
}

class SaleTransactionModel {
	SaleTransactionModel()
	setTicketPayment()
	getTicket()
	setTicket()
	deletTicket()
	getTicketNumber()
	setTicketNumber()
	getEntries()
	setEntries()
	getPrice()
	setPrice()
	computeCost()
	updateAmount()
}

class ReturnTransactionModel {
	ReturnTransactionModel()
	getReturnedProductList()
	setReturnedProductList()
	getPayment()
	setPayment()
}

class ReturnModel {
	ReturnModel()
	getProductList()
	setProductList()
	getSale()
	setSale()
	commit()	
}

class JsonRead {	
	parseBalance()
	parseProductType()
	parseUsers()
	parseCustomers()
	parseOrders()
	parseLoyalty()
}

class JsonWrite {
	writeProducts()
	writeOrders()
	writeUsers()
	writeCustomers()
	writeBalance()
	writeLoyaltyCards()
}

class EzShopModel {
	loadEzShop()
	getUserById()
	getUserList()
	deleteUserById()
	getBalance()
	checkAuthorization()
	getProductById()
	checkString()
	checkDouble()
	checkId()
	checkOrderInputs()
	EzShop()
	reset()
	createUser()
	deleteUser()
	getAllUsers()
	getUser()
	updateUserRights()
	login()
	logout()
	createProductType()
	updateProduct()
	deleteProductType()
	getAllProductTypes()
	getProductTypeByBarCode()
	getProductTypesByDescription()
	updateQuantity()
	updatePosition()
	issueOrder()
	payOrderFor()
	payOrder()
	recordOrderArrival()
	getAllOrders()
	defineCustomer()
	modifyCustomer()
	deleteCustomer()
	getCustomer()
	getAllCustomers()
	createCard()
	attachCardToCustomer()
	modifyPointsOnCard()
	startSaleTransaction()
	addProductToSale()
	deleteProductFromSale()
	applyDiscountRateToProduct()
	applyDiscountRateToSale()
	computePointsForSale()
	endSaleTransaction()
	deleteSaleTransaction()
	getSaleTransaction()
	startReturnTransaction()
	returnProduct()
	endReturnTransaction()
	deleteReturnTransaction()
	receiveCashPayment()
	receiveCreditCardPayment()
	returnCashPayment()
	returnCreditCardPayment()
	recordBalanceUpdate()
	getCreditsAndDebits()
	computeBalance()
}

@enduml
```
     
# Integration approach

    <Write here the integration sequence you adopted, in general terms (top down, bottom up, mixed) and as sequence
    (ex: step1: class A, step 2: class A+B, step 3: class A+B+C, etc)> 
    <Some steps may  correspond to unit testing (ex step1 in ex above), presented in other document UnitTestReport.md>
    <One step will  correspond to API testing>
    
Integration was done in a bottom up approach:
1. Unit Testing of leaf classes: 
2. Integration Testing of Balance Class and Persistence classes
3. API testing on the main facade class EzShopModel


#  Tests

   <define below a table for each integration step. For each integration step report the group of classes under test, and the names of
     JUnit test cases applied to them> JUnit test classes should be here src/test/java/it/polito/ezshop
   , , , ,
   , , , , 
## Step 1
| Classes  | JUnit test cases |
|----|----|
| UserModel | UnitTest/UserModelTest |
| TicketModel | UnitTest/TicketModelTest |
| SaleTransactionModel | UnitTest/SaleTransactionModelTest |
| CreditCardPayment | UnitTest/CreditCardPaymentTest |
| ProductTypeModel | UnitTest/ProductTypeModelTest |
| OrderModel | UnitTest/OrderModelTest |
| OrderTransactionModel | UnitTest/OrderTransactionModelTest |
| LoyaltyCardModel | UnitTest/LoyaltyCardModelTest |
| BalanceOperationModel | UnitTest/BalanceOperationModelTest |
## Step 2
| Classes  | JUnit test cases |
|----|----|
|BalanceModel|IntegrationTest/BalanceTest|
|JsonRead|IntegrationTest/PersistenceTest|
|JsonWrite|IntegrationTest/PersistenceTest|
|SaleModel|IntegrationTest/SaleModelTest|
## Step 3

| Classes  | JUnit test cases |
|----|----|
|EZShop|APITest/FR1_UserTests|
|EZShop|APITest/FR3_ProductTypeTest|
|EZShop|APITest/FR4_OrderTests|
|EZShop|APITest/FR5_CustomerTests|
|EZShop|APITest/FR6_SaleTransactionTest|
|EZShop|APITest/FR7_PaymentTests|
|EZShop|APITest/FR8_BalanceTest|




# Scenarios


<If needed, define here additional scenarios for the application. Scenarios should be named
 referring the UC in the OfficialRequirements that they detail>

## Scenario UCx.y

| Scenario |  name |
| ------------- |:-------------:| 
|  Precondition     |  |
|  Post condition     |   |
| Step#        | Description  |
|  1     |  ... |  
|  2     |  ... |



# Coverage of Scenarios and FR


<Report in the following table the coverage of  scenarios (from official requirements and from above) vs FR. 
Report also for each of the scenarios the (one or more) API JUnit tests that cover it. >




| Scenario ID | Functional Requirements covered | JUnit  Test(s) | 
| ----------- | ------------------------------- | ----------- | 
|  ..         | FRx                             |             |             
|  ..         | FRy                             |             |             
| ...         |                                 |             |             
| ...         |                                 |             |             
| ...         |                                 |             |             
| ...         |                                 |             |             



# Coverage of Non Functional Requirements


<Report in the following table the coverage of the Non Functional Requirements of the application - only those that can be tested with automated testing frameworks.>


### 

| Non Functional Requirement | Test name |
| -------------------------- | --------- |
|                            |           |


