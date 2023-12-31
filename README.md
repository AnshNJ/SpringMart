# SpringMart - eCommerce Backend Application

This repository houses the backend application for SpringMart, an ecommerce platform, featuring a suite of 37 APIs for performing CRUD operations on customer, seller, product, cart, card, and order entities. The application is
hosted on AWS, utilizing a cloud database for seamless scalability and performance.
#

Swagger implementation with demo - http://localhost:8080/swagger-ui/index.html (Application needs to be running for the UI to load).

# Features

## Customer:
- #### Perform CRUD operations on Customer data.
- #### Facilitated APIs to get all customer's information as well as find customers with specified parameters
- #### Encompasses cardinal relations between Card , Cart and Order entities.

  ![image](https://github.com/AnshNJ/SpringMart/assets/113118218/7f7ec065-3439-428a-ba1e-550ae9c5abe6)
  
## Seller:
- #### Perform CRUD operations on Seller data.
- #### Facilitated APIs to get all seller's information as well as find and update sellers with specified parameters.
- #### Encompasses cardinal relations between Product entity.

  ![image](https://github.com/AnshNJ/SpringMart/assets/113118218/ed1fc564-b646-4b5f-b300-03a8ca464c1c)

## Product:
- #### Perform CRUD operations on Product data.
- #### Facilitated APIs to get all product's information as well as find with specified parameters.
- #### Provided additional APIs to showcase products based on certain criterias, such as costliest, and cheapest variants.
- #### Facilitated API to obtain Product information based to provided Seller information.
- #### Each Product has been accomodated with state of "AVAILABLE" or "OUT-OF-STOCK".
- #### Encompasses cardinal relations between Item and Seller entity.

  ![image](https://github.com/AnshNJ/SpringMart/assets/113118218/a1d66505-5115-4498-bbc5-1043584e5e22)

## Order:
- #### Provides the ability to place real time orders without requiring to be added to cart.
- #### Additional APIs to filter set orders based on cost ,as well as time of order.
- #### Facilitated APIs to get all order's information for the specific customer.
- #### Encompasses cardinal relations between Customer and Item entity.

  ![image](https://github.com/AnshNJ/SpringMart/assets/113118218/cda2b839-c9e0-47df-9f32-d9b668eaeb34)

## Cart:
- #### Provides the ability to store multiple items of varying type , quantity, and cost for the specific customer.
- #### Provided API to check-out cart containing varying items depending on the customer.
- #### Additional APIs to view/delete contents from the specified customer's cart.
- #### Encompasses cardinal relations between Customer and Item entity.

  ![image](https://github.com/AnshNJ/SpringMart/assets/113118218/0b9926be-5864-4936-9638-0a5eea667660)

## Card:
- #### Provides the ability to store multiple Card Information for the specified Customer.
- #### Provided APIs to perform CRUD operations on specific customer's card.
- #### Additional APIs to view all Card entities present for the specified customer.
- #### Encompasses cardinal relations between Customer entity.

  ![image](https://github.com/AnshNJ/SpringMart/assets/113118218/71e01924-f0ec-4305-a0c4-e5f1d887d8f4)

#
## Requirements
- #### Java Development Kit (JDK) 8 or later installed./br
- #### SpringBoot 3.0 or later versions.
- #### Basic knowledge of Java and SpringBoot.
#

## Tech - Stack
- #### Programming Language: Java
- #### Integrated Development Environment (IDE): Intellij Idea
- #### Database Management System: MySQL (AWS Cloud Hosted)
- #### Version Control: Git (for version control and GitHub for repository hosting)
- #### User Interface (UI): Swagger UI (Provide a list of test-ready APIs)
- #### Backend Logic: Java + Springboot
#

## How to Run the Application

1. **Clone the Repository**:
   - Clone this repository to your local machine using the following command:
     ```
     git clone https://github.com/AnshNJ/SpringMart
     ```

2. **Open the Project in the specific IDE**:
   - Launch NetBeans and open the project by selecting `File` > `Open Project` and navigating to the cloned directory.

4. **Build and Run**:
   - Build and run the project from.


## Additional Notes
- Include any additional notes, dependencies, or special configurations that might be necessary.

## Thank you!
