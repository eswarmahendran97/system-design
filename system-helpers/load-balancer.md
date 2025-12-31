Design a load balancer

FR:
consitent accross users

NF:
na

Flow
Client -> LB -> server

How?
LDService config in kube


Design
1) DB/cache no needed

consistentHahsing
    getServer()
    addServer()
    removeServer()

Initiall
    hash all server into 32-bit int and add into Sortemap as key and value as IP
    consider it as ring.
    Now for each user request.. take the IP and hash it in same way.
    find the valeu for the key present to next hash or give first keys's value
