package cn.edu.wj.io.test;

import java.util.HashMap;
import java.util.Map;

public class Person {

	private String name;
	
	public Person(String name){
		this.name = name;
	}
	
	public static final Map<String, String> map = new HashMap<>();
	
	public static void main(String[] args) {
		Person p1 = new Person("p1");
		Person p2 = new Person("p2");
		
		System.out.println(p1.map == p2.map);
	}
	
}
