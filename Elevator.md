
ElevatorCar
    field
        int id;
        Direction direction;
        int currFloor;
        Queue<Integer> destinations:
        ElevatorButton button
    method
        void giveDestination(int destination);
        void emergencyStop();


ElevatorButton (command)
    field
        ElevatorCar car
    method
        execute();


DestinationButtons implements ElevatorButton
    type
        ElevatorCar car;
        int floor;
    method
        execute()
            car.giveDestination(floor)


EmergencyButton implements ElevatorButton
    type
        ElevatorCar car;
    method
        execute()
            car.emergencyStop()


Direction
    Up, DOWN, IDLE


ElevatorController
    field
        int id;
        List<ElevatorCar> cars;
    method
        getCard -> based on Startegy

SchedulingStrategy
        getCard()

NearestCarStrategy
        ElevatorCar getCard()
            //getNearestCar(int floor, Direction direction);


ElevatorSystem
    field
        ElevatorController contoller;
        ElevatorButton button = new DestinationButtons(car, 5);
    method
        getNearestCar(floor, direction);
        button.execute();
        
        





    