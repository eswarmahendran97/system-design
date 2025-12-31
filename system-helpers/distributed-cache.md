Distributed cache

FR:
it should be distributes
LRU

NFR
Assuming - Available, scalable

Flow:
client -> server -> cache config server(DefaultKubernetesClient library to get pod details) -> cache servers

Design:
    considering Timecomplexity
        I use LinkedHashMap
    considering LRU window size
        10 (can be changed in config server)
    
    method
        addCache(key, value) -> check LRU size and remove last element then add new
        getCache(key)
    
    additional cosideration
        Sharding - server details will be maintained in config server
            I will use consistent hashing for this based on key
        replication
    
DB design:
    depends on, is the data to be cached is calulated data or data stored in DB
