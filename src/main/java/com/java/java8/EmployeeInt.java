package com.java.java8;

public class EmployeeInt {
	private int id;
	private String name;
	private int salary;
	private String department;
	
	public EmployeeInt(int id,String name,int salary,String department)
	{
		this.id=id;
		this.name=name;
		this.salary=salary;
		this.department=department;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	@Override
	public String toString() {
		return "Employee[id-"+id+"name-"+name+"salary-"+salary+"department-"+department+"]";
	}
	
	

}
