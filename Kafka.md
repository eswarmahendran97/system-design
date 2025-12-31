# Kafka

## Components in Kafka
- **Broker** → A Kafka server that stores data. Accepting writes and Providing reads
- **Zookeeper** → Manages Kafka brokers by handling leader election and metadata (in older Kafka versions)
- **Topic** → A logical channel or category where messages are published
- **Partition** → A unit of parallelism within a topic that stores an ordered sequence of records
- **Offsets** → A unique position/index of a record within a partition
- **Producers** → Applications that publish messages to Kafka topics
- **Consumer group** → A group of consumers that share the load of reading from a topic
- **Consumer** → An application that reads messages from Kafka topics

---

## Zookeeper

### Purpose:
Managing metadata:
- **Topics** → List of topics and their configurations
- **Partitions** → It keeps track of how many partitions exist per topic and which broker is the leader for each partition
- **Brokers** → Maintains the list of active brokers in the cluster, so everyone knows who is "in" or "out"
- **ACLs (Access Control Lists)** → ZooKeeper stores security rules

---

## How Kafka System Starts

### Step 1: ZooKeeper starts
```bash
PATH="$PATH:/pathTo/kafka_2.12-3.6.1/bin" 
zookeeper-server-start.sh ~/pathTo/kafka_2.12-3.6.1/config/zookeeper.properties 
```

Only the root / znode exists (folder structure).

Path looks like:
```
/
```

### Step 2: Broker starts
```bash
PATH="$PATH:/pathTo/kafka_2.12-3.6.1/bin" 
kafka-server-start.sh ~/pathTo/kafka_2.12-3.6.1/config/server.properties 
```

**What happens:**
1. Broker registers itself in ZooKeeper
2. New znode will be added `/brokers/ids/[brokerId]`

Path looks like:
```
/
├── brokers
    ├── ids
        ├── 1
```

3. Broker tries to become controller by sending create controller request to zookeeper. If succeeds it becomes controller (only extra role, its originally a broker)

Path looks like:
```
/
├── brokers
    ├── ids
        ├── 1
├── controller
```

4. New controller gets metadata from zookeeper and loads inside it (in-memory cache)
   - The controller uses this metadata to make decisions (e.g., electing leaders, reassigning partitions)
   - It need to have metadata because If a broker dies.. it need to know for which partition it act as leader and which one it act as replica
   - Controller not only sends the metadata once.. It will send on each change.. like ISR (in sync replica) change

### Step 3: Topic creation (example: 3 partitions, 2 replicas)
```bash
kafka-topics.sh --bootstrap-server localhost:9092 --create --topic order --partitions 3 --replication-factor 1 
```

Creates folders in broker based on topic and partition:
```
/var/lib/kafka/data/orders-0
/var/lib/kafka/data/orders-1
/var/lib/kafka/data/orders-2
```

**Flow:**
1. An admin client (CLI or API) sends a create topic request → broker forwards it to ZooKeeper
2. The controller decides the partition–replica assignment across brokers. Controller will try to spread partition across multiple brokers

Example:
- Partition 0 → Leader: Broker 1, Replica: Broker 2
- Partition 1 → Leader: Broker 2, Replica: Broker 3
- Partition 2 → Leader: Broker 3, Replica: Broker 1

3. Now, Replica broker also need to know about Leader and ISR (insync replica). So it also have inmemory cache in it

4. This metadata is written into ZooKeeper under another node `/topics`.. Even though controller maintains this.. If controller dies next controller need to know this metadata info.. so it is written in zookeeper

Final ZooKeeper structure:
```
/
├── controller
├── brokers
│   ├── ids
│   │   ├── 1
│   └── topics
│       └── my-topic
└── config
```

**Note:** Zookeeper has also another node `/config` which has configurations like retention

---

## Failure Scenarios

### What if controller dies

**Step 1:** `/controller` node deleted and particular id under brokers → ids is deleted
```
/
├── brokers
│   ├── ids
│   │   ├── 1
│   └── topics
│       └── my-topic
└── config
```

