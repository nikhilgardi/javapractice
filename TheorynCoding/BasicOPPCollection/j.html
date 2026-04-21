import React, { useState } from 'react';
import { ChevronDown, ChevronRight, Code, Database, Layers, BookOpen, Cpu, Settings, Star, AlertCircle, Filter } from 'lucide-react';

const JavaInterviewQA = () => {
  const [selectedTopic, setSelectedTopic] = useState('coreJava');
  const [selectedDifficulty, setSelectedDifficulty] = useState('All');
  const [expandedQuestions, setExpandedQuestions] = useState({});

  const toggleQuestion = (questionId) => {
    setExpandedQuestions(prev => ({
      ...prev,
      [questionId]: !prev[questionId]
    }));
  };

  const getDifficultyColor = (difficulty) => {
    switch(difficulty) {
      case 'Simple': return 'bg-green-100 text-green-800';
      case 'Tricky': return 'bg-yellow-100 text-yellow-800';
      case 'Advanced': return 'bg-orange-100 text-orange-800';
      case 'Tough': return 'bg-red-100 text-red-800';
      default: return 'bg-gray-100 text-gray-800';
    }
  };

  const questionCategories = {
    coreJava: {
      title: "Core Java",
      icon: <Code className="w-5 h-5" />,
      color: "bg-blue-500",
      questions: [
        {
          id: 1,
          difficulty: "Simple",
          question: "What is the difference between JDK, JRE, and JVM?",
          answer: `JDK (Java Development Kit):
- Complete development environment
- Contains JRE + development tools (javac, javadoc, jar, etc.)
- Used for developing Java applications

JRE (Java Runtime Environment):
- Runtime environment for executing Java programs
- Contains JVM + core libraries + supporting files
- Used for running Java applications

JVM (Java Virtual Machine):
- Runtime engine that executes Java bytecode
- Platform-specific implementation
- Provides platform independence

Example Flow:
Source Code (.java) → [javac] → Bytecode (.class) → [JVM] → Machine Code

Relationship:
JDK = JRE + Development Tools
JRE = JVM + Core Libraries

Interview Tip: JDK is for developers, JRE is for end users, JVM is the execution engine.`
        },
        {
          id: 2,
          difficulty: "Simple", 
          question: "What are the main principles of OOP?",
          answer: `Four Main Principles of OOP:

1. Encapsulation:
- Bundling data and methods together
- Hiding internal details using access modifiers

Example:
class BankAccount {
    private double balance; // Hidden from outside
    
    public void deposit(double amount) { // Controlled access
        if(amount > 0) {
            balance += amount;
        }
    }
    
    public double getBalance() { // Read-only access
        return balance;
    }
}

2. Inheritance:
- Creating new classes based on existing classes
- Code reusability and "is-a" relationship

Example:
class Animal {
    protected String name;
    public void eat() { System.out.println("Animal eats"); }
}

class Dog extends Animal {
    public void bark() { System.out.println("Dog barks"); }
}

3. Polymorphism:
- Same interface, different implementations
- Method overriding and overloading

Example:
class Animal {
    public void makeSound() { System.out.println("Some sound"); }
}

class Dog extends Animal {
    @Override
    public void makeSound() { System.out.println("Woof!"); }
}

// Runtime polymorphism
Animal animal = new Dog();
animal.makeSound(); // Output: Woof!

4. Abstraction:
- Hiding implementation details, showing only essential features

Example:
abstract class Shape {
    public abstract double calculateArea(); // Abstract method
    
    public void display() { // Concrete method
        System.out.println("Area: " + calculateArea());
    }
}

Benefits:
- Code reusability
- Maintainability
- Modularity
- Security`
        },
        {
          id: 3,
          difficulty: "Tricky",
          question: "What is the output of this code and why?",
          answer: `Tricky Code Example:

public class TrickyCode {
    public static void main(String[] args) {
        Integer a = 100;
        Integer b = 100;
        Integer c = 200;
        Integer d = 200;
        
        System.out.println(a == b);  // What will this print?
        System.out.println(c == d);  // What will this print?
        
        String s1 = new String("Hello");
        String s2 = new String("Hello");
        String s3 = "Hello";
        String s4 = "Hello";
        
        System.out.println(s1 == s2);  // What will this print?
        System.out.println(s3 == s4);  // What will this print?
        
        System.out.println(s1.equals(s2));  // What will this print?
    }
}

Output:
true
false
false
true
true

Explanation:

Integer Caching:
- Java caches Integer objects from -128 to 127
- a == b returns true because both reference the same cached object
- c == d returns false because 200 is outside cache range, so different objects are created

Integer a = 100;  // Uses cached object
Integer b = 100;  // Uses same cached object
Integer c = 200;  // Creates new object
Integer d = 200;  // Creates another new object

String Pool:
- String literals are stored in String Pool
- new String() always creates new objects in heap
- s1 == s2 is false (different objects in heap)
- s3 == s4 is true (same object in string pool)
- equals() compares content, so always true for same strings

More Tricky Examples:

// Tricky with method calls
public class Tricky2 {
    static int x = 10;
    
    static {
        x = 20;  // Static block executes first
    }
    
    public static void main(String[] args) {
        System.out.println(x);  // Output: 20
        
        // Tricky with arrays
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        System.out.println(arr1 == arr2);        // false
        System.out.println(arr1.equals(arr2));   // false (Object.equals)
        System.out.println(Arrays.equals(arr1, arr2)); // true
    }
}

Interview Tips:
- Always consider autoboxing/unboxing
- Remember == compares references for objects
- Static blocks execute before main method
- Arrays don't override equals() method`
        },
        {
          id: 4,
          difficulty: "Advanced",
          question: "Explain HashMap collision handling and internal working with Java 8 changes.",
          answer: `HashMap Internal Structure:

Pre-Java 8:
- Array of LinkedList nodes
- Collision handling: Chaining with LinkedList
- Worst case time complexity: O(n)

Java 8+ Improvements:
- Array of LinkedList/TreeNode nodes
- Converts to Red-Black Tree when bucket size ≥ 8
- Converts back to LinkedList when size ≤ 6
- Worst case time complexity: O(log n)

Detailed Implementation:

// Simplified HashMap structure
class MyHashMap<K, V> {
    static class Node<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K, V> next;
        
        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
    
    Node<K, V>[] table;
    int size;
    int threshold;
    final float loadFactor;
    
    // Hash function
    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
    
    // Get bucket index
    static int indexFor(int hash, int length) {
        return hash & (length - 1); // Equivalent to hash % length
    }
}

Put Operation Step by Step:

public V put(K key, V value) {
    // 1. Calculate hash
    int hash = hash(key);
    
    // 2. Find bucket index
    int index = indexFor(hash, table.length);
    
    // 3. Handle collision
    Node<K, V> node = table[index];
    
    if (node == null) {
        // No collision: create new node
        table[index] = new Node<>(hash, key, value, null);
    } else {
        // Collision exists: traverse chain
        while (node != null) {
            if (node.hash == hash && 
                (node.key == key || (key != null && key.equals(node.key)))) {
                // Key exists: update value
                V oldValue = node.value;
                node.value = value;
                return oldValue;
            }
            
            if (node.next == null) {
                // End of chain: add new node
                node.next = new Node<>(hash, key, value, null);
                break;
            }
            node = node.next;
        }
    }
    
    // 4. Check if resize needed
    if (++size > threshold) {
        resize();
    }
    
    return null;
}

Tree Conversion (Java 8+):

// When bucket size >= TREEIFY_THRESHOLD (8)
static final int TREEIFY_THRESHOLD = 8;
static final int UNTREEIFY_THRESHOLD = 6;

// Conversion logic (simplified)
if (binCount >= TREEIFY_THRESHOLD - 1) {
    treeifyBin(table, hash); // Convert to Red-Black Tree
}

Key Performance Factors:

1. Good Hash Function:
// Good distribution
@Override
public int hashCode() {
    return Objects.hash(field1, field2, field3);
}

2. Proper Load Factor:
- Default: 0.75 (75% full before resize)
- Balance between space and time

3. Power of 2 Sizing:
- Allows efficient bitwise operations
- hash & (n-1) instead of hash % n

Common Interview Questions:

Q: Why is HashMap not thread-safe?
A: Multiple threads can cause infinite loops during resize operation, leading to CPU 100% usage.

Q: When does HashMap become TreeMap-like?
A: When a single bucket has ≥8 nodes, it converts to Red-Black Tree for O(log n) performance.

Q: Why load factor 0.75?
A: Optimal balance between time and space complexity based on Poisson distribution.`
        },
        {
          id: 5,
          difficulty: "Tough",
          question: "Create a thread-safe Singleton with lazy initialization that handles reflection and serialization attacks.",
          answer: `Ultimate Thread-Safe Singleton:

This implementation handles all possible attacks and edge cases:

import java.io.Serializable;
import java.lang.reflect.Constructor;

public final class UltimateSingleton implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Volatile ensures visibility of instance creation across threads
    private static volatile UltimateSingleton instance;
    
    // Flag to prevent reflection attacks
    private static boolean instanceCreated = false;
    
    // Private constructor to prevent instantiation
    private UltimateSingleton() {
        synchronized (UltimateSingleton.class) {
            if (instanceCreated) {
                throw new RuntimeException("Cannot create instance using reflection. Use getInstance() method.");
            }
            instanceCreated = true;
        }
    }
    
    // Double-checked locking for thread safety and lazy initialization
    public static UltimateSingleton getInstance() {
        if (instance == null) { // First check (no locking)
            synchronized (UltimateSingleton.class) {
                if (instance == null) { // Second check (with locking)
                    instance = new UltimateSingleton();
                }
            }
        }
        return instance;
    }
    
    // Prevent serialization attacks
    protected Object readResolve() {
        return getInstance();
    }
    
    // Prevent cloning
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Cloning is not supported for Singleton class");
    }
    
    // Business methods
    public void doSomething() {
        System.out.println("Doing something with singleton instance: " + this.hashCode());
    }
}

Alternative: Enum Singleton (Recommended by Joshua Bloch):

public enum EnumSingleton {
    INSTANCE;
    
    private String data;
    
    private EnumSingleton() {
        data = "Initial Data";
    }
    
    public void doSomething() {
        System.out.println("Enum singleton: " + data);
    }
    
    public void setData(String data) {
        this.data = data;
    }
}

// Usage
EnumSingleton.INSTANCE.doSomething();

Alternative: Initialization-on-demand Holder Pattern:

public class HolderSingleton {
    
    private HolderSingleton() {
        // Prevent reflection attacks
        if (SingletonHolder.INSTANCE != null) {
            throw new IllegalStateException("Instance already created!");
        }
    }
    
    // Inner class loaded only when getInstance() is called
    private static class SingletonHolder {
        private static final HolderSingleton INSTANCE = new HolderSingleton();
    }
    
    public static HolderSingleton getInstance() {
        return SingletonHolder.INSTANCE; // Thread-safe, lazy, no synchronization overhead
    }
}

Key Points for Interviews:

1. Double-Checked Locking requires volatile keyword
2. Enum Singleton is the best approach (handles all attacks automatically)
3. Holder Pattern is most efficient (no synchronization overhead)
4. Reflection attacks need explicit protection
5. Serialization requires readResolve() method
6. Thread safety is crucial in multi-threaded environments

Common Mistakes:
- Forgetting volatile keyword in double-checked locking
- Not handling serialization properly
- Not protecting against reflection attacks
- Using synchronized method instead of block (performance impact)`
        }
      ]
    },
    multithreading: {
      title: "Multithreading & Concurrency",
      icon: <Cpu className="w-5 h-5" />,
      color: "bg-red-500",
      questions: [
        {
          id: 6,
          difficulty: "Simple",
          question: "What are the different ways to create threads in Java?",
          answer: `Three Main Ways to Create Threads:

1. Extending Thread Class:
class MyThread extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(Thread.currentThread().getName() + " - " + i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

// Usage
public class ThreadExample1 {
    public static void main(String[] args) {
        MyThread thread1 = new MyThread();
        MyThread thread2 = new MyThread();
        
        thread1.setName("Thread-1");
        thread2.setName("Thread-2");
        
        thread1.start(); // Calls run() method
        thread2.start();
    }
}

2. Implementing Runnable Interface:
class MyTask implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(Thread.currentThread().getName() + " - " + i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

// Usage
public class ThreadExample2 {
    public static void main(String[] args) {
        MyTask task = new MyTask();
        
        Thread thread1 = new Thread(task, "Thread-1");
        Thread thread2 = new Thread(task, "Thread-2");
        
        thread1.start();
        thread2.start();
    }
}

3. Using Lambda Expressions (Java 8+):
public class ThreadExample3 {
    public static void main(String[] args) {
        // Lambda with Runnable
        Runnable task = () -> {
            for (int i = 0; i < 5; i++) {
                System.out.println(Thread.currentThread().getName() + " - " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        
        Thread thread1 = new Thread(task, "Lambda-Thread-1");
        Thread thread2 = new Thread(task, "Lambda-Thread-2");
        
        thread1.start();
        thread2.start();
        
        // Direct lambda
        new Thread(() -> {
            System.out.println("Direct lambda thread: " + Thread.currentThread().getName());
        }, "Direct-Lambda").start();
    }
}

Comparison:

Method | Pros | Cons | When to Use
Extending Thread | Simple, direct access to Thread methods | Single inheritance limitation | Simple threading tasks
Implementing Runnable | Better OOP design, can extend other classes | Need Thread wrapper | Most common approach
Lambda Expression | Concise, functional programming | Java 8+ only | Simple tasks, one-time use

Thread Lifecycle:
NEW → RUNNABLE → BLOCKED/WAITING/TIMED_WAITING → RUNNABLE → TERMINATED

Key Points:
- Always call start(), never run() directly
- Runnable is preferred over extending Thread
- Lambda expressions make code more concise
- Use join() to wait for thread completion
- Set appropriate thread names for debugging`
        }
      ]
    },
    database: {
      title: "Database & SQL",
      icon: <Database className="w-5 h-5" />,
      color: "bg-teal-500",
      questions: [
        {
          id: 7,
          difficulty: "Medium",
          question: "Explain different types of SQL JOINs with examples using Employee and Department tables.",
          answer: `Sample Tables:

Employee Table:
emp_id | name  | dept_id | salary
1      | John  | 10      | 50000
2      | Alice | 20      | 60000
3      | Bob   | 10      | 55000
4      | Carol | NULL    | 45000

Department Table:
dept_id | dept_name | location
10      | IT        | New York
20      | HR        | London
30      | Finance   | Paris

1. INNER JOIN:
Returns records that have matching values in both tables.

SELECT e.name, e.salary, d.dept_name, d.location
FROM Employee e
INNER JOIN Department d ON e.dept_id = d.dept_id;

Result:
name  | salary | dept_name | location
John  | 50000  | IT        | New York
Alice | 60000  | HR        | London
Bob   | 55000  | IT        | New York

Note: Carol (NULL dept_id) and Finance dept are excluded

2. LEFT JOIN (LEFT OUTER JOIN):
Returns all records from left table + matching records from right table.

SELECT e.name, e.salary, d.dept_name, d.location
FROM Employee e
LEFT JOIN Department d ON e.dept_id = d.dept_id;

Result:
name  | salary | dept_name | location
John  | 50000  | IT        | New York
Alice | 60000  | HR        | London
Bob   | 55000  | IT        | New York
Carol | 45000  | NULL      | NULL

Note: All employees included, Carol shows NULL for department info

3. RIGHT JOIN (RIGHT OUTER JOIN):
Returns all records from right table + matching records from left table.

SELECT e.name, e.salary, d.dept_name, d.location
FROM Employee e
RIGHT JOIN Department d ON e.dept_id = d.dept_id;

Result:
name  | salary | dept_name | location
John  | 50000  | IT        | New York
Bob   | 55000  | IT        | New York
Alice | 60000  | HR        | London
NULL  | NULL   | Finance   | Paris

Note: All departments included, Finance shows NULL for employee info

Performance Tips:
1. Use INNER JOIN when possible - fastest
2. Index join columns - significant performance improvement
3. Filter early - use WHERE clause before JOIN when possible
4. Avoid CROSS JOIN - usually indicates missing WHERE clause

Interview Traps:
- Remember that JOINs can return duplicate rows
- NULL values in join columns need special handling
- Understand the difference between WHERE and HAVING with JOINs`
        }
      ]
    },
    designPatterns: {
      title: "Design Patterns & SOLID",
      icon: <BookOpen className="w-5 h-5" />,
      color: "bg-purple-500",
      questions: [
        {
          id: 8,
          difficulty: "Medium",
          question: "Explain Singleton Pattern with different implementation approaches.",
          answer: `Singleton Pattern - Ensures only one instance of a class exists.

1. Eager Initialization:
public class EagerSingleton {
    private static final EagerSingleton instance = new EagerSingleton();
    
    private EagerSingleton() {}
    
    public static EagerSingleton getInstance() {
        return instance;
    }
}

Pros: Thread-safe, simple
Cons: Instance created even if not used, no lazy loading

2. Lazy Initialization (Not Thread-Safe):
public class LazySingleton {
    private static LazySingleton instance;
    
    private LazySingleton() {}
    
    public static LazySingleton getInstance() {
        if (instance == null) {
            instance = new LazySingleton(); // Problem in multithreading!
        }
        return instance;
    }
}

Problems: Not thread-safe, multiple instances possible

3. Thread-Safe Singleton:
public class ThreadSafeSingleton {
    private static ThreadSafeSingleton instance;
    
    private ThreadSafeSingleton() {}
    
    public static synchronized ThreadSafeSingleton getInstance() {
        if (instance == null) {
            instance = new ThreadSafeSingleton();
        }
        return instance;
    }
}

Pros: Thread-safe
Cons: Performance impact due to synchronization overhead

4. Double-Checked Locking:
public class DoubleCheckedSingleton {
    private static volatile DoubleCheckedSingleton instance;
    
    private DoubleCheckedSingleton() {}
    
    public static DoubleCheckedSingleton getInstance() {
        if (instance == null) { // First check
            synchronized (DoubleCheckedSingleton.class) {
                if (instance == null) { // Second check
                    instance = new DoubleCheckedSingleton();
                }
            }
        }
        return instance;
    }
}

Pros: Thread-safe, better performance than synchronized method
Important: volatile keyword is mandatory!

5. Bill Pugh Solution (Inner Class):
public class BillPughSingleton {
    private BillPughSingleton() {}
    
    private static class SingletonHelper {
        private static final BillPughSingleton INSTANCE = new BillPughSingleton();
    }
    
    public static BillPughSingleton getInstance() {
        return SingletonHelper.INSTANCE;
    }
}

Pros: Thread-safe, lazy loading, no synchronization overhead

6. Enum Singleton (Best Approach):
public enum EnumSingleton {
    INSTANCE;
    
    public void doSomething() {
        System.out.println("Doing something...");
    }
}

// Usage: EnumSingleton.INSTANCE.doSomething();

Pros: Thread-safe, handles serialization, prevents reflection attacks
Recommended by Joshua Bloch (Effective Java)

Use Cases:
- Database connections
- Logging
- Configuration settings
- Cache management

Interview Tips:
- Enum is the best approach for most cases
- Always mention thread safety concerns
- Understand the pros/cons of each approach
- Know how to handle serialization and reflection attacks`
        }
      ]
    },
    advancedJava: {
      title: "Advanced Java",
      icon: <Settings className="w-5 h-5" />,
      color: "bg-indigo-500",
      questions: [
        {
          id: 9,
          difficulty: "Advanced",
          question: "Explain Java Memory Model and Garbage Collection in detail.",
          answer: `Java Memory Model:

1. Heap Memory:
   - Young Generation:
     * Eden Space: New objects created here
     * Survivor Space (S0, S1): Objects that survived one GC
   - Old Generation (Tenured): Long-lived objects
   - Metaspace (Java 8+): Class metadata (replaced PermGen)

2. Non-Heap Memory:
   - Method Area: Class-level data, constants
   - Code Cache: Compiled native code
   - Direct Memory: NIO operations

3. Stack Memory (Per Thread):
   - Local variables and method parameters
   - Method call references

Example Memory Allocation:
class Person {
    private String name; // Reference in stack, object in heap
    private static int count = 0; // In method area
    
    public void createPerson() {
        Person p = new Person(); // 'p' in stack, object in heap
        String temp = "temp"; // In stack (if not interned)
    } // temp and p go out of scope
}

Garbage Collection Process:

1. Minor GC (Young Generation):
// Objects created in Eden space
List<String> list = new ArrayList<>();
for(int i = 0; i < 1000; i++) {
    list.add("String " + i); // Objects in Eden
}
// When Eden fills up, Minor GC triggered
// Surviving objects moved to S1

2. Major GC (Old Generation):
- When Old Generation fills up
- More expensive, stop-the-world event
- Objects promoted from Young to Old after several minor GCs

GC Algorithms:

1. Serial GC: Single-threaded, suitable for small applications
   -XX:+UseSerialGC

2. Parallel GC: Multi-threaded, default for server applications
   -XX:+UseParallelGC

3. G1 GC: Low-latency collector for large heaps
   -XX:+UseG1GC -XX:MaxGCPauseMillis=200

4. ZGC/Shenandoah: Ultra-low latency collectors

Memory Leak Example:
class MemoryLeak {
    private static List<Object> list = new ArrayList<>();
    
    public void addObject() {
        list.add(new Object()); // Objects never removed!
    }
}

GC Tuning Parameters:
-Xms512m          # Initial heap size
-Xmx2g            # Maximum heap size
-XX:NewRatio=3    # Old/Young generation ratio
-XX:+PrintGC      # Print GC information
-XX:+PrintGCDetails # Detailed GC information

Monitoring Tools:
- JVisualVM
- JConsole
- Eclipse MAT (Memory Analyzer Tool)
- JProfiler

Best Practices:
1. Avoid creating unnecessary objects in loops
2. Use object pools for expensive objects
3. Set appropriate heap sizes
4. Monitor GC logs regularly
5. Use StringBuilder for string concatenation
6. Close resources properly (try-with-resources)

Interview Tips:
- Understand different GC algorithms for different use cases
- Know how to identify memory leaks
- Explain object lifecycle from creation to collection
- Understand the impact of GC on application performance`
        }
      ]
    }
  };

  const filteredQuestions = (categoryKey) => {
    const category = questionCategories[categoryKey];
    if (!category) return [];
    
    return category.questions.filter(q => 
      selectedDifficulty === 'All' || q.difficulty === selectedDifficulty
    );
  };

  const topics = Object.keys(questionCategories);
  const difficulties = ['All', 'Simple', 'Tricky', 'Advanced', 'Tough'];

  return (
    <div className="max-w-7xl mx-auto p-6 bg-gray-50 min-h-screen">
      <div className="bg-white rounded-lg shadow-sm border mb-6 p-6">
        <h1 className="text-3xl font-bold text-gray-900 mb-2">Java Interview Questions & Answers Worldwide</h1>
        <p className="text-gray-600 mb-4">
          Comprehensive collection of questions from Simple to Tough level with detailed examples
        </p>
        <div className="bg-blue-50 p-4 rounded-lg">
          <p className="text-blue-800 text-sm">
            <strong>Difficulty Levels:</strong> Simple (Basic concepts), Tricky (Gotchas & edge cases), Advanced (Deep understanding), Tough (Expert level with complex implementations)
          </p>
        </div>
      </div>

      {/* Filters */}
      <div className="bg-white rounded-lg shadow-sm border mb-6 p-4">
        <div className="flex flex-wrap gap-4 items-center">
          <div className="flex items-center gap-2">
            <Filter className="w-4 h-4 text-gray-600" />
            <span className="font-medium text-gray-700">Filters:</span>
          </div>
          
          <div className="flex items-center gap-2">
            <label className="text-sm text-gray-600">Topic:</label>
            <select 
              value={selectedTopic} 
              onChange={(e) => setSelectedTopic(e.target.value)}
              className="border rounded px-3 py-1 text-sm"
            >
              {Object.entries(questionCategories).map(([key, category]) => (
                <option key={key} value={key}>{category.title}</option>
              ))}
            </select>
          </div>

          <div className="flex items-center gap-2">
            <label className="text-sm text-gray-600">Difficulty:</label>
            <select 
              value={selectedDifficulty} 
              onChange={(e) => setSelectedDifficulty(e.target.value)}
              className="border rounded px-3 py-1 text-sm"
            >
              {difficulties.map(diff => (
                <option key={diff} value={diff}>{diff}</option>
              ))}
            </select>
          </div>
        </div>
      </div>

      {/* Topic Navigation */}
      <div className="bg-white rounded-lg shadow-sm border mb-6">
        <div className="flex flex-wrap border-b">
          {Object.entries(questionCategories).map(([categoryKey, category]) => (
            <button
              key={categoryKey}
              onClick={() => setSelectedTopic(categoryKey)}
              className={`flex items-center gap-2 px-4 py-3 font-medium text-sm ${
                selectedTopic === categoryKey 
                  ? 'border-b-2 border-blue-500 text-blue-600 bg-blue-50' 
                  : 'text-gray-500 hover:text-gray-700 hover:bg-gray-50'
              }`}
            >
              <div className={`${category.color} text-white p-1 rounded`}>
                {category.icon}
              </div>
              {category.title}
            </button>
          ))}
        </div>

        {/* Questions */}
        <div className="p-6">
          {filteredQuestions(selectedTopic).length > 0 ? (
            <div className="space-y-4">
              {filteredQuestions(selectedTopic).map((question) => (
                <div key={question.id} className="border rounded-lg bg-gray-50">
                  <div 
                    className="flex items-center justify-between p-4 cursor-pointer hover:bg-gray-100"
                    onClick={() => toggleQuestion(question.id)}
                  >
                    <div className="flex-1">
                      <div className="flex items-center gap-3 mb-2">
                        <span className={`text-xs px-2 py-1 rounded-full ${getDifficultyColor(question.difficulty)}`}>
                          {question.difficulty}
                        </span>
                        <Star className="w-4 h-4 text-yellow-500" />
                      </div>
                      <h3 className="font-semibold text-gray-900">{question.question}</h3>
                    </div>
                    <div className="text-gray-400">
                      {expandedQuestions[question.id] ? <ChevronDown className="w-5 h-5" /> : <ChevronRight className="w-5 h-5" />}
                    </div>
                  </div>
                  
                  {expandedQuestions[question.id] && (
                    <div className="border-t bg-white p-4">
                      <div className="prose prose-sm max-w-none">
                        <pre className="whitespace-pre-wrap text-sm text-gray-700 font-sans">
                          {question.answer}
                        </pre>
                      </div>
                    </div>
                  )}
                </div>
              ))}
            </div>
          ) : (
            <div className="text-center py-8">
              <AlertCircle className="w-12 h-12 text-gray-400 mx-auto mb-4" />
              <p className="text-gray-600">No questions found for the selected filters.</p>
              <p className="text-sm text-gray-500 mt-2">Try selecting a different topic or difficulty level.</p>
            </div>
          )}
        </div>
      </div>

      {/* Summary Stats */}
      <div className="bg-gradient-to-r from-blue-600 to-purple-600 text-white p-6 rounded-lg">
        <h3 className="text-xl font-bold mb-3">Interview Preparation Tips</h3>
        <div className="grid md:grid-cols-3 gap-4 text-sm">
          <div>
            <h4 className="font-semibold mb-2">🎯 Practice Strategy</h4>
            <p>Start with Simple questions, then progress to Tricky and Advanced levels</p>
          </div>
          <div>
            <h4 className="font-semibold mb-2">💡 Key Focus Areas</h4>
            <p>Core Java, Multithreading, and Database questions are most frequently asked</p>
          </div>
          <div>
            <h4 className="font-semibold mb-2">🔄 Hands-on Practice</h4>
            <p>Code all examples yourself - understanding concepts through implementation</p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default JavaInterviewQA;