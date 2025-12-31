upload/download -> 
    create hash and check in db where the image is already present
    generate signedurl and send to client
    client will upload directly to s3
    ask cdn to cache it. Image also will have cache header and control the cache
    If image is updated send a purge request to cdn to purge the old image
    s3 also supports life cycle policy.. which helps to move cold files(older files)


generate qr -> 
using google library - convert our information into binary(using QR encoding rules).
 places this binary as 2d bitmatrix. 
 convert bit matrix to white and black box (1- black and 0 - white)

web push
send notification when user is offline. 
Need to register Service Worker (sw.js) in browser which runs in background.
Add push_subscription in server

backend:
    Generate VAPID Keys(public and private key)

frontend
    Register service worker
    Ask user for notification permission - Notification API
    Subscribe to Web Push - using public key from BE

backend
    saves the subscription json
    push offline notification



Order or payment
    use idempotent key
    use redis for atomic operation
    then process the response 
    we can use bots

redis fetches the stock count(on bootup) -> user updates redis(atomic operation, so no failure) -> updates DB
Think about fallback when DB or Redis is down



location update:
client to send location update to serve. It varies for
    stationary (who didnt pick the order) - each 2 mins
    assigned (who picked the order) - each 5 sec

or else we can use post on distance change instead of regular interval
    Only send if location changed significantly (e.g., >50 meters)

we can also use Geo-spatial DB, geospatial index
A geo-spatial database is a database that has built-in support for storing and querying geographic data.(GeoJSON)
we will store GeoJSON Point in DB. using the DB query we can get the user by range

If we dont use geo spatial db or redis Geo
You’ll have to calculate distance manually for every user using the lat and long of user



Data analyzer

Etl
Extraction:
Extract the data

Transformation:
    Data cleaning - Remove duplicates, invalid records
    Data normalization - Convert raw fields into a standard format.
    Data enrichment - Add more information using external or internal datasets
    Business Rule Application - Apply rules defined by business stakeholders.
    ML-Based Transformation

Load:
    Big query
    


Payment:
Internal wallet payments (Venmo), bank transfers, UPI, debit card, credit card — all systems maintain an immutable ledger.
Ledger service acts as source of truth

we will see tokenizationa also since we cannot save payment info


one time url

user -> generate one time url -> BE -> generates a token and returns back
Ui frame url with that token...eg: https://example.com/one-time/<token>

User clicks the URL.
Backend receives token → look up in in DB and validates



job scheduler

Easy way to store jobs
bootup -> get all jobs -> store in PQ(sort by time)

How to control the time
while(pq)
    sleep

how to call the method which job need to be trigger
    make endpoint call(ideal)
    Task Registry
        Map<Type, HandlerClass>
        job -> get by type -> gets instance -> instance.execute
    



Back-Of-The-Envelope Estimation / Capacity Planning

scale of the system
    Total users
    Montly active users
    Daily active users
    Request per second

Estimate data size / storage
    Size per record
    Total storage needed per year

Compute Capacity / Number of Servers
    based on Request per second and server handling capacity
    

