# Handle High concurrency
Thousands of users are trying to modify the same resource (seat, product, slot) — only one should succeed

## Optimistic Locking (DB level)
UPDATE seat SET status='BOOKED' WHERE id=101 AND status='NOT_BOOKED';

What if 2 users tries to update at same time?
For an update in a row. Db will always locks it until transaction (update) completed to avoid inconsistency.


## Distributed Locking (Cache level)
In DB level you can handle lock.
But what if it is S3

Also for eg, there 500 concurrent request 
I will send all request to DB. 1 request will updating the row.. remianing will be locked. It becomes bottleneck in DB.

What if I send 1 request to DB using Synchronized keyword
This will work only if service have 1 instance

So, we can use Distributed Locking

calls redlock before performing operation. If it respond with ok, we can process the operation


## Message queue
User tries to book a seat
Selects a seat and proceeds to payment.
Payment is completed
Successfull Booking request added to Queue
Consumer processes the queue
A consumer (worker) picks requests from the queue one by one.