A platform allows users to order food from restaurants. 
Users can add items to a cart, place orders, pay, track delivery, and rate restaurants. Restaurants can manage menus and update availability.

Domain
FoodDelivery

SubDomain
RestaurantsAndFoodManagement
UserManagement
OrderManagement
Traking
Payment

Bounded context

FoodCatalog - Core
    Food(root)
    FoodItems
    FoodSearchRepository
Domain events
searchForFood

Ordering - Core
    Order(root)
    OrderItem
    OrderRespository
Domain events
GetFoodDetails
PayForFood

Traking - generic
    Track(root)
    TrackFood
Domain events
getting order details


Restaurant Context (Core)
├── Restaurant (Aggregate Root)
├── Menu (Entity) 
├── MenuItem (Entity)
├── Availability (Value Object)
└── Events: MenuUpdated, ItemUnavailable

Order Context (Core)  
├── Order (Aggregate Root)
├── OrderItem (Entity)
├── OrderStatus (Value Object)
└── Events: OrderPlaced, OrderConfirmed, OrderCanceled

Cart Context (Core)
├── Cart (Aggregate Root)
├── CartItem (Entity)  
└── Events: ItemAddedToCart, CartCleared

Delivery Context (Supporting) - Not Generic!
├── Delivery (Aggregate Root)
├── DeliveryAddress (Value Object)
├── DeliveryStatus (Value Object)
└── Events: DeliveryStarted, DeliveryCompleted

UserManagement - Core
    User(root)
    UserProfile
    Userrepository

Payment Context (Supporting)
    Payment (Aggregate Root)
    PaymentMethod (Value Object)
    PaymentStatus (Value Object)
    PaymentRepository