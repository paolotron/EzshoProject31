# Unit Testing Documentation

Authors:

Date:

Version:

# Contents

- [Black Box Unit Tests](#black-box-unit-tests)




- [White Box Unit Tests](#white-box-unit-tests)


# Black Box Unit Tests

    <Define here criteria, predicates and the combination of predicates for each function of each class.
    Define test cases to cover all equivalence classes and boundary conditions.
    In the table, report the description of the black box test case and (traceability) the correspondence with the JUnit test case writing the 
    class and method name that contains the test case>
    <JUnit test classes must be in src/test/java/it/polito/ezshop   You find here, and you can use,  class TestEzShops.java that is executed  
    to start tests
    >

 ### **Class *UserModel* - method *Constructor***



**Criteria for method *Constructor*:**
 - Role is valid





**Predicates for method *Constructor*:**

| Criteria | Predicate |
| -------- | --------- |
|     Role  is valid   |  "Shop Manager"         |
|          |       "Administrator"    |
|          |       "Cashier"    |
|          |       Any other string    |




**Combination of predicates**:


| Role is valid | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
|"Cashier"|valid|Constructor("Paolo", "Password", "Cashier")|UserModelTest/TestRole|
|"Administrator"|valid|Constructor("Paolo", "Password", "Administrator")|UserModelTest/TestRole|
|"Shop Manager"|valid|Constructor("Paolo", "Password", "ShopManager")|UserModelTest/TestRole|
|"sopManag"|invalid|Constructor("Paolo", "Password", "SsopManag")|UserModelTest/TestRole|



### **Class *UserModel* - method *checkPassword***



**Criteria for method *checkPassword*:**


- Password 





**Predicates for method *checkPassword*:**

| Criteria | Predicate |
| -------- | --------- |
|     Password is empty     |   null        |
|          |      ""     |
|    |     "Previously set Password"      |
|          |     anything else      |





**Combination of predicates**:


| Password is the one set | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
|""|Invalid|checkPassword("")|UserModelTest/testInvalidPassword|
|null|Invalid|checkPassword(null)|UserModelTest/testInvalidPassword|
|"Previously set password"|Valid|User.setPassword("Password")<br />checkPassword("Password")->True|UserModelTest/testValidPassword|
|"Anything else"|Valid|User.setPassword("Password")<br />checkPassword("else")->False|UserModelTest/testValidPassword|





### **Class *CreditCardPayment* - method *validateCardWithLuhn***



**Criteria for method *validateCardWithLuhn*:**

- CreditCardNumber

**Predicates for method *validateCardWithLuhn*:**

| Criteria | Predicate |
| -------- | --------- |
|     CreditCardNumber     |      Luhn digit is valid     |
|          |       Luhn digist is not valid    |
|         |     Number is empty

**Combination of predicates**:


| CreditCardNumber | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
|Luhn digit is valid| valid |(5265807692)->True|testCorrectLuhn|
|Luhn digit is valid| valid |(6214838176)->True|testCorrectLuhn|
|Luhn digit is not valid| valid|(6234838176)->False|testWrongLuhn|
|Luhn digit is not valid| valid|(51658026)->False|testWrongLuhn|
|Luhn digit is not valid| valid|(ABC)->False|testWrongLuhn|

### **Class *CreditCardPayment* - method *sedPaymentRequestThroughAPI***
**Criteria for method *sedPaymentRequestThroughAPI*:**
- CardNumber
- Amount
- Balance

**Predicates for method *sedPaymentRequestThroughAPI*:**

| Criteria | Predicate |
| -------- | --------- |
|     CardNumber     |     CardNumber is invalid      |
|          |      CardNumber is registered      |
|    Amount      |     Amount > Credit card balance      |
|          |      Amount <= Credit card balance     |





**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|     Amount     |        -999, credit card balance         |
|          |          credit card balance, 999       |



**Combination of predicates**:


| CardNumber | Amount | CreditCardBalance | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|-------|
|""|*|*|invalid|("")->InvalidCreditCardException|testInvalidPaymentWithAPI|
|null|*|*|invalid|(null)->InvalidCreditCardException|testInvalidPaymentWithAPI|
|invalid|*|*|invalid|(1234)->InvalidCreditCardException|testInvalidPaymentWithAPI|
|5265807692|20|30|valid|setAmount(20)</br>writeToFile(30)</br>(5265807692)->true|testCorrectPaymentWithAPI|
|5265807692|20|10|valid|setAmount(20)</br>writeToFile(10)</br>(5265807692)->false|testFailPaymentWithAPI|
|6214838176|20|/|valid|setAmount(20)</br>(6214838176)->false|testFailPaymentWithAPI|

### **Class *LoyaltyCardModel* - method *addPoints***



**Criteria for method *name*:addPoints**


- sign of Points to add
- total points


**Predicates for method *name*:add Points**

| Criteria | Predicate |
| -------- | --------- |
|    sign of Points to add      | (minint, 0]  |
|          |  (0, maxint)  |
|   total points    |   (minint, 0) |
|                   |    [0, maxpoints]    |
|   | (maxpoints, maxint)  |


**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|     sign of Points     |   -1, 0, 1    |
|   total points    |   -1, 0, maxpoints, maxpoints+1  |


**Combination of predicates**:


| Sign of points to add | total points  | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|------|
| | (minint, 0) | |||
| | [0, maxpoints] ||||
| | (maxpoints, maxint) ||||
||||||
||||||
### **Class *class_name* - method *name***



**Criteria for method *name*:**


-
-





**Predicates for method *name*:**

| Criteria | Predicate |
| -------- | --------- |
|          |           |
|          |           |
|          |           |
|          |           |





**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |
|          |                 |



**Combination of predicates**:


| Criteria 1 | Criteria 2 | ... | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|-------|
|||||||
|||||||
|||||||
|||||||
|||||||
### **Class *class_name* - method *name***



**Criteria for method *name*:**


-
-





**Predicates for method *name*:**

| Criteria | Predicate |
| -------- | --------- |
|          |           |
|          |           |
|          |           |
|          |           |





**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |
|          |                 |



**Combination of predicates**:


| Criteria 1 | Criteria 2 | ... | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|-------|
|||||||
|||||||
|||||||
|||||||
|||||||
### **Class *class_name* - method *name***



**Criteria for method *name*:**


-
-





**Predicates for method *name*:**

| Criteria | Predicate |
| -------- | --------- |
|          |           |
|          |           |
|          |           |
|          |           |





**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |
|          |                 |



**Combination of predicates**:


| Criteria 1 | Criteria 2 | ... | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|-------|
|||||||
|||||||
|||||||
|||||||
|||||||
### **Class *class_name* - method *name***



**Criteria for method *name*:**


-
-





**Predicates for method *name*:**

| Criteria | Predicate |
| -------- | --------- |
|          |           |
|          |           |
|          |           |
|          |           |





**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |
|          |                 |



**Combination of predicates**:


| Criteria 1 | Criteria 2 | ... | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|-------|
|||||||
|||||||
|||||||
|||||||
|||||||
### **Class *class_name* - method *name***



**Criteria for method *name*:**


-
-





**Predicates for method *name*:**

| Criteria | Predicate |
| -------- | --------- |
|          |           |
|          |           |
|          |           |
|          |           |





**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |
|          |                 |



**Combination of predicates**:


| Criteria 1 | Criteria 2 | ... | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|-------|
|||||||
|||||||
|||||||
|||||||
|||||||
### **Class *class_name* - method *name***



**Criteria for method *name*:**


-
-





**Predicates for method *name*:**

| Criteria | Predicate |
| -------- | --------- |
|          |           |
|          |           |
|          |           |
|          |           |





**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |
|          |                 |



**Combination of predicates**:


| Criteria 1 | Criteria 2 | ... | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|-------|
|||||||
|||||||
|||||||
|||||||
|||||||


# White Box Unit Tests

### Test cases definition

    <JUnit test classes must be in src/test/java/it/polito/ezshop>
    <Report here all the created JUnit test cases, and the units/classes under test >
    <For traceability write the class and method name that contains the test case>


| Unit name | JUnit test case |
|--|--|
|||
|||
||||

### Code coverage report

    <Add here the screenshot report of the statement and branch coverage obtained using
    the Eclemma tool. >


### Loop coverage analysis

    <Identify significant loops in the units and reports the test cases
    developed to cover zero, one or multiple iterations >

|Unit name | Loop rows | Number of iterations | JUnit test case |
|---|---|---|---|
|||||
|||||
||||||



