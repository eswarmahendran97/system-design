Design circuit breaker

NFR
users - 1M
calling service depents on receiving service data?

Traffic calculation
over 1000 RPS

But these all not necessary

1) High level architecture - connection
user request -> service -> rest api call -> rate limiter -> receiver service

2) Domain/functionality design

circuit breaker
    Entity
        CurcuitBreakerEntity
            receiverServiceName
            currentFailureCount
            lastCheckedTime
    ValueObject
        WindowSize
        Threshold
        maxOpenTime
    Function
        allowRequest()

3) Persistence choice
not necessary
