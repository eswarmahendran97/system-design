Design rate limiter

I will ask NFR
Performance, Scalability, Availability, Consistency, Security

assuming - 1M users
scalablity - scalable
how many service - 5


Traffic calculation
over 1000 RPS

<100 RPS → Usually fine for small services, no scaling needed.
100–500 RPS → Depends on complexity; some services scale here if DB-heavy.
500–1000 RPS → Many production systems begin horizontal scaling around here.
>1000 RPS → Almost always need scaling (unless you’ve proven a single instance can handle it).

size calculation:
In DB
IpAddress: 15
userid: 10
serviceName: 10
lastRequestTime: 8
total: 43 - 50 bytes per row
1M users * 5(service) * 10 req = 50M req
for 50M request -> so size of table will be 50 * 50M = 2.5B -> 2.5GBs used


Considering this my initial tought is 
1) Seperate Rate limiter service.. like mini gateway
    not sidecar
        reason - service will scale unnecessarly for Rate limiter coz it handles huge request

2) Persistence choice
    Will use Redis.. since distributed and have eviction

3) Domain Design
    Rate limiter
        Enities
            Request Info - Ipaddress, userId
        function
            Dynamic sliding window
                get count for Ipaddress, userId from Redis
                    if(underlimit)
                        updates
                        forward request
                    else
                        return 419

4) DB Design
    Redis
        IpAddress, userId, serviceName, lastRequestTime

algo:
🔹 Fixed Bucket (Fixed Window Counter)
Limit: 100 requests per minute.
If a user sends 100 requests in the first 10 seconds, the bucket is full.
All further requests in the remaining 50 seconds are rejected, even though the overall minute still has capacity.
❌ This causes bursty traffic allowance but no smoothing.

🔹 Leaky Bucket
Requests enter a bucket (queue).
They leak out at a constant rate (say 100 per minute = ~1.67 requests/second).
If 100 requests arrive in 10s → they don’t all get processed immediately, they wait and drain steadily.
Only when the bucket overflows (more requests arrive than can be drained + held in buffer) are requests rejected.
✅ Ensures smooth flow, no “use it all at once” problem.
