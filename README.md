# Cabify Mobile Challenge

Besides providing exceptional transportation services, Cabify also runs a physical store which sells Products.

Our products are like this:

``` 
Code         | Name                |  Price
-------------------------------------------------
VOUCHER      | Cabify Voucher      |   5.00€
TSHIRT       | Cabify T-Shirt      |  20.00€
MUG          | Cabify Coffee Mug   |   7.50€
```

Various departments have insisted on the following discounts:

 * The marketing department believes in 2-for-1 promotions (buy two of the same product, get one free), and would like for there to be a 2-for-1 special on `VOUCHER` items.

 * The CFO insists that the best way to increase sales is with discounts on bulk purchases (buying x or more of a product, the price of that product is reduced), and demands that if you buy 3 or more `TSHIRT` items, the price per unit should be 19.00€.

Cabify's checkout process allows for items to be scanned in any order, and should return the total amount to be paid.

Examples:

    Items: VOUCHER, TSHIRT, MUG
    Total: 32.50€

    Items: VOUCHER, TSHIRT, VOUCHER
    Total: 25.00€

    Items: TSHIRT, TSHIRT, TSHIRT, VOUCHER, TSHIRT
    Total: 81.00€

    Items: VOUCHER, TSHIRT, VOUCHER, VOUCHER, MUG, TSHIRT, TSHIRT
    Total: 74.50€


# To do
- Implement an app where a user can pick products from a list and checkout them to get the resulting price. No need to implement any real payment system, just a fake feedback about the payment has been completed.
- The discounts can change in the future, depending on the year season we apply different ones.
- There is no need for a user login screen.
- We would like to show users what discounts have been applied in their purchase. 
- You should get the list of products from [here](https://api.myjson.com/bins/4bwec).


# Proposed solution

### Project structure
This project is built following a clean arquitecture pattern with 3 main modules: app, domain and data. 

 - App: contains all view-related code (fragments, activities, adapters, viewholder and so on)
 - Domain: contains usecases, model classes and a repository interface
 - Data: contains classes related with data access following a repository pattern. In this project, you will only find one cloud datasource. There is no cache datasource involved. 
 
Additionaly, I have created 2 modules: checkout and kpresenteradapter (which is a library I created myself, but I have included it in the project so that the code can be reviewed as well just in case).

### Checkout module
 - Pure Java library module that contains all checkout related logic. The checkout process is represented by a ```Checkout``` class, with receives a ```DiscountConfig``` dependency in its constructor. ```DiscountConfig``` is just an interface with a single property of type ```Map<String, DiscountStrategy>```, used to link an item code with a specific ```DiscountStrategy```.

```kotlin
interface DiscountConfig<T: ItemPrice> {
    val config: Map<String, DiscountStrategy<T>>
}
```
* ```DiscountConfig``` class is generic, so you must specify a type that inherits from ```ItemPrice```. ```ItemPrice``` is the class used by our java module as an input of elements involved in the checkout process. The output of this library is a list of ```CheckoutItem``` objects, where each object represents an association of each ItemPrice object with an optional discount applied to it. ```ItemPrice``` class is just an interface with two fields: code and price:

```kotlin
interface ItemPrice {
    val code: String
    val price: Double
}
````

* As you can see, our ```checkout``` module is completely isolated and does not have any dependency to the rest of modules defined in this project. ```ItemPrice``` interface is implemented in the ```domain``` module by a class called ```Item```. This class represents an item available to be added to our shopping list. 

```kotlin
data class Item(override val code: String, val name: String, override val price: Double): ItemPrice
```
* The project structure is maded to be easily extensible and configurable. Discounts configuration is specified in the class ```CabifyDiscountConfiguration``` that you can find in ```app``` module.  As you can see below, this class inherits from ```DiscountConfig``` and will be passed to the ```Checkout``` class constructor. 


```kotlin
class CabifyDiscountConfiguration: DiscountConfig<Item> {

    //Item codes
    val TSHIRT = "TSHIRT"
    val VOUCHER = "VOUCHER"

    override val config = mapOf(
        TSHIRT to DiscountStrategyBulk<Item>(minAmount = 3),
        VOUCHER to DiscountStrategy2x1<Item>()
    )
}
````

 - You can see how ```TSHIRT``` is linked to a ```DiscountStrategy``` of type ```DiscountStrategyBulk```, while ```VOUCHER```is linked to a ```DiscountStrategy2x1```. Both strategies are provided by the ```checkout``` library, but you can create new ones easily extending from ```DiscountStrategy``` class. 
 
 ### KPresenterAdapter module 

- KPresenterAdapter is a library made by myself which provides a framework to create adapters easily following an MVP pattern for your viewholders. It is open source and you can find all the documentation following this [link](https://github.com/vicpinm/KPresenterAdapter).

- This library is used to create two different viewholders. ```ItemViewHolder``` is used by ```ItemListFragment``` to represent ```Item``` objects available to be added to your shopping list, while ```CheckoutItemViewHolder``` is used by ```CheckoutFragment``` to represent ```CheckoutItem``` objects returned by ```checkout``` library in order to show your shopping list with its discounts.  


 ### App module 
 
 - This module contains all the classes involved in the view logic and view representation. Dependency injection is also configured in this module using Koin (https://insert-koin.io/). All dependencies are defined in the ```AppModule``` class. 
 
- This project contains a single activity which holds two fragments. ```ItemListFragment``` shows a list of all ```Item``` objects returned by the domain. ```CheckoutFragment``` shows a list of all ```CheckoutItem``` object, which shows the discount and final price of each item in your shopping list. 

- Fragments are built with an MVVM approach using ```ViewModel``` class from google architecture components. Both fragments share the same viewModel implemented in ```CheckoutModel``` class. Thus, when the first fragment adds items to the shopping list, the second fragment is notified of this change and updates its view consequently. 


 ### Domain module 
 
 - Domain module contains our model definition and the use cases. It also contains an interface with the repository definition used in the project, in order to decouple ```domain``` and ```data``` modules. 
 - In this project, there is only one use case, ```GetDataCase```, which extends from ```UseCase```. ```UseCase``` class contains a coroutine which executes our use case on a background thread and dispatches the result back on the main thread. 
 

### Data module
- Simple module that contains a repository to access to a cloud data source called ```CheckoutService```. ```CheckoutService``` makes use of Retrofit library to perform a call to the endpoint, and uses gson to parse the response and to convert it to a collection of ```DataItem```objects. ```DataItem``` objects are mapped to ```Item``` objects using a mapper class called ```DataItemMapper```.

### Unit and UI Tests
- ```kpresenteradapter``` and ```checkout``` modules includes unit tests. In the case of ```checkout``` module, you can find a class called ```CheckoutParameterizedTest``` which includes a single test method, but is called several times with different input configurations to cover the diffent discounts a scenarios. 
- ```app``` module contains UI tests for both ```CheckoutFragment``` and ```ItemFragment``` using Espresso library. You must execute these tests on a emulator or a real device with disabled animations so that they can run properly. 

