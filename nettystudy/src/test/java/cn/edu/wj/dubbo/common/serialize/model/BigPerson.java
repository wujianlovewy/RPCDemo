package cn.edu.wj.dubbo.common.serialize.model;

public class BigPerson {

	String personId;

    String loginName;
    
    String email;

    String penName;
    
    PersonInfo personInfo;
    
    PersonStatus personStatus;

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPenName() {
		return penName;
	}

	public void setPenName(String penName) {
		this.penName = penName;
	}

	public PersonInfo getPersonInfo() {
		return personInfo;
	}

	public void setPersonInfo(PersonInfo personInfo) {
		this.personInfo = personInfo;
	}

	public PersonStatus getPersonStatus() {
		return personStatus;
	}

	public void setStatus(PersonStatus personStatus) {
		this.personStatus = personStatus;
	}
    
}
