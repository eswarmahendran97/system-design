DB Choice

SQL
follows ACID principle
Because of following ACID, it can slow things down and makes scaling harder but cosistent and relaiable
How?
In time
Atomicity
In a transaction if you have multiple operation(update/insert/delete).. everything need to succeeded or everything need to be rollback
Consistency
While doing a transaction, the operation should make data in valid state(no constraint or rule miss)
Isolation
Multiple transactions running at the same time should not interfere with each other.
Durability
before confirming the commit, the database must write the transaction to a physical storage medium, like a disk. to avoid loss of data

In scaling(horizontal)
During an update I need to alter 2 different db which is present in 2 different server. According to atomicity.. both need to be succeded or role back
locks need to communicated between servers

Nosql Document Stores
Things are fast and more scalable is better but incosistent and not relaiable
How?
In time
Nothing is locked. It works on Eventual Consistency(for brief time it will be inconsistent)
In scaling(horizontal)
No joins, why
The core principle of NoSQL data modeling is to denormalize data, which means storing related information together in a single document or record. 
This often eliminates the need for joins.


When to use SQL and NoSQL
SQL
Data -> transaction, accurate, reliable, connection between multiple entities(normalized)
NoSQL
Horizontal scaling, High availability, Flexible schema(denormalize)

When to use which
based on read, write, availability... 
note: B+ read heavy, LSM write heavy

Strong Consistency + Relational - MySQL (MySQL, Oracle, SQL Server)
Read-heavy, SQL with B+ Tree - MySQL, Oracle, SQL Server
Write-heavy, LSM-based NoSQL - Cassandra, Cosmos DB
Highly Scalable NoSQL (Sharding) - Cassandra, MongoDB
Highest Availability + Global Distribution - Cosmos
Search-heavy / Analytics - Elasticsearch
Ultra-low Latency - DynamoDB with DAX(not open source)
relationship - Neo4j(graph db)