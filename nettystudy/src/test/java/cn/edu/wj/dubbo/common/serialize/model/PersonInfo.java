package cn.edu.wj.dubbo.common.serialize.model;

public class PersonInfo {

	String mobileNo;

    String name;

    boolean male;

    boolean female;

    String department;

    String jobTitle;

    String homepageUrl;
    
    double salary;
    
	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isMale() {
		return male;
	}

	public void setMale(boolean male) {
		this.male = male;
	}

	public boolean isFemale() {
		return female;
	}

	public void setFemale(boolean female) {
		this.female = female;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getHomepageUrl() {
		return homepageUrl;
	}

	public void setHomepageUrl(String homepageUrl) {
		this.homepageUrl = homepageUrl;
	}
    
	
}
