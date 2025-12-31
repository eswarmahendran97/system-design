
Snapshot Array (LeetCode 1146)
    HashMap<index, TreeMap<snapId, value>>

LRU Cache (LeetCode 146)
    HashMap + Doubly Linked List

LFU Cache (LeetCode 460)
    HashMap + Doubly Linked List + Min Heap (or nested HashMap)


Design an in-memory leaderboard
Requirement:
Insert scores
Update scores
Get top-K players
Get rank of a player in O(log n)


Build a real-time autocomplete system
Requirement:
Prefix search
Top 3 or top K most frequent searches
Fast insertion and update


5️⃣ Implement a text editor with undo/redo
Requirement:
Insert/delete characters
Undo O(1)
Redo O(1)


9️⃣ Efficiently store and compress repeating data

Requirement:
Store AAABBCCDDDD → 3A2B2C4D
Quick decompression
Expected DS:
Run Length Encoding (RLE)

List<Pair<char, count>>

List<Object>{char c, int startIndex}
Just like you correctly mentioned.


4. Range Sum Query - ImmutableQuestion: Given an immutable array of numbers, handle multiple queries where each query asks for the sum of elements between index $i$ and $j$ (inclusive). How do you preprocess the data to answer queries quickly?


6. Design Hit Counter
Requirements:

Record hits at timestamp
Get hits in last 5 minutes
Handle high volume

DS: Circular Buffer/Queue with timestamps OR TreeMap<timestamp, count> with cleanup 