package com.master.oops;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/*
 * ============================================================
 *              JAVA MASTER CLASS - ALL IN ONE
 * ============================================================
 * Covers:
 * 1. Data Types (Primitive & Reference)
 * 2. JVM Memory Concept (Stack & Heap)
 * 3. Type Casting (Widening & Narrowing)
 * 4. Loops (for, while, do-while, nested)
 * 5. OOPS (Encapsulation, Abstraction, Inheritance, Polymorphism)
 * 6. Interface & Abstract Class
 * 7. Compile-time & Runtime Polymorphism
 * 8. Exception Handling (Checked & Unchecked)
 * 9. SOLID Principles (Conceptual demonstration)
 * 10. Clean Code Practices
 */

public class JavaProMasterClass {

    /*
     * ============================================================
     * 1️⃣ PRIMITIVE DATA TYPES (Stored in Stack Memory)
     * ============================================================
     */
    static void primitiveExamples() {

        int a = 10;           // 4 bytes
        double b = 10.5;      // 8 bytes (IEEE 754)
        char c = 'A';         // 2 bytes
        boolean flag = true;  // true/false

        // Floating precision issue (binary representation problem)
        double x = 0.1 + 0.2;
        System.out.println("Floating precision: " + x); 
        // 0.30000000000000004

        // BigDecimal avoids precision issues (stored in heap)
        BigDecimal bd = new BigDecimal("0.1")
                .add(new BigDecimal("0.2"));
        System.out.println("BigDecimal precise: " + bd);
    }

    /*
     * ============================================================
     * 2️⃣ TYPE CASTING
     * ============================================================
     */
    static void castingExamples() {

        // Widening (Implicit)
        int a = 10;
        double b = a;  // int → double (safe)

        // Narrowing (Explicit)
        double x = 10.9;
        int y = (int) x; // data loss (10)

        System.out.println("Narrowed value: " + y);

        // Wrong casting (Runtime error example)
        try {
            Object obj = "Hello";
            Integer num = (Integer) obj;  // ClassCastException
        } catch (ClassCastException e) {
            System.out.println("Runtime casting error handled!");
        }
    }

    /*
     * ============================================================
     * 3️⃣ LOOPS
     * ============================================================
     */
    static void loopExamples() {

        // For loop (when iterations known)
        for (int i = 0; i < 3; i++) {
            System.out.println("For loop: " + i);
        }

        // While loop (condition-based)
        int j = 0;
        while (j < 3) {
            System.out.println("While loop: " + j);
            j++;
        }

        // Do-While (runs at least once)
        int k = 0;
        do {
            System.out.println("Do-While: " + k);
            k++;
        } while (k < 3);

        // Nested loop (careful: O(n²))
        for (int i = 1; i <= 2; i++) {
            for (int m = 1; m <= 2; m++) {
                System.out.println("Nested Loop: " + i + "," + m);
            }
        }
    }

    /*
     * ============================================================
     * 4️⃣ OOPS CONCEPTS
     * ============================================================
     */

    // -------------------------
    // Encapsulation
    // -------------------------
    static class Employee {

        private int id;           // data hidden
        private String name;
        private BigDecimal salary;

        // Constructor
        public Employee(int id, String name, BigDecimal salary) {
            this.id = id;
            this.name = name;
            this.salary = salary;
        }

        // Getter (Controlled access)
        public BigDecimal getSalary() {
            return salary;
        }

        // Compile-time Polymorphism (Method Overloading)
        public BigDecimal calculateBonus(int percent) {
            return salary.multiply(BigDecimal.valueOf(percent))
                         .divide(BigDecimal.valueOf(100));
        }

        public BigDecimal calculateBonus(double percent) {
            return salary.multiply(BigDecimal.valueOf(percent / 100));
        }

        // Runtime Polymorphism
        public BigDecimal calculateSalary() {
            return salary;
        }
    }

    // -------------------------
    // Inheritance
    // -------------------------
    static class Manager extends Employee {

        public Manager(int id, String name, BigDecimal salary) {
            super(id, name, salary);
        }

        // Runtime Polymorphism (Overriding)
        @Override
        public BigDecimal calculateSalary() {
            return getSalary().add(new BigDecimal("10000"));
        }
    }

    // -------------------------
    // Abstraction using Interface
    // -------------------------
    interface SalaryValidator {
        void validate(BigDecimal salary) throws Exception;
    }

    static class BasicSalaryValidator implements SalaryValidator {

        @Override
        public void validate(BigDecimal salary) throws Exception {
            if (salary.compareTo(BigDecimal.ZERO) < 0) {
                throw new Exception("Salary cannot be negative!");
            }
        }
    }

    /*
     * ============================================================
     * 5️⃣ EXCEPTION HANDLING
     * ============================================================
     */
    static void exceptionExamples() {

        // Checked Exception
        try {
            throwCheckedException();
        } catch (IOException e) {
            System.out.println("Checked Exception handled");
        }

        // Unchecked Exception
        try {
            int a = 10 / 0;
        } catch (ArithmeticException e) {
            System.out.println("Runtime Exception handled");
        }
    }

    static void throwCheckedException() throws IOException {
        throw new IOException("IO problem");
    }

    /*
     * ============================================================
     * 6️⃣ SOLID PRINCIPLES (Conceptual in single class)
     * ============================================================
     *
     * S - Single Responsibility:
     *      Employee handles employee data only.
     *
     * O - Open/Closed:
     *      Manager extends Employee without modifying Employee.
     *
     * L - Liskov Substitution:
     *      Employee e = new Manager(); works correctly.
     *
     * I - Interface Segregation:
     *      SalaryValidator is small and focused.
     *
     * D - Dependency Inversion:
     *      Code depends on SalaryValidator interface,
     *      not BasicSalaryValidator concrete class.
     */

    public static void main(String[] args) {

        primitiveExamples();
        castingExamples();
        loopExamples();
        exceptionExamples();

        // OOPS Practical Demonstration
        Employee emp = new Employee(1, "John", new BigDecimal("50000"));
        Employee mgr = new Manager(2, "Alice", new BigDecimal("70000"));

        // Runtime Polymorphism
        System.out.println("Employee Salary: " + emp.calculateSalary());
        System.out.println("Manager Salary: " + mgr.calculateSalary());

        // Compile-time Polymorphism
        System.out.println("Bonus int: " + emp.calculateBonus(10));
        System.out.println("Bonus double: " + emp.calculateBonus(10.5));

        // Interface usage
        SalaryValidator validator = new BasicSalaryValidator();
        try {
            validator.validate(emp.getSalary());
            System.out.println("Salary validated!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // Clean Code Practice:
        // 1. Meaningful names
        // 2. Small methods
        // 3. Separation of concerns
        // 4. Avoid hardcoding in real systems
    }
}