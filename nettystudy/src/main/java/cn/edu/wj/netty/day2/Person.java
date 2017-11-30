package cn.edu.wj.netty.day2;

import java.io.Serializable;

public class Person implements Serializable{

	private static final long serialVersionUID = 7472196710024577263L;
	
	private int age;
	private String name;
	private double salary;
	
	public Person() {
	}
	
	public Person(int age, String name, double salary) {
		this.age = age;
		this.name = name;
		this.salary = salary;
	}



	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getSalary() {
		return salary;
	}
	public void setSalary(double salary) {
		this.salary = salary;
	}

	@Override
	public String toString() {
		return "Person [age=" + age + ", name=" + name + ", salary=" + salary
				+ "]";
	}
	
}