**Step 2:** 
- ZooKeeper fires a watch event to all brokers
- All alive brokers immediately race to create `/controller`. If succeeds it becomes controller

```
/
├── controller
├── brokers
│   ├── ids
│   │   ├── 1
│   └── topics
│       └── my-topic
└── config
```

### What if broker dies

**Zookeeper part:**
- As we know each broker has node in zookeeper `/brokers/ids/[id]`
- That node will be deleted. Controller will be notified by zookeeper

**Controller part:**
1. Look up all partitions where broker X was leader or replica
2. **Leader re-election:**
   - Controller takes In-Sync Replica list and finds the replica caught up with Leader. Elect that replica as new leader
   - Brokers receive a request from the controller to update Leader and ISR (inmemory cache)
3. **For partitions where X was only a follower:**
   - Just remove it from ISR
4. Send request to update the zookeeper metadata

---

## Producers

Producer is the application where you need to send the data to downstream

### How it is connected with broker: using ProducerConfiguration
```java
@Configuration
public class KafkaProducerConfig {
    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); //brokers used only on bootstrap
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        return new DefaultKafkaProducerFactory<>(config);
    }
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
```

**Note:** Actual connection happens lazily on the first send() request.

### How data is sent internally: 
Binary format (Messages are serialized into bytes using serializer... to avoid overheads present in http)

**Serializers:**
- **Built-in:** String, Integer, Long, Double, ByteBuffer, ByteArray
- **Schema based:** Avro, Protobuf, Json

### How message is sent:
Need to say topic, key, value, partition, header, timestamp
```java
kafkaTemplate.send("orders", "order-123", "Order placed");
```

### How partition is decided:
Partitioner will decided based on key
- **If key is given:** `partition = hash(key) % number_of_partitions`
- **If not:** Round Robin

Producer adds topic + partition number into the message metadata.
Message reaches broker, broker knows about topic and partition.

### What if message is not sent to broker:
We can configure retries in config
We can also configure ack in config. So broker will acknowledge back
- `acks=0` → producer doesn't wait (fastest, but risky)
- `acks=1` → broker leader confirms write (default)
- `acks=all` → leader + all ISR replicas confirm (safest)

### What if duplicate message is sent:
We can configure Idempotence in config
```
enable.idempotence=true
```
- Producer connects → broker assigns PID
- Producer keeps a sequence number counter for each partition it writes to
- Every message sent carries PID + partition sequence number

### How message is sent to broker having leader partition:
On the first send():
1. Producer picks any broker from bootstrap.servers
2. Opens TCP connection to that broker
3. Sends a Metadata request → asks for:
   - List of brokers
   - Topics and partitions
   - Leaders for each partition
   - ISR (in-sync replicas)
4. Producer caches this metadata
5. Producer chooses partition (via key or round-robin) and finds the leader broker from metadata
6. Sends the serialized message directly to the leader broker with partition
7. Broker appends message to next offset for the partition (offset is assigned by the broker, not the producer)

**Flow from producer:**
Producer → Metadata → Partitioner → Leader → Followers → Ack

---

## Consumers

Consumer is a application which try to read data from broker

### Consumer Configuration

```java
@Configuration
public class KafkaConsumerConfig {
    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "my-group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        
        return new DefaultKafkaConsumerFactory<>(config);
    }
    
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}

@Service
public class OrderConsumer {
    @KafkaListener(topics = "orders", groupId = "my-group")
    public void consume(String message) {
        System.out.println("Consumed message: " + message);
    }
}
```

Unlike producer, consumer need to do something before finding the leader broker

### Consumer Group Coordination Process:
1. Once consumer connects to broker using bootstrap details
2. One of the broker is chosen as **group coordinator** based on hashing the groupId
3. Group coordinator is responsible to:
   - Keep track of which consumers are alive
   - Rebalancing the consumer based on partition - mapping of partitions to consumer
