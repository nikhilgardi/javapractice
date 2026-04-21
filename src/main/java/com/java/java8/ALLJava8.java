package com.java.java8;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ALLJava8 {

	public static void main(String[] args) {
		
		//1.	Print all employees with salary > 50,000
		List<Employee> employees = Arrays.asList(new Employee(1, "John", 60000, "IT"),
				new Employee(2, "Sara", 45000, "HR"), new Employee(3, "Mike", 80000, "Finance"),
				new Employee(4, "David", 30000, "IT"), new Employee(5, "Sujane", 35000, "IT"));
		
		//entire employees is null
		List<Employee> employees_1 = null;
		
		//handling one employee null
		List<Employee> employees_2 = Arrays.asList(
			    new Employee(1, "John", 60000, "IT"),
			    new Employee(2, "Sara", 45000, "HR"),
			    null,   //  one employee is null
			    new Employee(3, "Mike", 80000, "Finance"),
			    new Employee(4, "David", 30000, "IT"),
			    new Employee(5, "Sujane", 35000, "IT")
			);	
		
		List<EmployeeDouble> employees_3 = Arrays.asList(
		        new EmployeeDouble(1, "John", 60000.0, "HR"),
		        new EmployeeDouble(2, "Janny", 30000.0, "IT"),
		        new EmployeeDouble(3, "Jacob", null, "Finance")   // null allowed
		);
		
		List<EmployeeDouble> employees_4 = Arrays.asList(
		        new EmployeeDouble(1, "John", 60000.0, "HR"),
		        null,
		        new EmployeeDouble(3, "Jacob", null, "Finance")   // null allowed
		);
		
		
		List<EmployeeBigDecimal> employees_5 = Arrays.asList(
		        new EmployeeBigDecimal(1, "John", new BigDecimal("60000"), "HR"),
		        new EmployeeBigDecimal(2, "Janny", new BigDecimal("30000"), "IT"),
		        new EmployeeBigDecimal(3, "Jacob", null, "Finance")  // null allowed
		);
		
		List<EmployeeBigDecimal> employees_6 = Arrays.asList(
		        new EmployeeBigDecimal(1, "John", new BigDecimal("60000"), "HR"),
		        null,
		        new EmployeeBigDecimal(3, "Jacob", null, "Finance")  // null allowed
		);
		
		
		/*
		 * map is used to transform the value inside the Optional.
Here, you are taking the list (list) and printing employees with salary > 50000.
Then you return the list itself.
Important:

map only runs if the Optional is not empty.
If employees_1 is null, this block does nothing.
		 */
		/*
		 * orElseGet is used to provide a default value if the Optional is empty.
If employees_1 was null, the Optional is empty, so this code runs.
It prints "employees_1 is null" and returns an empty list (so the program can keep running safely).
		 */
		Optional.ofNullable(employees_1)
        .map(list -> {
            list.stream()
                .filter(e -> e.getSalary() > 50000)
                .forEach(System.out::println);
            return list;
        })
        .orElseGet(() -> {
            System.out.println("employees_1 is null");
            return Collections.emptyList();
        });
		
		
	}

}
