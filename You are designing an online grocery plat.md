You are designing an online grocery platform. 
Users can browse products, add them to a cart, place orders, schedule deliveries, and make payments. 
Stores can manage inventory and product availability. There is also a loyalty program for rewarding users based on purchases.

Domain
Online grocery platform

SubDomain
Product Managment
Order Management
Delivery Management
Payment

Bounded context
ProductSearch - core
    ProductSearch(root)
    SearchItem-Entity
    Product-Entity
    status - ValueObject
    Domain event
        ProductSearched
        FilterApplied
        RecommendedProductShowed
Cart - core
    Cart
    CartItem
    Product
    Domain event
        AddedToCart
        RemovedFromCart
Order - core
    Order(root)
    OrderItem-Entity
    OrderStatus-ValueObject
    Domain event
        ProductOrdered
        PaymrntProcessed
ProductDelivery - supporting
    ProductDelivery(root)
    ProductDeliveryItem-Entity
    Status-ValueObject
    Domain event
        ProductDelivered
        DeliveryDelayed

Payment - supporting
    Payment(root)
    PaymentItem-Entity
    Status-ValueObject
    Domain event
        PaymentSuccessfull
        PaymentFailed