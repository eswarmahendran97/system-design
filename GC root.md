# GC root


## Basic misassuption
When you say
fun(){
    Object o = new Object();
}
new Object() is stored in heap
o is stored in stack as reference

its wrong

actual one:
o (reference) is stored in GC root


## Explanation
in above case, When a method is running, the JVM creates a stack frame for that method (in stack memory). 
Inside it, JVM stores the reference. Once method ends the frame is destroyed, so reference will not be present(this is what we say as object is dereffernced)

the place where reference is present we call it as root
stack frame is the GC root here


## Areas of object creation
Above case, since it is local variable, frame is created in stack. There are other areas where object can be created.

### Local variable
m(){
    Object o = new Object();
}
reference will be inside stack memory


### static variable
static Object s = new Object();
reference will be inside metaspace


### Thread object
Thread t = new Thread(() -> {
    Object o = new Object();
});
t.start();
reference will be inside stack memory for thread


### JNI
long handle = NativeLib.createObject();
reference will be inside Native Method Stack

All these objects memory is allocated only in heap. So GC to need to manage the memory differently for all. So we can types of GC root


## Types of GC Roots

Stack Frames (per thread)
Local variables, method parameters, and temporaries.
Stack frame itself = GC Root; references inside frame point to heap objects.

Static Variables
Stored in metaspace
References in static fields → objects in heap are reachable.

Active Threads
Thread objects themselves are GC Roots.
References in their stack frames are reachable.

JNI References / Native Handles
References from native code.
JNI or native code can keep heap objects alive until released.


### How GC runs

GC maintains a list of all GC Roots:
    Active stack frames of all threads
    Static variables in classes
    Active threads
    JNI references

Mark Phase
While running GC (when eden is full or old gen is full)
GC traverses all roots and follows the references to heap objects.
Each reachable object → marked as alive (flag in object header).

Sweep Phase
GC scans the entire heap.
Objects not marked → unreachable → memory is reclaimed.