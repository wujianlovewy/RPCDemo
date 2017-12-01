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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + age;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		long temp;
		temp = Double.doubleToLongBits(salary);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		if (age != other.age)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (Double.doubleToLongBits(salary) != Double
				.doubleToLongBits(other.salary))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Person [age=" + age + ", name=" + name + ", salary=" + salary
				+ "]";
	}
	
}
