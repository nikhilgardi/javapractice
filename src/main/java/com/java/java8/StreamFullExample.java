package com.java.java8;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamFullExample {

    public static void main(String[] args) {

        // ============================
        // Sample Employees
        // ============================
        List<Employee> employees = Arrays.asList(
                new Employee(1, "John", 60000, "IT"),
                new Employee(2, "Sara", 45000, "HR"),
                new Employee(3, "Mike", 80000, "Finance"),
                new Employee(4, "David", 30000, "IT"),
                new Employee(5, "Sujane", 35000, "IT")
        );

        // ============================
        // 1️⃣ Basic Stream Operations
        // ============================
        System.out.println("=== Filter Salary > 50000 ===");
        employees.stream()
                .filter(e -> e.getSalary() > 50000)
                .forEach(System.out::println);

        System.out.println("\n=== Map to Names ===");
        employees.stream()
                .map(Employee::getName)
                .forEach(System.out::println);

        System.out.println("\n=== Distinct Example ===");
        Stream.of(1,2,2,3,3,4).distinct().forEach(System.out::println);

        System.out.println("\n=== Sorted By Salary ===");
        employees.stream()
                .sorted(Comparator.comparing(Employee::getSalary))
                .forEach(System.out::println);

        System.out.println("\n=== Skip & Limit ===");
        employees.stream()
                .skip(1)
                .limit(3)
                .forEach(System.out::println);

        System.out.println("\n=== Peek Example ===");
        employees.stream()
                .peek(e -> System.out.println("Before Filter: " + e))
                .filter(e -> e.getSalary() > 50000)
                .forEach(System.out::println);

        // ============================
        // 2️⃣ Terminal Operations
        // ============================
        System.out.println("\n=== Count Employees ===");
        long count = employees.stream().count();
        System.out.println("Count: " + count);

        System.out.println("\n=== Max Salary ===");
        employees.stream()
                .max(Comparator.comparing(Employee::getSalary))
                .ifPresent(System.out::println);

        System.out.println("\n=== Min Salary ===");
        employees.stream()
                .min(Comparator.comparing(Employee::getSalary))
                .ifPresent(System.out::println);

        System.out.println("\n=== Any Match Salary > 70000 ===");
        boolean any = employees.stream().anyMatch(e -> e.getSalary() > 70000);
        System.out.println(any);

        System.out.println("\n=== All Match Salary > 20000 ===");
        boolean all = employees.stream().allMatch(e -> e.getSalary() > 20000);
        System.out.println(all);

        System.out.println("\n=== None Match Salary < 10000 ===");
        boolean none = employees.stream().noneMatch(e -> e.getSalary() < 10000);
        System.out.println(none);

        System.out.println("\n=== Find First ===");
        employees.stream().findFirst().ifPresent(System.out::println);

        System.out.println("\n=== Find Any ===");
        employees.parallelStream().findAny().ifPresent(System.out::println);

        // ============================
        // 3️⃣ Numeric Streams
        // ============================
        System.out.println("\n=== Sum of Salaries ===");
        double sum = employees.stream().mapToDouble(Employee::getSalary).sum();
        System.out.println(sum);

        System.out.println("\n=== Average Salary ===");
        employees.stream()
                .mapToDouble(Employee::getSalary)
                .average()
                .ifPresent(System.out::println);

        System.out.println("\n=== Summary Statistics ===");
        DoubleSummaryStatistics stats = employees.stream()
                .mapToDouble(Employee::getSalary)
                .summaryStatistics();
        System.out.println(stats);

        // ============================
        // 4️⃣ Collectors
        // ============================
        System.out.println("\n=== Group By Department ===");
        Map<String, List<Employee>> groupByDept =
                employees.stream().collect(Collectors.groupingBy(Employee::getDepartment));
        groupByDept.forEach((dept, list) -> {
            System.out.println("Department: " + dept);
            list.forEach(System.out::println);
        });

        System.out.println("\n=== Partition By Salary > 50000 ===");
        Map<Boolean, List<Employee>> partition =
                employees.stream().collect(Collectors.partitioningBy(e -> e.getSalary() > 50000));
        partition.forEach((key, value) -> {
            System.out.println("Salary > 50000: " + key);
            value.forEach(System.out::println);
        });

        System.out.println("\n=== Count By Department ===");
        Map<String, Long> countByDept =
                employees.stream().collect(Collectors.groupingBy(
                        Employee::getDepartment, Collectors.counting()));
        countByDept.forEach((dept, c) -> System.out.println(dept + " -> " + c));

        System.out.println("\n=== Average Salary By Department ===");
        Map<String, Double> avgByDept =
        	    employees.stream()
        	             .collect(Collectors.groupingBy(
        	                 Employee::getDepartment,
        	                 Collectors.averagingDouble(Employee::getSalary) // <- works with Integer
        	             ));
        avgByDept.forEach((dept, avg) -> System.out.println(dept + " -> " + avg));

        System.out.println("\n=== Joining Employee Names ===");
        String names = employees.stream()
                .map(Employee::getName)
                .collect(Collectors.joining(", "));
        System.out.println(names);

        System.out.println("\n=== Mapping Names By Department ===");
        Map<String, List<String>> namesByDept =
                employees.stream().collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.mapping(Employee::getName, Collectors.toList())
                ));
        namesByDept.forEach((dept, list) -> System.out.println(dept + " -> " + list));

        System.out.println("\n=== Custom Collector (Count Employees) ===");
        Long customCount = employees.stream()
                .collect(Collectors.reducing(0L, e -> 1L, Long::sum));
        System.out.println("Custom Count: " + customCount);

        // ============================
        // 5️⃣ Reduce Example
        // ============================
        System.out.println("\n=== Reduce Total Salary ===");
        double totalSalary = employees.stream()
                .map(Employee::getSalary)
                .reduce(0.0, Double::sum);
        System.out.println("Total Salary: " + totalSalary);

        // ============================
        // 6️⃣ Parallel Stream Example
        // ============================
        System.out.println("\n=== Parallel Stream Example ===");
        employees.parallelStream()
                .filter(e -> e.getSalary() > 50000)
                .forEach(e -> System.out.println(Thread.currentThread().getName() + ": " + e));

        // ============================
        // 7️⃣ FlatMap Example
        // ============================
        System.out.println("\n=== FlatMap Example ===");
        List<List<Employee>> nested = Arrays.asList(employees, employees);
        nested.stream()
                .flatMap(List::stream)
                .forEach(System.out::println);

    }
}