4. Consumer sends request coordinator to join the group
5. Based on Assignment strategies, The Group Coordinator decides which partitions, consumer will consume
6. **Consumer joins/leaves → GC detects change → triggers rebalance → partitions reassigned → consumers resume**

Now consumer will get this partition data from coordinator and knows which partition to consume

### Then it finds the leader:
1. Consumer picks any broker from bootstrap.servers
2. Opens TCP connection to that broker
3. Sends a Metadata request → asks for:
   - List of brokers
   - Topics and partitions
   - Leaders for each partition
   - ISR (in-sync replicas)
4. Consumer caches this metadata

### Offset Management:
Now consumer know which broker and which partition.
So in that broker there should be a folders created based on topic and partition.

**But how consumer knows which offset and even also if consumer maintains that offset.. what if it dies?**

There comes `__consumer_offsets` - It is a topic (like all other)

**When it is created:** once consumer commit the offset (manually or automatically)

Once consumer join the group, Group Coordinator not only reply with which partition to consumer. It also checks the last committed offset for that partition in `__consumer_offsets` topic.

From second time onwards consumer will send req to broker with topic, partition and offset

---

## Data Storage & Retrieval

Now we know where data is stored.. now we need to know how it is stored and how consumer poll it based in offset

Now consumer requests with partition, offset to leader broker... leader broker will go to that path basically that a logging path.. that's why we say kafka is created for logging system

### File Structure:
Our message will be added as `.log` under the topic-partition folder
```
00000000000000000000.log
00000000000000000000.index
00000000000000000000.timeindex
```

- `.log` → actual messages in binary (with offsets, keys, values, headers)
- `.index` → mapping offset → file position (for fast lookup)
- `.timeindex` → mapping timestamp → offset (for time-based queries)

### Message Storage:
`00000000000000000000.log` is not one message = one file.
Each message inside that .log file gets a unique offset within the partition:

```
00000000000000000000.log
   Offset 0 → first message
   Offset 1 → second message
   Offset 2 → third message
```

### How many offset can the file hold?
Kafka has a broker config:
`log.segment.bytes` → default = 1 GB (it is each file size... not total size)

So number of offset is based on the size of message.
Let's say Average message size = 1 KB, so 1 GB / 1 KB = ~1,000,000 messages (offsets)

Once file reached 1GB size... new file will be created:
```
00000000000000001001.log
00000000000000001001.index
00000000000000001001.timeindex
```

### So when the file is deleted?
It's based on retention policy
If retention = 3GB, Kafka keeps at most 3 segments (log files).

### Message Format:
```
log file: [Record 0] [Record 1] [Record 2] ... [Record N]
Record: [offset][record-size][crc][timestamp][key-size][key][value-size][value][header-count][headers]
```

So:
```
[offset][record-size][crc][timestamp][key-size][key][value-size][value][header-count][headers]...
[offset][record-size][crc][timestamp][key-size][key][value-size][value][header-count][headers]...
```

---

## Security
SSL(keystore, truststore)
security.protocol=SSL
ssl.keystore.location=/etc/kafka/client.keystore.jks
ssl.keystore.password=clientpass
ssl.truststore.location=/etc/kafka/client.truststore.jks
ssl.truststore.password=trustpass

SASL(username, password hashing)
security.protocol=SASL_SSL
sasl.mechanism=SCRAM-SHA-512
sasl.jaas.config=org.apache.kafka.common.security.scram.ScramLoginModule required \
    username="producer1" \
    password="secret";

## Performance: How Kafka is Fast?

It uses **zero copy**

When consumer request for data below happens:

| Step                          | Normal   | Kafka Zero-Copy |
| ----------------------------- | ------   | --------------- |
| Disk → OS                     | yes      | yes             |
| OS → Broker memory            | yes      | (skipped)       |
| Broker memory → Socket buffer | yes      | (skipped)       |
| Socket → NIC → Consumer       | yes      | yes             |

allowing data to move directly from disk page cache to the network socket without copying into JVM memory.