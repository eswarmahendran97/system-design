Clarify the requirements + Data persistence needs
    Funtional requirement
    Non-Functional - Performance, Scalability, Availability, Consistency, Security
        active users
        autentication/autorization
        metrics management
        Data senstive
        latency vs throughput
        availablity vs consitency

Decide actors or figure out while drawing HLD

Draw a basic HLD to understand the flow

Figure out Object/Entity based on it

Implementation flow
Think more to Implement using
    SOLID
    Design patterns
Try to explain with
    Folder structure
    Class Diagram
    Sequence Diagram

DDD approach
    Bounded context
    3 layers


Database
    In LLD, Talk about DB only when interviewer asks
    DB is not mandatory for all system

Database and cache Choice & Table Design + relationships
    choice based on need - consistent, relational or scalable
    data sesitivity
    cache strategy
    fan out on read and write(feed, notification)


extra:
whole flow should include - API gateway, RLS, LBS
Consider Concurrency (if needed)
Discus. Exception Handling + Logging
Discuss Testing Strategy


Note:
LLD is about classes, interfaces, polymorphism, sequence flow. If you start with tables, you look like you’re skipping the OOP part.


Basic NFR Question:

Asking Number of Active users?
we can decide
    scalling, sharding, cache(if read is more)

What is performace expectation?
We can decide based on
    latency vs throughput
less latency - we can think of CDN, grpc, inmemory cache, indexing, JVM tuning
high throughput - we can think of distributed system, redis, sharding, kafka

What is main purpose (read or right more), How consistent should be
We can decide based on
    availablity vs consitency
High available - distributed service, distributed db
High consistency - sql


Tips

use subclass instead of enum
If you are going to use a special logic use strategy pattern (Eg: find nearest..tomorrow that can vary...)


Communication
Notification - Kafka
Social Midea - Rest, Kafka(for fan out)
Chat - Websocket