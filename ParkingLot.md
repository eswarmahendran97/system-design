For Parkinglot

Actors and Object:
    Driver
    Entrance
    Exit
    parking space finder
    Parking space
    payment system


Class/Entity
Parking space
    SubClass
        2-wheeler parking
        4-wheeler Parking
        HandiCap parking
Gate
    Entrance
        register
        findNearestFinder
    Exit
        calculateFare
        Pay

ParkingSpaceFinder
    findNearestFinder

PaymentProcessor
    CashPaymentProcessor
    CardPaymentProcessor
    UPIPaymentProcessor