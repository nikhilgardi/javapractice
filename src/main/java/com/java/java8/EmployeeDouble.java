package com.java.java8;

public class EmployeeDouble {
	
	private Integer id;
    private String name;
    private Double salary;   // Wrapper class
    private String department;
    
    
    public EmployeeDouble(Integer id,String name,Double salary,String department)
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


	public Double getSalary() {
		return salary;
	}


	public void setSalary(Double salary) {
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
    	return "Employee[id="+id+"name="+name+"salary="+salary+"department="+department+"]";
    }
    

}
