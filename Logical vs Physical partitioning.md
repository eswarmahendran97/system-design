Logical vs Physical partitioning

Physical:
Data is partitioned and stored in different physical memory or system

Logical:
It most like Application level filtering
All data might still be on the same physical nodes, just organized logically

if (userType === 'premium') {
  db.premium_users.insert(data);
} else {
  db.regular_users.insert(data);
}


How RUs affects physical partitioning
1 physical partition can support max 10,000 RUs and 50 GB storage.
Meaning:
If RUs exceed 10,000 → needs another physical partition
If storage > 50 GB → needs another physical partition


How to reduce RU consumption
1) Choose the Right Partition Key
2) Avoid Cross-Partition Queries (Major RU Killer)
3) Read using id and Partition keys