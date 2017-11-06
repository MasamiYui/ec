package tk.mybatis.springboot.model;

public class User {
	
	Long id;
	
	String name;
	
	String phone;
	
	String idcard_no;
	
	String passwd;
	
	int state;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getIdcard_no() {
		return idcard_no;
	}

	public void setIdcard_no(String idcard_no) {
		this.idcard_no = idcard_no;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public User(Long id, String name, String phone, String idcard_no, String passwd, int state) {
		super();
		this.id = id;
		this.name = name;
		this.phone = phone;
		this.idcard_no = idcard_no;
		this.passwd = passwd;
		this.state = state;
	}
	
	public User(String name, String phone, String idcard_no, String passwd, int state) {
		super();
		this.name = name;
		this.phone = phone;
		this.idcard_no = idcard_no;
		this.passwd = passwd;
		this.state = state;
	}
	
	public User() {
		
	}
	
	
}
