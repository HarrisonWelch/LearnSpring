# What is AOP?

AOP stands for Aspect Oriented Programming
* Methodology for creating applications

Why AOP
* Gives flexibility to remove cross cutting concerns (CCC) from our application.
* Seperate out the CCC from main business logic.

What are cross cut concerns?

### Shopping cart example
When we make a application we tend to do a lot of things. Ex shopping cart. Add items, remove items. After we checkout and order items. Add shipping address and all that. We add a number of diff logics to it. 
* Logging for each request would be a example of CCC. 
* We can also handle authorized and authenticated. Another CCC.
* Sanitize data to JSON format

Completely diff from business logic.

AOP tells us to remove the CCC from the business logic.

AspectJ library to implement with spring framework

