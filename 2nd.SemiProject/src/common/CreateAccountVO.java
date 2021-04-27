package common;

public class CreateAccountVO {
	//회원가입과 관련된 VO
	private int member_code =  0;        
	private String id      =  "";
	private String pw      =  "";
	private String deptno  =  "";
	private String name    =  "";
	private String email   =  "";
	private String gender  =  "";
	/////////////////////////////
	
	public CreateAccountVO() {}
	
	public CreateAccountVO(String id, String deptno, String name, String email, String gender) {
		this.id=id;
		this.deptno=deptno;
		this.name=name;
		this.email=email;
		this.gender=gender;
	}

	public int getMember_num() {
		return member_code;
	}
	public void setMember_num(int member_code) {
		this.member_code = member_code;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPw() {
		return pw;
	}
	public void setPw(String pw) {
		this.pw = pw;
	}
	public String getDeptno() {
		return deptno;
	}
	public void setDeptno(String deptno) {
		this.deptno = deptno;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
}