Thread
Object created in Java
Each thread is mapped to Os level thread
Scheduler assigns that thread to Execution unit present in core
Execution unit executes the instruction

1. What is a Object Monitor?
Definition: A monitor is a built-in lock mechanism in each object to make a method to be accessed by single thread in a time.

Key Points:
Every Java object automatically has a monitor (you can see notify(), wait(), notifyAll() etc.. in Object)
Jvm identifies, the object need to be aquired by monitory by synchronized keyword
synchronized keyword activates the monitor

Monitor States:
FREE: Available for any thread to acquire
OCCUPIED: Currently held by one thread

How it works:
Thread try to access a method. Monitor moves from Free to Occupied state. 
When another try access same method, the 2nd thread will go to Blocked state
once first thread completes, Monitor will change to Free state. Then second will resume


2) synchronized vs synchronized block

public void synchronized getData(){
    // some opeartion
}

public void getData(){
    synchronized(this){
        // some opeartion
    }
}
both are same

Based on object, monitor is aquired.

3) independent lock

In above example, If I have 2 synchronized methods
//Data.class
public synchronized void getData() {
    // some opeartion
 }
    
public synchronized void addData() {
    // some opeartion
}

//main.class
Data d = new Data()
new Thread(() -> d.getData()).start(); // object (d) will be in OCCUPIED state
new Thread(() -> d.addData()).start(); // this will blocked until object (d) moves to FREE state.

This affects parallelism. To avoid this we can have independent object which can have its own lock

//Data.class
Object readLock = new Object();
Object writeLock = new Object();
public void getData() {
    synchronized(readLock){
        // some opeartion
    }
 }
public void addData() {
    synchronized(writeLock){
        // some opeartion
    }
 }

 //main.class
Data d = new Data()
new Thread(() -> d.getData()).start();
new Thread(() -> d.addData()).start();
nothing will be blocked now


so independent lock helps parallelism.


4)Important methods

1) start() and join()
 Thread t = new Thread(run);
 Thread t1 = new Thread(run);
 t.start(); // starts the thread
 t.join(); // stops code execution here util t completes
 System.out.println("t completed");


2) wait() and notifyAll()
public synchronized void addItem(String item) {
    counter.put(item, counter.getOrDefault(item, 0) + 1);
    System.out.println("Added: " + item);
    notifyAll(); // notify a waiting thread
}

public synchronized void getItem(String item) throws InterruptedException {
    while (!counter.containsKey(item)) {
        System.out.println("Waiting for " + item);
        wait(); // wait for the item to be available
    }
    System.out.println("Got: " + item + " -> " + counter.get(item));
}

3) Executor service
ExecutorService executor = Executors.newFixedThreadPool(5);
for(int i=1; i<=4; i++) {
    executor.submit(run);
}
executor.shutdown();



5) why completeble future

String userData = callSlowDatabaseAPI(userId); // 5 seconds

CompletableFuture<String> userData = CompletableFuture.supplyAsync(() -> 
        callSlowDatabaseAPI(userId)  // Runs in background
);

CompletableFuture is perfect for I/O operations.


6) More threads for computation

more threads ≠ better performance for computation

When CPU switches between threads, it must:
Each thread has its own call stack, program counter, and local variables.
When the CPU switches from one thread to another, it has to save the current thread’s state and load the next thread’s state.

why CPU switches between threads, cant it do multiprocessing
for 4 cores only 4 threads can run at parallel. If you createdd more thread than that for computation the it need to switch



ThreadLocal
ThreadLocal provides a way to store data to a specific thread

public class UserContext {
    // ThreadLocal to hold userId per thread
    private static ThreadLocal<String> userIdThreadLocal = new ThreadLocal<>();

    public static void setUserId(String userId) {
        userIdThreadLocal.set(userId);
    }
}


Semaphore

Synchronized = 1 thread 
Semaphore = multiple fixed threads

uses:
acquire() and release()