Loadbalancer hashing


Normal round robin

static count=0;
on each call
return count++ % no.of server


Consistent hashing
Return same server for same user

create a sortedMap<Integer, String> sortedMap;
add all server in it - by converting serverId into 32bit Integer

When user comes with userId
get hash for the userId by sameway (integer - 32bit)
check sortedMap that any key present after found userId 32bit key
SortedMap<Integer, String> tailMap = ring.tailMap(hash); -> gives the reamining after the input hash
if noting get hash of fisrtServer present in sortedMap and return value
else get firstkey of tailMap -> hash. check the hash in sortedMap and return the value