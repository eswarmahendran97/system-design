## operational transformer

In git
Conflicts can be left for humans to resolve.

This is not suitable for real time collabaration system. So, we need to use Operational Transformer.

## How Operational Transformer works?

Unlike git (which has multiple branch), OT-based systems maintain a single authoritative state
    Single current version: The server maintains one "current" or "canonical" document state

The OT Process Flow
Operation applied (Client-side):
Operations are applied to current state locally in client. Then that operation is sent to server.

Operation Queue (Server-side):
When multiple clients send operations simultaneously, the server queues them.

Sequential Application:
Operations are processed one by one against the current state.

Transformation:
Each queued operation gets transformed against any operations that were applied before it.
When operations arrive at the server, they might be based on an old state, not the current one.
The transformation adjusts these operations so they make sense against the new current state.

State Update:
The transformed operation is applied, creating a new current state.

Broadcast:
The server sends the applied operation to all other clients.

Note: It will not send the current state (file).Only operations are broadcast.


## How Operations are Transformed

Note: Both Client and server track versions. The client attaches the version number to every operation it sends in metadata.

Steps:
1. User A performs an operation (metadata: v0) and sends it to server.
2. User B performs an operation (metadata: v0 – before receiving A’s update) and sends it to server.
3. Server receives op1 and op2. Both will be in queue.

For op1:
4. Transformation: checks operation.basedOnVersion < currentServerVersion.
(v0 < v0) = false → No transformation.
Applies operation to current state and updates currentServerVersion.
Broadcast operation.

For op2:
5. Transformation: checks operation.basedOnVersion < currentServerVersion.
(v0 < v1) = true → transforms the operation (op2 → op2’) (against op1 - Transform(op1, op2) = op2'... Next time Transform(op2', op3) = op3').
Applies operation to current state and updates currentServerVersion.
Broadcast transformed operation (op2’).

## How Client applies the operation:

1. Client first applies and sends the operation(with metadata) to server
2. It adds the sent operation in queue.
3. When it receives the operation from Server. It checks is it present in queue.
4. If already present it will ackonwledge and not apply the received operation. else it will apply it and update the current version


## Transformation Functions
Transformation Rules
    Insert vs Insert, 
    Insert vs Delete, 
    Delete vs Insert,
    Delete vs Delete,

Considering the rule and data we receive. It will give back a new operation which we broadcast


# CRDT
It's same like OT... but instead of sending operation to server... it sends to replicas

Why it is needed:
It works offline

Google Doc with OT can work offline
It doesnt need central server
