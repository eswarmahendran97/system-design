Rate limiter service

Deploy your rate limiter service 
Create 3 files and apply
kubectl apply -f ratelimiter-deployment.yaml 
kubectl apply -f ratelimiter-service.yaml 
kubectl apply -f ratelimiter-config.yaml

Now rate limiter will running in a pod


Point LB to RLS
In ingress.yml point to RLS

Now RLS recevies the request. There it need to decide, the request need to send to backend pod or return 429(too many request)

RLS receives
X-Forwarded-Host: myapi.example.com
X-Forwarded-Uri: /api/orders/123
in header

once ratelimit is valid, it can forward to this url