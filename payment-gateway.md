Handle large request:
    Horizontal scalling (service and DB) - High throughput
    But you can scale only to certain limit. If you need to handle large number request with available pods, then we need to handle properly
        Because of Microservice call delay or faliure you cant able process many request
            circuit breaker
        DB could able handle the load
            Sharding
        Eventual consistency is not a problem and provide Backpressure
            Kafka
            processing request in batch
        If I can send response very quick, I have handle more request per second
            cache


Low Latency:
    Network level
        Use fast DNS providers 
    gRPC for microservice calls
    Offloading concerns
         auth, rate limiting in api gateway - so services remain lightweight and can respond faster 
    Use faster data structures & algorithms
    Use parallel operation
    Circuit breakers / bulkheads: Prevent slow downstream services from affecting your latency.
    Opening a new DB connection for each request is expensive
        Connection pooling
    Searching large tables without indexes is slow
        Using Indexing in DB
    Materialize view
    Split your database horizontally so each shard handles a subset of users
        Sharding Based on Region
    Avoid hitting the database or external service
        Cache
    faster response transfer over network
        Compress/Paginate API Responses
    Avoid N+1 queries


Handling and Protecting Sensitive Data
    Application level Encryption. Key will be stored in vault
    Mask card numbers
    Only store what is required.
    

