Identify Entities & Relationships - sql

Identify Entities
Identify Relationships

Eg: A library has books. Each book has one or more authors. A member can borrow books and return them later. The library tracks due dates

Identify Entities(should be noun)
Book
Author
Member
Loan

Identify Relationships
Book <-> Author (many to many)... so BookAuthor Entity is needed. Dont use foreign key here
Loan -> Member (1 to many)
Loan -> Book (many to many)... so LoanBook Entity is needed. Dont use foreign key here


Eg: A ride-sharing app allows drivers to register their vehicles. Passengers can book rides with drivers. Each ride has a pickup location, drop-off location, fare, and status (requested, ongoing, completed). Drivers can have ratings from passengers.

Entities
Vehicle - (Vehicle info)
Passenger - (Passenger info)
Driver - (driver info)
Booking - (booking info)
PassengerRating

Relationships
Vehicle <-> Driver (one to one)
Driver <-> Booking (one to many)
Passenger <-> Booking (one to many)
Passenger <->  PassengerRating (one to many)

Schema
Vehicle - basic vehicle info
Driver - basic driver info + VehicleId as FK
Passenger - basic passenger info
Booking - Booking details(pickup location, drop-off location, fare, and status) + passengerId(FK) + driverId(FK)
PassengerRating - Rating details + passengerId(FK) + driverId(FK)


Eg: Restaurants register their menus on the platform. Customers can place food orders from one or more restaurants. Each order has items, quantities, prices, delivery address, payment method, and status (pending, preparing, out-for-delivery, delivered, cancelled). Delivery partners pick up and deliver orders. Customers can rate restaurants and delivery partners.

Entities
Restarunt
Menu (List of menu and details)
FoodCategory (Type of food category - breakfast, briyani, lunch)
Customer (customer details)
Order (order details - items, quantities, prices, delivery address, payment method, and status (pending, preparing, out-for-delivery, delivered, cancelled))
DeliveryPartner
RestaurantRating
DeliveryPartnerRating

Relationship
Restarunt <-> Menu (one to many)
FoodCategory <-> Menu (many to many) Menu_FoodCatgory table needed
Customer <-> order (one to many)
Order <-> Food(many to many) Order_OrderItem table needed
DeliveryPartner <-> order (one to many)
Customer <-> RestaurantRating (one to many)
Customer <-> DeliveryPartnerRating (one to many)

Schema
Restarunt - Restarunt details
Menu - Food details
FoodCategory - List of categories
Menu_FoodCatgory - MenuId and FoodCategoryId
Order_Food - OrderId and foodId
Order - orderinfo + MenuId(FK)
DeliveryPartner - Delivery Partner details
RestaurantRating - Rating details + restaruntId(FK)
DeliveryPartnerRating - Rating details + deliveryPartnerId(FK)


Eg:

Entities
Course
Instructor
student
module
Review
enrollment
Payment

Relationship
Course <-> Instructor (many to many)
Course <-> Module (1 to many)
Review <-> Course <-> Students (many to 1)
Instructor <-> Review (many to many)
enrollment <-> (student <-> course (many to many)) (one to many)
enrollment <-> Payment (1 to 1)


Schema
Course - Course details
Instructor  - Instructor details
student - student details
Module - Module details + CourseIf(FK)
Review - Review details + CourseId(FK) + studentId(FK) + Review_InstructorId(FK)
Review_Instructor - ReviewId and InstructorId
enrollment - enrollment details + paymentId(FK) + studentId(FK) + CourseId(FK)
Payment - payment info


learnt:
    incase of many to many - new table(Junction table) should be created
    incase on 1 to many - The foreign key should be in the “many” side, 
    How to check it many to many - 
        Order <-> Food(many to many) - 1 order have many food... One same type of food can be present in many order

| Entities           | Relationship | Why many-to-many?                                             |
| ------------------ | ------------ | ------------------------------------------------------------- |
| Student & Course   | Many-to-many | Student enrolls in many courses, courses have many students   |
| Author & Book      | Many-to-many | One author writes many books, one book can have many authors  |
| Employee & Project | Many-to-many | Employee works on many projects, projects have many employees |




Identify Entities & Relationships - NoSql

Indirect way from question to choose nosql
    Write Thousands of events per second
    Analytics - doesnt need strong consistency
    schema will evolve
    should be more available
    very large data

catch:
Heavy update with more read - choose sql. LSM creates new file and mark old one as tombstoned(so processing will be more while read)


Table -> Collection
Row -> Document
column -> Field

cosmos alone different
container -> item -> property

embedding vs referencing
embedding - embed whole child data in collection of parent (single query to retrive whole data)
referencing - child in different collection. reference that id in parent (to avoid the large data if child gets keep on increasing)

cheat:
Unchanging child data → Embedding.
Frequently changing child data → Referencing (comments for a post).


Entities
Restaurant
Order
Customer
Address

Restaurant
{
    id: restaueantId,
    //other details
    Menu: [
        //embedding
        menu1:{},
        menu2:{},
    ]
    review: [
        //refferencing
        reviewId
    ]
}


Order
{
    id: orderId
    //other details
    customer: customerId
    items: [
        menus
    ],
    deliveryAddress: addressId
}


Customer
{
    id: customerId
    //other details
    Address[
        id: addressId
        customer: customerId
        //address details
    ]
}


2)

Entities
User
Chat
Message

User
{
    [
        {
            userId: 1,
            //userInfo
        },
        {
            userId: 2,
            //userInfo
        }
    ]

}

Chat{
    [
         {
            chatId: 1,
            isGroupChat: true/false
            users:[
                //refferencing
                userIds
            ]
            messages[
                //old
                {
                    type: ref:
                    messageId: messageId
                }
                //latest 100
                {
                    type: emd:
                    //message details
                }
            
            ]
            //chatInfo
        },
    ]
}

message{
    [
        {
            messgaeId: 1,
            chatId: 1,
            sendBy: userId //refferencing
        }
    ]
}


3)

Enities
User
Video
Comments


User
{
    [
        {
            userId: 1,
            uploads: [
                VideoIds
            ],
            likes: [
                VideoIds
            ]
        }
    ]
}


Video
[
    {
        videoId
        //videoDetails
    },
    latestComments:[
                //for first 20
                (
                    comment: "",
                    userId: 1
                )
            ],
    olderComments: [commentIds]
]

Comment
[
    {
        comment: "",
        userId: 1
    }
]




learnt:
For historic data dont reference other table... tomorrow that address my change
"deliveryAddress": {
    "street": "456 Burger Blvd",
    "city": "Snacktown",
    "zip": "54321"
  }

In case you need to fast without using many query but should not explode means... use both refferincing(old data) and emdding(latest 100 data)