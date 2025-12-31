# gRPC Complete Study Guide

## What is RPC?

**RPC - Remote Procedure Call**

- **Procedure call**: procedure is nothing but function or method. Making a call to that procedure is called Procedure call
- **Remote procedure call**: Calling a function which is present in different Remote Desktop

Since it was designed by Google we are calling it as **gRPC**

---

## Why gRPC is Preferred Over REST?

**gRPC is much faster**

### How?

Either on gRPC or REST, we need to serialize the data then we need to send. **Serialization is faster in gRPC**.

### Serialization Process

**Generally will we serialize java object?**
No, Java Object to JSON/Protobuf then it is serialized.

Objects must be converted into a common serialized format (like JSON or Protobuf) because raw in-memory objects are language and platform-specific.

*Note: If sender and receiver are of same platform we may not need to convert it to common format. But this makes them tightly coupled*

### Comparison:

**In HTTP/1.1:**
- We need to convert request (header, body, status code etc) to text → serialize → send

**In HTTP/2:**
- Object → protobuf → send
- Protobuf is faster to serialize and deserialize, so gRPC is faster

---

## Apart from Serialization - Why Choose gRPC Over REST?

### HTTP/2

**HTTP/2 Advantages:**
1. **Multiplexing** - avoids head-of-line blocking (in browser you can make only max of 6 requests to a domain at a time)
2. **Binary framing** - whole request is in binary format so no need extra transformation
3. **Full-duplex streaming** - request and response in same channel
4. **H Pack** - Header compression

### Reality Check for Microservices

But honestly gRPC with HTTP/1.1 and HTTP/2 is the same for micro service architecture:
- No streaming needed - just request/response
- No multiplexing needed - one call at a time
- Header overhead minimal - internal services have simple headers
- Connection reuse works fine - HTTP/1.1 keep-alive handles this

---

## Browser Limitations

### Important Note:
It is **not designed for web API calls from Frontend (web browsers)**. It's only designed for calling between micro services.

### Why natively not supported from Web browser call to backend?

When your client tries to get a request from server, it needs to use **Fetch/XHR/WebSocket browser API**. These APIs are gatekeepers which follow single origin policy and CORS.

Browser may use HTTP/2 features like (multiplexing, binary frames, bidirectional streaming, flow control) internally, only through fetch/xhr. These APIs support only one request and response per connection but gRPC opens a connection and uses it for multiple req and res.

### Example:
- **Browser**: "I give JS one request, one response at a time."
- **gRPC**: "I want multiple simultaneous requests/responses over one connection with streaming."
- **Result**: native gRPC cannot run in the browser.

---

## gRPC Call Flow (Sender → Receiver)

### Step 1: Define the service and messages
Create a `.proto` file describing the RPC methods and messages.

### Step 2: Compile the proto file
Use `protoc` to generate stub code for both client (sender) and server (receiver).
The same proto is used on both sides.

### Step 3: Set up the network info
On the sender/client, create a `ManagedChannel` with the receiver's host and port.

### Step 4: Create stub on sender side
Use the generated stub to call server methods as if they were local. This stub class acts as a proxy for the Server, allowing the client to interact with server as if server were a local object.

### Step 5: gRPC runtime handles serialization & HTTP/2 transport
Stub serializes request → Netty → HTTP/2 → server stub.
Why grpc not using tomcat with http/2 instead of netty
Tomcat supports HTTP/2 for servlets, but it hides streams, frames, and full-duplex control from application code. gRPC needs direct access to these HTTP/2 features, so it uses Netty instead.
EG: streaming in netty.. not possible in tomcat
responseObserver.onNext(response);

### Step 6: Server processes the request
Server stub deserializes request → calls service implementation → serializes response.

### Step 7: Sender receives the response
HTTP/2 transports response back → client stub deserializes → client gets the object as if method was local.

---

## Understanding Stub Class

### What is Stub class?
Class having dummy implementation of functionality using sys out.

### Why to use Stub class?
1. **Make client, server strongly typed** - ensures both parties adhere to the same service definitions and data structures, maintaining consistency and type safety
2. **Serialization and Deserialization part is taken care here**

---

## Protocol Buffers (Protobuf)

### What is Protobuf?
**Protocol buffer is a library**

