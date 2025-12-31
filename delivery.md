
1. Kafka consumer receives delivery request
   → deliveryId, sourceLocation, destinationLocation, customerId
2. Find Available Partners from DB
3. Get top 20 candidates based on Haversine distance
4. For top 20 candidates:
   → Call Google Directions API
   store in cache
5. Create invitation record in DB
7. Set Redis key with 10-second TTL
8. Send invitation via WebSocket
9. Subscribe to Redis expiration event:
   PSUBSCRIBE __keyevent@0__:expired
10. Partner clicks "Accept" in mobile app
11. Mobile app sends accept request
12. Delete Redis TTL key
13. Update invitation status:
    UPDATE delivery_invitations
    SET status = 'ACCEPTED',