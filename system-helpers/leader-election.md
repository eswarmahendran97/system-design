Design a Leader Election Mechanism in a Distributed System
Leader-Follower

High level flow
client on write -> leader (writes) -> all consumers (writes)
client on read -> any server(leader or follower) based on LB

RAFT algo
    on each replica.. timeout will be maintained
    Initially all will be Candidate state
    sends request to each other, elects Candidate with less timeout

Design:
     LeaderFollowerService
        setTimeOut() -> Post construct
        read data(int key)
        write data(Object value)
        sendHeartBeat()
        checkLeaderAlive() -> change state
        requestVote()
        electLeader()