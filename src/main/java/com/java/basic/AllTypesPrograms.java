package com.java.basic;

import java.math.BigDecimal;

public class AllTypesPrograms {

    public static void main(String[] args) {

        // -------------------------------
        // DOUBLE vs Double
        // -------------------------------

    	
        // Use double (primitive) for fast arithmetic with fractional numbers.
        // But be aware of floating-point precision issues.
        double a = 0.1 + 0.2;
        System.out.println(a); // 0.30000000000000004 - floating-point rounding error

        // double cannot be null
        // double b = null; // ❌ Compiler Error: incompatible types

        // double will automatically append .0 for integers
        double c = 10;
        System.out.println(c); // 10.0

        // Double (wrapper class) can be null. Useful in collections or objects
        Double salary = null; // ✅ Allowed
        System.out.println(salary); // null

        // -------------------------------
        // BIGDECIMAL
        // -------------------------------

        // BigDecimal is used for precise decimal arithmetic (money, currency)
        // Avoid using float/double for money.
        // BigDecimal salaryB = 10000; // ❌ Type mismatch: must use constructor
        BigDecimal salaryB = new BigDecimal("5000"); // ✅ Use String constructor to avoid precision errors
        System.out.println(salaryB);

        BigDecimal salaryB1 = new BigDecimal("50000.50");
        System.out.println(salaryB1);

        // Compare BigDecimal properly using compareTo
        boolean compareTo = salaryB1.compareTo(new BigDecimal("50000")) > 0;

        if (compareTo) {
            System.out.println("Salary is greater than 50000");
        }

        // -------------------------------
        // PRIMITIVE TYPES vs WRAPPER CLASSES
        // -------------------------------

        int num = 10; // primitive int, faster and memory-efficient
        Integer numObj = null; // wrapper Integer, can be null, used in Collections

        float pi = 3.14f; // primitive float, use 'f' suffix
        Float piObj = 3.14f; // wrapper Float, can be null

        // Wrong type assignment examples
        // int x = 3.5; // ❌ Type mismatch
        // BigDecimal bd = 10; // ❌ Must use constructor BigDecimal(String)

        // -------------------------------
        // CONTROL FLOW EXAMPLES
        // -------------------------------

        int score = 75;

        // if-else
        if (score >= 90) {
            System.out.println("Grade A");
        } else if (score >= 75) {
            System.out.println("Grade B");
        } else {
            System.out.println("Grade C");
        }

        // while loop
        int i = 0;
        while (i < 3) {
            System.out.println("While loop iteration: " + i);
            i++;
        }

        // do-while loop
        int j = 0;
        do {
            System.out.println("Do-while iteration: " + j);
            j++;
        } while (j < 3);

      
        
    }
}