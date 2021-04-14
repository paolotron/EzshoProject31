# Requirements Document 

Authors:

Date:

Version:

# Contents

- [Essential description](#essential-description)
- [Stakeholders](#stakeholders)
- [Context Diagram and interfaces](#context-diagram-and-interfaces)
	+ [Context Diagram](#context-diagram)
	+ [Interfaces](#interfaces) 
	
- [Stories and personas](#stories-and-personas)
- [Functional and non functional requirements](#functional-and-non-functional-requirements)
	+ [Functional Requirements](#functional-requirements)
	+ [Non functional requirements](#non-functional-requirements)
- [Use case diagram and use cases](#use-case-diagram-and-use-cases)
	+ [Use case diagram](#use-case-diagram)
	+ [Use cases](#use-cases)
    	+ [Relevant scenarios](#relevant-scenarios)
- [Glossary](#glossary)
- [System design](#system-design)
- [Deployment diagram](#deployment-diagram)

# Essential description

Small shops require a simple application to support the owner or manager. A small shop (ex a food shop) occupies 50-200 square meters, sells 500-2000 different item types, has one or a few cash registers 
EZShop is a software application to:
* manage sales
* manage inventory
* manage customers
* support accounting


# Stakeholders


| Stakeholder name  | Description | 
| ----------------- |:-----------:|
|	Customer  |	Use the application to find information about Products and Prizes, makes purchases  |
|	Registered Customer  |	Use the application to find information about Products and Prizes, can login making purchases online and accumulate and spend points  |
|	Shop's Owner  | Pays for the application and owns the shop to which the application is made for  |
|	Supplier  | Supplies some kind of Inventory Items to the Shop |
|	Stripe  | Provides the payment gateway for online shopping and the POS System for the shopping inside the physical shop |
|	Inventory Item  | It may be a product in sale, a product in the Prize Catalog (that is exchanged for points), or a Work Tool  |
|	Cashier |  Employee that handles transactions at the cash register, can register or delete customer accounts  |
|	Accountant  |  Employee that handles accounting |
|	Inventory Manager  |  Employee that manages inventory items from a logical point of view  |
|	Fidelity Card  |  Mechanism through which a Registered Customer is identified |


# Context Diagram and interfaces

## Context Diagram
\<Define here Context diagram using UML use case diagram>
```plantuml
@startuml
actor Owner

Employee <|-- Inventory_Manager
Employee <|-- Cashier
Employee <|-- Accountant
Inventory_Manager <|-- Owner
Cashier <|-- Owner
Accountant <|-- Owner 
Employee --> (EZSHOP)
(EZSHOP) --> Product
(EZSHOP) <-- Product
Customer <|-- Registered_Customer
Customer --> (EZSHOP)
POS_System <-- (EZSHOP)
Fidelity_Card <-- (EZSHOP)
@enduml
```


## Interfaces
\<describe here each interface in the context diagram>

\<GUIs will be described graphically in a separate document>

| Actor | Logical Interface | Physical Interface  |
| ------------- |:-------------:| -----:|
|   Customer/Registered Customer    | Web Gui | Screen, keyboard, mouse, touchscreen |
|	Employee	| Gui | Screen Keyboard |
|	Product	| Bar Code | Bar Code Scanner |
|	Fidelity Card  | Card Code | Card Code Scanner |
|	POS System | Web Services | Internet Connection |
|	Cashier	 | Gui | Screen Keyboard |

# Stories and personas
\<A Persona is a realistic impersonation of an actor. Define here a few personas and describe in plain text how a persona interacts with the system>

\<Persona is-an-instance-of actor>

\<stories will be formalized later as scenarios in use cases>

Paul is 38 and just opened a new flower shop, he has some experience in business but not many resources to hire many workers. He spends many hours managing the inventory and the finances of his small shop. He thinks that he could better spend his time interacting with clients and taking care of the flowers.

Martina is 30 and works in an office all day. She wants to do shopping before going home, in his favorite food shop, once a week. She is very practical with computers and smartphones but doesn't want to dedicate too much time ordering online. 

Calogero is 29, he has been a cashier in a supermarket for the last 5 years. Being a cashier he has to deal with customers who want to receive a prize in exchange for their fidelity card points. In order to do so, every time a client asks for a certain item he should call an inventory worker to get the list of available items and eventually update it.

Giorgio is 23, he likes to play video games, for this reason often he goes to his favorite videogames shop and buys a lot of things. He would appreciate being rewarded for all his purchases.


# Functional and non functional requirements

## Functional Requirements

\<In the form DO SOMETHING, or VERB NOUN, describe high level capabilities of the system>

\<they match to high level use cases>

| ID        | Description  |
| ------------- |:-------------:| 
|  FR1    | Manage Registered Customer |
|  FR1.1  | Define a new customer or modify an existing one |
|  FR1.2  | Delete a Customer account  |
|  FR1.3  | List all Customers |
|  FR1.4  | Manage Fidelity Cards |
|  FR1.4.1  | Create Fidelity Card associated to the new Customer |
|  FR1.4.2  | Delete Fidelity Card |
|  FR1.4.3  | Update points on Card |
|  FR1.4.4  | Transfer points beetween Cards |
|  FR2  | Manage rights. Authorize access to functions to specific actors according to access rights |
|  FR3  | Manage Inventory |
|  FR3.1  | Define a new Inventory Item, or modify existing one |
|  FR3.2  | Remove Inventory Item |
|  FR3.3  | Update quantity of Inventory Items |
|  FR3.4  | List all Products |
|  FR3.5  | List all Prizes |
|  FR3.6  | List all Work tools |
|  FR3.7  | List all items with low quantity |
|  FR4 | Manage Accounting |
|  FR4.1 | Record each transaction |
|  FR4.2 | Record expenses for taxes and supplies |
|  FR4.3 | Show the total income per Day/Week/Month/Year |
|  FR4.4 | Show the total expenses per Week/Month/Year  |
|  FR5 | Manage Suppliers |
|  FR5.1 | Add new Supplier to Suppliers list |
|  FR5.2 | Delete Supplier from Suppliers list |
|  FR5.3 | List all Suppliers |
|  FR5.4 | List Suppliers of Products |
|  FR5.5 | List Suppliers of Work tools |
|  FR6   | Manage Replenishment |
|  FR6.1 | Add to Replenishment list Items which quantity is below a given threshold |
|  FR6.2 | Create an order notification at the end of the week based on Replenishment list and Suppliers list |
|  FR6.3 | Reset Replenishment list after order is done |
|  FR6.4 | Manage Order(?) |
|  FR6.4.1 | Add another product |
|  FR6.4.2 | Modify quantity |
|  FR6.4.3 | Modify  where product Supplier |
|  FR6.4.4 | Delete product |
|  FR6.4.5 | Validate order | 
|  FR7  | Manage Online Shopping |
|  FR7.1 | List all Products |
|  FR7.2 | Show Fidelity Card points |
|  FR7.3 | Show Fidelity Offers |
|  FR7.4 | Show History of transactions |
|  FR7.5 | Add Product to Cart |
|  FR7.6 | Remove Product from Cart |
|  FR7.7 | Show Products in Cart |
|  FR7.8 | Manage Order |
|  FR7.8.1 | Choose day of pickup in store |
|  FR7.8.2 | Create Order from cart |
|  FR7.8.3 | Modify Order |
|  FR7.8.4 | Pay for Order online |
|  FR7.8.5 | Delete an Order |
|  FR8   | Manage Employee Account |
|  FR8.1 | Define a new Employee or modify existing one |
|  FR8.2 | Delete Employee |
|  FR8.3 | List Employees |
|  FR8.4 | Search for an Employee |

## Non Functional Requirements

\<Describe constraints on functional requirements>

| ID        | Type (efficiency, reliability, ..)           | Description  | Refers to |
| ------------- |:-------------:| :-----:| -----:|
|  NFR1     |  Usability | Application should be used with no specific training for Cashiers and Customers | |
|  NFR2     | |  | |
|  NFR3     | | | |
| NFRx .. | | | | 


# Use case diagram and use cases


## Use case diagram
\<define here UML Use case diagram UCD summarizing all use cases, and their relationships>
```plantuml
@startuml
actor Owner
actor Employee
actor User
actor Accountant
actor Inventory_Manager
actor Warehouse_Worker
Employee <|-- Inventory_Manager
Employee <|-- Warehouse_Worker
Employee <|-- Cashier
Employee <|-- Accountant
Owner --|> Employee
Employee --> (Manage User Account)
Owner --> (Manage Employee account)
Registered_User --|> User
Accountant --> (Manage Balance)
Registered_User --> (Make Online Purchase)
User --> (Make Purchase)
Cashier --> (Make Purchase)
Inventory_Manager --> (Manage Supply)
Inventory_Manager --> (Manage items)
Warehouse_Worker --> (Manage items)
Cashier --> (Manage items)
Inventory_Manager --> (Manage Online Catalogue)
Warehouse_Worker --> (Manage Supply)
Registered_User --> (Point Exchange for Prize)
Cashier --> (Point Exchange for Prize)
@enduml
```
```plantuml
@startuml
(Manage Employee account) .> (Delete Employee account) :include
(Manage Employee account) .> (Create Employee account) :include
(Manage Employee account) .> (Modify Employee account) :include
@enduml
```
```plantuml
@startuml
(Manage Employee account) .> (Delete Employee account) :include
(Manage Employee account) .> (Create Employee account) :include
(Manage Employee account) .> (Modify Employee account) :include
@enduml
```
```plantuml
@startuml
(Manage User Account).> (Create User Account) : "include"
(Manage User Account).> (Modify User Account) : "include"
(Manage User Account).> (Delete User Account) : "include"
(Manage User Account).> (Transfer Points Between Cards) : "include"
@enduml
```
```plantuml
@startuml
(Manage Items) .> (Define new Inventory Item) : include
(Manage Items) .> (Modify Inventory Item) : include
(Manage Items) .> (Update Quantity of Item) : include
(Manage Items) .> (Remove Inventory Item) : include
@enduml
```
```plantuml
@startuml
(Manage Balance) .> (Generate Balance) : include
(Manage Balance) .> (Record Generic Expense) : include
(Manage Balance) .> (Delete Generic Expense) : include
@enduml
```
```plantuml
@startuml
(Manage Supply) .> (Register Supplier)
(Manage Supply) .> (Remove Supplier)
(Manage Supply) .> (Modify Supplier)
(Manage Supply) .> (Register Order to Supplier)
(Manage Supply) .> (Register Order to Supplier)
(Manage Supply) .> (Set product Threshold)
(Manage Supply) .> (Delete Threshold)
@enduml
```
```plantuml
@startuml
(Manage online catalogue) .> (Add product) : include
(Manage online catalogue) .> (Remove Product) : include
(Manage online catalogue) .> (Modify Product) : include
@enduml
```
\<next describe here each use case in the UCD>
### Use case 1, UC1 - Create Customer Account
| Actors Involved |  Employee    |
| ------------- |:-------------:| 
|  Precondition     | Customer Account C does not Exist |
|  Precondition		| Fidelity card code F not already associated to an Account   |
|  Post condition     | Customer Account C added to the system  |
|  Post condition	  | Fidelity card F associated to A |	
|  Nominal Scenario     | A customer asks an employee to be enrolled in the fidelity program |
|  Variants     | Every customer can have at most 1 account and 1 fidelity card |

##### Scenario 1.1 
| Scenario 1.1 | Create Customer Account |
| ------------- |:-------------:| 
|  Precondition     | Customer Account C does not Exist |
|  Precondition		| Fidelity card code F not already associated to an Account   |
|  Post condition     | Customer Account C added to the system  |
|  Post condition	  | Fidelity card F associated to A |	
| Step#        | Description  |
|  1    | Employee logs in system |  
|  2    | Employee inserts customer data |
|  3    | Employee inserts fidelity card code  |
|  4	| System checks if SSN Format is valid |
|  4.1  | If SSN is not valid Employee reinserts it |
|  5	| System bounds Fidelity Card to SSN |	 

### Use case 2, UC2 - Modify Customer Account 
| Actors Involved        | Employee |
| ------------- |:-------------:| 
|  Precondition     | Customer Account C exists |  
|  Post condition     | - |
|  Nominal Scenario     | Employee modifies Customer Account C |
 
### Use case 3, UC3 - Delete Customer Account
| Actors Involved        | Employee |
| ------------- |:-------------:| 
|  Precondition     | Customer Account C exists |  
|  Post condition     | Fidelity_Card F unbound from A |
|  Post condition     | Customer Account C removed from the system |
|  Nominal Scenario     | Employee deletes Customer Account C |

### Use case 4, UC4 - Create Employee Account
| Actors Involved        | Owner |
| ------------- |:-------------:| 
|  Precondition     | Employee Account E does not exists |  
|  Post condition     | Employee Account E added to the system |
|  Nominal Scenario     | Owner hires new employee |

### Use case 5, UC5 - Modify Employee Account
| Actors Involved        | Owner |
| ------------- |:-------------:| 
|  Precondition     | Employee Account E exists |  
|  Post condition     | - |
|  Nominal Scenario     | Owner modifies employee account |

### Use case 6, UC6 - Delete Employee Account
| Actors Involved        | Owner |
| ------------- |:-------------:| 
|  Precondition     | Employee Account E exists |  
|  Post condition     | Account E removed from the system |
|  Nominal Scenario     | Owner removes Employee Account E |

### Use case 7, UC7 - Transfer points between user accounts
| Actors Involved      | Employee |
| ------------- |:-------------:| 
|  Precondition     | Customer Account C1 exists |
|  Precondition		| Customer Account C2 exists |
|  Precondition     | X <= C1's Fidelity Card Total Points |
|  Post condition     | An amount X of points are subtracted from C1's Fidelity Card|
|  Post condition     | An amount X of points are added to C2's Fidelity Card |
|  Nominal Scenario     | Employee transfers points beetween user accounts |

##### Scenario 7.1 
| Scenario 7.1 | Points are transferred between two cards |
| ------------- |:-------------:| 
|  Precondition     | Customer Account C1 exists |
|  Precondition		| Customer Account C2 exists |
|  Precondition     | X <= C1's Fidelity Card Total Points |
|  Post condition     | C1.points -= X |
|  Post condition     | C2.points += X |
| Step#        | Description  |
|  1    | Employee logs in |  
|  2    | Employee inserts C1's Fidelity Card |
|  3    | Employee inserts C2's Fidelity Card |
|  4	| Employee inserts amount X |
|  5	| Employee commits |

### Use case 8, UC8 - Record Generic Expense
| Actors Involved        | Accountant |
| ------------- |:-------------:| 
|  Precondition     | - |  
|  Post condition     | Expense EX registered in the system |
|  Post condition	  | Balance is updated |
|  Nominal Scenario     | The accountant registers a generic expense |

### Use case 9, UC9 - Delete Generic Expense
| Actors Involved        | Accountant |
| ------------- |:-------------:| 
|  Precondition     | Expense EX exists |  
|  Post condition     | Expense EX removed from the system |
|  Nominal Scenario     | An Accountant removes an expense from the system |

### Use case 10, UC10 - Generate Balance
| Actors Involved        | Accountant |
| ------------- |:-------------:| 
|  Precondition     | At least one financial movement is recorded |  
|  Post condition     | Balance Exists |
|  Post condition	  | Balance can be exported with proper file extension |
|  Nominal Scenario     | Accountat A generates balance |

##### Scenario 10.1 
| Scenario 10.1 | Balance is generated for a specific time frame |
| ------------- |:-------------:| 
|  Precondition     | At least one financial movement is recorded | 
|  Post condition     | Balance Exists |
|  Post condition	  | Balance can be exported with proper file extension |
| Step#        | Description  |
|  1    | Accountant logs in |  
|  2    | Accountant selects timeframe |
|  3    | Accountant selects file extension |

### Use case 11, UC11 - Show Financial movements
| Actors Involved        | Accountant |
| ------------- |:-------------:| 
|  Precondition     | Time range is valid |  
|  Post condition     | - |
|  Nominal Scenario     | Accountat selects a time range and the system shows all financial movements relative to that range |
|  Variants   | Filter only by expenses |
|  Variants   | Filter only by income |

### Use case 12, UC12 - Define new Inventory item
| Actors Involved        | Item, Inventory_Manager |
| ------------- |:-------------:| 
|  Precondition     | Item I doesn't exist |  
|  Post condition     | Item I added to inventory |
|  Nominal Scenario     | Inventory manager creates a new item and describes it, specifing the quantity |
|  Variants   | If the item is a Product, bar code and price are also registered |

##### Scenario 12.1 
| Scenario 12.1 | Item is a Work tool |
| ------------- |:-------------:| 
|  Precondition     | Item I doesn't exist |  
|  Post condition     | Item I added to inventory |
| Step#        | Description  |
|  1    | Inventory Manager logs in |  
|  2    | Inventory Manager inserts work tool datas |
|  3    | Inventory Manager sets work tool quantity |

##### Scenario 12.2 
| Scenario 12.2 | Item is a Product |
| ------------- |:-------------:| 
|  Precondition     | Item I doesn't exist |  
|  Post condition     | Item I added to inventory |
| Step#        | Description  |
|  1    | Inventory Manager logs in | 
|  2    | Inventory Manager inserts Product datas |
|  3    | Inventory Manager sets Product quantity |
|  4    | Inventory Manager inserts Bar Code |  
|  5    | Inventory Manager sets Product's price |  

### Use case 13, UC13 - Modify Inventory item
| Actors Involved        |  Inventory_Manager |
| ------------- |:-------------:| 
|  Precondition     | Item I exists |  
|  Post condition     | - |
|  Nominal Scenario     | Inventory manager modifies one or more fields of item I |

### Use case 14, UC14 - Remove Inventory item
| Actors Involved        |  Inventory_Manager |
| ------------- |:-------------:| 
|  Precondition     | Item I exists |  
|  Post condition     | Item I is removed from the system |
|  Nominal Scenario     | Inventory manager removes I from the system |

### Use case 15, UC15 - Update Quantity
| Actors Involved        |  Inventory_Manager |
| ------------- |:-------------:| 
|  Precondition     | Item I exists |  
|  Post condition     | - |
|  Nominal Scenario     | Inventory manager mofifies quantity of I |

### Use case 16, UC16 - Register Supplier 
| Actors Involved        |  Inventory_Manager |
| ------------- |:-------------:| 
|  Precondition     | - |  
|  Post condition     | Supplier S is added to Supplier list |
|  Nominal Scenario     | Inventory manager adds S to the Supplier list  |

### Use case 17, UC17 - Modify Supplier 
| Actors Involved        |  Inventory_Manager |
| ------------- |:-------------:| 
|  Precondition     | Supplier S exists |  
|  Post condition     | - |
|  Nominal Scenario     | Inventory manager modifies one or more fields of supplier S  |

### Use case 18, UC18 - Delete Supplier 
| Actors Involved        |  Inventory_Manager |
| ------------- |:-------------:| 
|  Precondition     | Supplier S exists |  
|  Post condition     | Supplier S is removed from the system |
|  Nominal Scenario     | Inventory manager removes supplier S  |

### Use case 19, UC19 - Set Product threshold 
| Actors Involved        |  Inventory_Manager |
| ------------- |:-------------:| 
|  Precondition     | Item I exists |  
|  Post condition     | Item I threshold T is set|
|  Nominal Scenario     | Inventory Manager set the quantity under which a Product should be inserted in the supplies order |

### Use case 20, UC20 - Delete Product threshold 
| Actors Involved        |  Inventory_Manager |
| ------------- |:-------------:| 
|  Precondition     | threshold T of item I is set |  
|  Post condition     | Item I has no threshold|
|  Nominal Scenario     | The threshold for a specific Item I is removed |

### Use case 21 , UC21 - Add product to online catalogue
| Actors Involved        |  Inventory_Manager |
| ------------- |:-------------:| 
|  Precondition     | Product P exists in system |  
|  Post condition     | Product P is added to online Catalogue |
|  Nominal Scenario     | Inventory manager adds to the online Catalogue a specific product |

### Use case 22 , UC22 - Remove product from online catalogue
| Actors Involved        |  Inventory_Manager |
| ------------- |:-------------:| 
|  Precondition     | Product P is in the online Catalogue |  
|  Post condition     | Product P removed from the online Catalogue |
|  Nominal Scenario     | Inventory manager removes from the online Catalogue a specific product |

### Use case 23, UC23 - Modify product in online catalogue
| Actors Involved        |  Inventory_Manager |
| ------------- |:-------------:| 
|  Precondition     | Product P is in the online Catalogue |  
|  Post condition     | - |
|  Nominal Scenario     | Inventory manager modifies the fields of a specific product |

### Use case 24, UC 24 - Register order to supplier
| Actors Involved        |  Inventory_Manager |
| ------------- |:-------------:| 
|  Precondition     | Supplier S exists |  
|  Post condition     | Order O is registered |
|  Nominal Scenario     | Inventory_Manager registers Order O in the system |

### Use case 25, UC 25 - Customer makes purchase
| Actors Involved        |  Product, Customer, Cashier |
| ------------- |:-------------:| 
|  Precondition     | Customer has enough money to pay |  
|  Post condition     | Money are added to the cash register or to the Owner's Bank Account  |
|  Post condition      | Products are given to the Customer |
|  Nominal Scenario     | Customer purchases items with cash |
|  Variants      | Customer pays with credit card |
|  Variants      | Customer uses fidelity card in order to gain points |

##### Scenario 25.1
| Scenario 25.1 | Pays with cash No Fidelity Card |
| ------------- |:-------------:| 
|  Precondition     | Customer has enough money to pay |  
|  Post condition     | Money are added to the cash register |
|  Post condition      | Products are given to the Customer |
| Step#        | Description  |
|  1    | Cashier starts sale Transaction | 
|  2    | Cashier scans Products with Bar Code Scanner |
|  3    | Name and Price are retreived for Products |
|  4    | Repeat 2 and 3 for all Products |  
|  5    | Compute total T |
|  6	| Manage payment cash amount T |  
|  7	| Deduce quantity of products from system |  
|  8	| Print receipt |  
|  9	| Close Transaction |
|  10   | Register Transaction in system |

##### Scenario 25.2
| Scenario 25.2 | Pays with credit card No Fidelity Card|
| ------------- |:-------------:| 
|  Precondition     | Customer has enough money to pay |  
|  Post condition     | Money are added to the Owner's Bank Account |
|  Post condition      | Products are given to the Customer |
| Step#        | Description  |
|  1    | Cashier starts sale Transaction | 
|  2    | Cashier scans Products with Bar Code Scanner |
|  3    | Name and Price are retreived for Products |
|  4    | Repeat 2 and 3 for all Products |  
|  5    | Compute total T |
|  6	| Customer uses Stripe POS system to read card |  
|  7	| Customer inserts Pin inside Stripe POS System |  
|  8	| System receives Payment confirmation |
|  9	| Deduce quantity of products from system |  
|  10	| Print receipt |  
|  11	| Close Transaction |
|  12   | Register Transaction in system |

##### Scenario 25.3
| Scenario 25.3 | Pays with Cash, with Fidelity Card |
| ------------- |:-------------:| 
|  Precondition     | Registered Customer R has enough money to pay |
|  Precondition     | Registered Customer R submits Fidelity Card F to the Cashier |
|  Post condition   | Money are added to the cash register |
|  Post condition   | Products are given to the Customer |
|  Post condition   | Points P are added to F |
| Step#        | Description  |
|  1    | Cashier starts sale Transaction | 
|  2    | Cashier scans Products with Bar Code Scanner |
|  3    | Name and Price are retreived for Products |
|  4    | Repeat 2 and 3 for all Products |  
|  5    | Compute total T |
|  6    | Compute gained points P |
|  7	| Manage payment cash amount T |  
|  8	| Deduce quantity of products from system |  
|  9	| Print receipt |  
|  10	| Close Transaction |
|  11   | Register Transaction in system |

##### Scenario 25.4
| Scenario 25.4 | Pays with credit card, with Fidelity Card |
| ------------- |:-------------:| 
|  Precondition     | Registered Customer R has enough money to pay |
|  Precondition     | Registered Customer R submits Fidelity Card F to the Cashier |
|  Post condition     | Money are added to the Owner's Bank Account |
|  Post condition      | Products are given to the Customer |
|  Post condition   | Points P are added to F |
| Step#        | Description  |
|  1    | Cashier starts sale Transaction | 
|  2    | Cashier scans Products with Bar Code Scanner |
|  3    | Name and Price are retreived for Products |
|  4    | Repeat 2 and 3 for all Products |  
|  5    | Compute total T |
|  6    | Compute gained points P |
|  7	| Customer uses Stripe POS system to read card |  
|  8	| Customer inserts Pin inside Stripe POS System |  
|  9	| System receives Payment confirmation |
|  10	| Deduce quantity of products from system |  
|  11	| Print receipt |  
|  12	| Close Transaction |
|  13   | Register Transaction in system |
 
### Use case 26, UC 26 - Registered Customer makes online order
| Actors Involved        | Registered Customer |
| ------------- |:-------------:| 
|  Precondition     | Registered Customer account R exists |
|  Precondition     | Cart has at least one product |
|  Precondition     | Specified timeslot T is valid |
|  Post condition     | Money are added to the Owner's Bank Account  |
|  Nominal Scenario     | A Registered customer makes an order to be picked up in store |

##### Scenario 26.1
| Scenario 26.1 | Registered Customer makes online order |
| ------------- |:-------------:| 
|  Precondition     | Registered Customer account R exists |
|  Precondition     | Cart has at least one product |
|  Precondition     | Specified timeslot T is valid |
|  Post condition    | Money are added to the Owner's Bank Account  |
| Step#        | Description  |
|  1	| Total amount Tot is calculated |
|  2	| Registered Customer inserts payment methods into Stripe interface |
|  3	| Payment confirmation from Stripe is received  |
|  4	| Items in Cart are tagged as reserved |
|  5	| Pickup in store is reserved for timeslot T |
|  6	| Transaction Tr is registered in system |



### Use case 27, UC 27 - Registered Customer pickups order in shop
| Actors Involved        |  Fidelity Card, Employee |
| ------------- |:-------------:| 
|  Precondition     | Registered Customer R has order to pickup |
|  Precondition 	| Fidelity Card is valid |
|  Post condition     | Products ordered are given to the Customer |
|  Nominal Scenario     | R book a pickup through the web app, then he goes to take his purchases after showing his Fidelity Card to an Employee |

##### Scenario 27.1
| Scenario 27.1 | Registered Customer pickups order in shop |
| ------------- |:-------------:| 
|  Precondition     | Registered Customer R has order to pickup |
|  Precondition 	| Fidelity Card code is valid |
|  Post condition     | Products ordered are given to the Customer |
| Step#        | Description  |
|  1	| Employee E logs in |
|  2	| Registered Customer R submits Fidelity Card F to Employee E |
|  3	| E checks in system for pending order associated to F |
|  4	| E retrives reserved Products |
|  5	| E marks order as completed |
|  6	| Reseved Products are removed from the system |

### Use case 28, UC 28 - Registered Customer spends points
| Actors Involved      | Product, Cashier, Fidelity card |
| ------------- |:-------------:| 
|  Precondition     | Fidelity card Code is valid  |
|  Precondition     | Fidelity card has enough points |   
|  Post condition     | |
|  Nominal Scenario     | Registered Customer spends points in store |

##### Scenario 28.1
| Scenario 28.1 | Registered Customer spends points |
| ------------- |:-------------:| 
|  Precondition     | Fidelity card Code is valid  |
|  Precondition     | Fidelity card has enough points |   
|  Post condition     ||
| Step#        | Description  |
|  1	| Registered Customer R submits Fidelity Card F to Employee E |
|  2	| Employee start a Transaction |
|  3	| Employee E sets amount of points X |
|  4	| Employee E inserts Prize description |
|  5	| Employee E inserts Registered Customer Fidelity Card code |
|  6    | Product quantity is updated |
|  7	| Close Transaction |
|  8    | Register Transaction in system |
|  9	| E gives prize to R |


# Glossary

\<use UML class diagram to define important terms, or concepts in the domain of the system, and their relationships> 

\<concepts are used consistently all over the document, ex in use cases, requirements etc>

# System Design
\<describe here system design>

\<must be consistent with Context diagram>

# Deployment Diagram 

\<describe here deployment diagram >