It is a toolset consisting of:
1. **Protobuf language** - providing a language-neutral way to define data structures and services in `.proto` files
2. **protoc** - compiles `.proto` file and generates Client and Server code
3. **Libraries** - Serialization and deserialization

Uses `.proto` file - structure of data and services

---

## .proto File Structure

### Basic Config
*(Configuration details go here)*

### Message Structure
```proto
message HelloRequest { 
    string firstName = 1; 
    string lastName = 2; 
}
```

**Important**: The protocol buffer uses this tag to represent the attribute, instead of using the attribute name. 

**Example**: The protocol buffer will use the number 1 as tag

### Service Contract
This serves as the blueprint for service implementations, ensuring they adhere to the same communication protocol and data structures

```proto
service HelloService { 
    rpc hello(HelloRequest) returns (HelloResponse); 
}
```

The `hello()` operation accepts a unary request, and returns a unary response. gRPC also supports streaming by prefixing the `stream` keyword to the request and response.

---

## Additional Features

### Interceptors
It also supports **Interceptors** - addressing cross-cutting concerns (auth, logging, metrics)


## Configuration

1) Add respective dependencies - grpc-client-spring-boot-starter, grpc-server-spring-boot-starter
2) Plugin to generate stub file

```plugin
    <plugin>
        <groupId>com.github.os72</groupId>
        <artifactId>protoc-jar-maven-plugin</artifactId>
        <version>3.11.4</version>
        <executions>
            <execution>
                <phase>generate-sources</phase>
                <goals>
                    <goal>run</goal>
                </goals>
                <configuration>
                    <addProtoSources>all</addProtoSources>
                    <includeImports>true</includeImports>
                    <includeStdTypes>true</includeStdTypes>
                    <includeMavenTypes>direct</includeMavenTypes>
                    <outputDirectory>src/main/java</outputDirectory>
                    <inputDirectories>
                        <include>src/main/proto</include>
                    </inputDirectories>
                    <outputTargets>
                        <outputTarget>
                            <type>java</type>
                            <outputDirectory>src/main/java</outputDirectory>
                        </outputTarget>
                        <outputTarget>
                            <type>grpc-java</type>
                            <pluginArtifact>io.grpc:protoc-gen-grpc-java:1.57.2</pluginArtifact>
                            <outputDirectory>src/main/java</outputDirectory>
                        </outputTarget>
                    </outputTargets>
                </configuration>
            </execution>
        </executions>
    </plugin>
```
inputDirectories and outputDirectory is provided

3) proto file

```proto
syntax = "proto3";

option java_multiple_files=true;
option java_package="com.walmart.clientService.generated";
option java_outer_classname = "ServerConnection";

package server;

service ServerService {
  rpc GetByName (ServerNameRequest) returns (ServerNameResponse) {}
}

message ServerNameRequest {
  string name = 1;
}

message ServerNameResponse {
  string name = 1;
}
```

4) Config class in client to call server

```client
public class ServerConfig {

    private ManagedChannel channel;
    private ServerServiceGrpc.ServerServiceBlockingStub serverStub;

    @PostConstruct
    public void init(){
        String host = "localhost"; // server's info
        int port = 9090;
        channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        serverStub = ServerServiceGrpc.newBlockingStub(channel);
    }

    @PreDestroy
    public void shutDown(){
        channel.shutdown();
    }
}
```

5) serice method based on service given in proto file

```
proto
service ServerService {
  rpc GetByName (ServerNameRequest) returns (ServerNameResponse) {}
}

client
public class ClientService {
    // serverStub is injected/created from ManagedChannel
    private final ServerServiceGrpc.ServerServiceBlockingStub serverStub;

    public ClientService(ServerServiceGrpc.ServerServiceBlockingStub serverStub) {
        this.serverStub = serverStub;
    }

    public ServerNameResponse method(ServerNameRequest request) {
         // setting request inside entity which we defined in protofile
        return serverStub.getByName(request); // looks like a local method call
    }
}


server
@GrpcService
public class ServerService extends ServerServiceGrpc.ServerServiceImplBase {

    @Override
    public void getByName(ServerNameRequest request,
                          StreamObserver<ServerNameResponse> responseObserver) {
        // setting response inside entity which we defined in protofile
        ServerNameResponse response = ServerNameResponse.newBuilder().setName("name").build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
```