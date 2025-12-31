Design distributed logging

FR
    Log from all service
    Time based search

NFR
    Fast search

High level design

    navie approach
        mulitple services -> api call -> logging server
    
    cons:
        service is depends on logging API.
        log will be missed if service is down
    
    imporved approach
        mulitple services -> kafka
        kafka -> logging server
    
    cons:
        send is fine now but search is bad.. we need to query Single server

    
    imporved approach
        mulitple services -> kafka
        kafka -> group of logging server
    
    Data storage approach
    Assuming Im using my own DB not ES

    navie approach
        logging server -> database
    
    cons:
        When I query, I need to search frow huge chunk
    

    imporved approach
        logging server -> sharded nosql database
    
    now read also will be fast


    Service level
    How to maintain the trace.
        1) use external library
        2) or in Filter layer check trace-id in header..If not there create new uuid for trace and use it
