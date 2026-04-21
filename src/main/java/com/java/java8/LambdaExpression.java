package com.java.java8;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class LambdaExpression {

	public static void main(String[] args) {
		
		//double program
		//1.	Print all employees with salary > 50,000
				List<Employee> employees = Arrays.asList(new Employee(1, "John", 60000, "IT"),
						new Employee(2, "Sara", 45000, "HR"), new Employee(3, "Mike", 80000, "Finance"),
						new Employee(4, "David", 30000, "IT"), new Employee(5, "Sujane", 35000, "IT"));
				
		//Typical Way
		for(Employee e:employees)
		{
			if(e.getSalary()>50000)
			{
				System.out.println(e);
			}
		}
		
		//Lambda Way
		employees.stream()
				.filter(e->e.getSalary()>50000)
				.forEach(System.out::println);
		
		//entire employees is null
		List<Employee> employees_1 = null;
		
		
		
		Optional.ofNullable(employees_1)
				.orElse(Collections.emptyList())
				.stream()
				.filter(e->e.getSalary()>50000)
				.forEach(System.out::println);
		
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
		
		//handling one employee null
		List<Employee> employees_2 = Arrays.asList(
			    new Employee(1, "John", 60000, "IT"),
			    new Employee(2, "Sara", 45000, "HR"),
			    null,   //  one employee is null
			    new Employee(3, "Mike", 80000, "Finance"),
			    new Employee(4, "David", 30000, "IT"),
			    new Employee(5, "Sujane", 35000, "IT")
			);	
		
		Optional.ofNullable(employees_2)
		.orElse(Collections.emptyList())
		.stream()
		.filter(Objects::nonNull)
		.filter(e->e.getSalary()>50000)
		.forEach(System.out::println);
		
		List<EmployeeDouble> employees_3 = Arrays.asList(
		        new EmployeeDouble(1, "John", 60000.0, "HR"),
		        new EmployeeDouble(2, "Janny", 30000.0, "IT"),
		        new EmployeeDouble(3, "Jacob", null, "Finance")   // null allowed
		);
		
		employees_3.stream()
					.filter(e->Optional.ofNullable(e.getSalary()).orElse(0.0)>50000)
					.forEach(System.out::println);
		
		//Double
		List<EmployeeDouble> employees_4 = Arrays.asList(
		        new EmployeeDouble(1, "John", 60000.0, "HR"),
		        null,
		        new EmployeeDouble(3, "Jacob", null, "Finance")   // null allowed
		);
		
		employees_4.stream()
					.filter(Objects::nonNull)
					.filter(e->Optional.ofNullable(e.getSalary()).orElse(0.0)>50000)
					.forEach(System.out::println);
		
		//Big Decimal
		List<EmployeeBigDecimal> employees_6 = Arrays.asList(
		        new EmployeeBigDecimal(1, "John", new BigDecimal("60000"), "HR"),
		        null,
		        new EmployeeBigDecimal(3, "Jacob", null, "Finance")  // null allowed
		);
		
		employees_6.stream()
					.filter(Objects::nonNull)
					.filter(e->Optional.ofNullable(e.getSalary())
							.orElse(BigDecimal.ZERO)
							.compareTo(BigDecimal.valueOf(5000))>0)
					.forEach(System.out::println);
				
		// ===============================
        // 🔥 MULTI STREAM OPERATIONS
        // ===============================

        System.out.println("\n=== Grouping By Department (Employee) ===");

        Map<String, List<Employee>> groupByDept =
                employees.stream()
                        .collect(Collectors.groupingBy(Employee::getDepartment));

        groupByDept.forEach((dept, empList) -> {
            System.out.println("Department: " + dept);
            empList.forEach(System.out::println);
        });


        System.out.println("\n=== Count Employees By Department ===");

        Map<String, Long> countByDept =
                employees.stream()
                        .collect(Collectors.groupingBy(
                                Employee::getDepartment,
                                Collectors.counting()
                        ));

        countByDept.forEach((dept, count) ->
                System.out.println(dept + " -> " + count)
        );


        System.out.println("\n=== Max Salary Employee ===");

        employees.stream()
                .max(Comparator.comparing(Employee::getSalary))
                .ifPresent(System.out::println);


        System.out.println("\n=== Sorted By Salary Descending ===");

        employees.stream()
                .sorted(Comparator.comparing(Employee::getSalary).reversed())
                .forEach(System.out::println);


        System.out.println("\n=== Average Salary ===");

        employees.stream()
                .mapToDouble(Employee::getSalary)
                .average()
                .ifPresent(avg -> System.out.println("Average Salary: " + avg));


        System.out.println("\n=== Names Of Employees Earning > 50000 ===");

        employees.stream()
                .filter(e -> e.getSalary() > 50000)
                .map(Employee::getName)
                .forEach(System.out::println);
        
        System.out.println("\n=== Group By Department Then Salary > 50000 ===");

        Map<String, Map<Boolean, List<Employee>>> multiLevel =
                employees.stream()
                        .collect(Collectors.groupingBy(
                                Employee::getDepartment,
                                Collectors.groupingBy(e -> e.getSalary() > 50000)
                        ));

        multiLevel.forEach((dept, salaryGroup) -> {
            System.out.println("Department: " + dept);
            salaryGroup.forEach((condition, list) -> {
                System.out.println("Salary > 50000 : " + condition);
                list.forEach(System.out::println);
            });
        });
        
        System.out.println("\n=== Partitioning (Salary > 50000) ===");

        Map<Boolean, List<Employee>> partitioned =
                employees.stream()
                        .collect(Collectors.partitioningBy(e -> e.getSalary() > 50000));

        partitioned.forEach((key, value) -> {
            System.out.println("Salary > 50000 : " + key);
            value.forEach(System.out::println);
        });
        
        System.out.println("\n=== Total Salary ===");

        double totalSalary = employees.stream()
                .mapToDouble(Employee::getSalary)
                .sum();

        System.out.println("Total Salary: " + totalSalary);
        
        System.out.println("\n=== Total Salary Using Reduce ===");

        double totalUsingReduce = employees.stream()
                .map(Employee::getSalary)
                .reduce(0.0, Double::sum);

        System.out.println("Total Salary (Reduce): " + totalUsingReduce);
        
        System.out.println("\n=== Parallel Stream Example ===");

        employees.parallelStream()
                .filter(e -> e.getSalary() > 50000)
                .forEach(e ->
                        System.out.println(Thread.currentThread().getName() + " : " + e)
                );
        
        System.out.println("\n=== FlatMap Example ===");

        List<List<Employee>> nestedList = Arrays.asList(employees, employees_2);

        nestedList.stream()
                .flatMap(List::stream)
                .filter(Objects::nonNull)
                .filter(e -> e.getSalary() > 50000)
                .forEach(System.out::println);
        
        System.out.println("\n=== Custom Collector (Count Employees) ===");

        Long customCount = employees.stream()
                .collect(Collectors.reducing(
                        0L,
                        e -> 1L,
                        Long::sum
                ));

        System.out.println("Custom Count: " + customCount);
	}

}
