backpressure mechanism

FR:
Backpressure when we try to read a file and send to Elastic search

NFR:
Assuming it should be scalable
Doesnt affect the service


Flow
sidecar -> readfile -> producer -> delay -> consumer


Design:

    Entity
        BackPressureMechanism
            ProducerThread
            ConsumerThread
            Multithreaded List - LinkedList
        
    method
        produceDate
            raeds data from file
            thread wait when threshold met
        
        consumeData
            consumes the produced data

DB design
    NA