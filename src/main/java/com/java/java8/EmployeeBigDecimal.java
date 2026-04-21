package com.java.java8;

import java.math.BigDecimal;

public class EmployeeBigDecimal {
	
	private Integer id;
    private String name;
    private BigDecimal salary;
    private String department;
    
    public EmployeeBigDecimal(Integer id,String name,BigDecimal salary,String department)
    {
    	this.id=id;
    	this.name=name;
    	this.salary=salary;
    	this.department=department;
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getSalary() {
		return salary;
	}

	public void setSalary(BigDecimal salary) {
		this.salary = salary;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}
    
    @Override
    public String toString()
    {
    	return "Employee[id-"+id+"name-"+name+"salary-"+salary+"department-"+department+"]";
    }

}
