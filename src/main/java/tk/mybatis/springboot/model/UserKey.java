package tk.mybatis.springboot.model;

public class UserKey {
	Long id;
	
	String idcard_no;
	
	String public_key;
	
	String private_key;
	
	String passwd;
	
	

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIdcard_no() {
		return idcard_no;
	}

	public void setIdcard_no(String idcard_no) {
		this.idcard_no = idcard_no;
	}

	public String getPublic_key() {
		return public_key;
	}

	public void setPublic_key(String public_key) {
		this.public_key = public_key;
	}

	public String getPrivate_key() {
		return private_key;
	}

	public void setPrivate_key(String private_key) {
		this.private_key = private_key;
	}

	public UserKey(Long id, String idcard_no, String public_key,
			String private_key, String passwd) {
		super();
		this.id = id;
		this.idcard_no = idcard_no;
		this.public_key = public_key;
		this.private_key = private_key;
		this.passwd = passwd;
	}

	public UserKey(String idcard_no, String public_key,
			String private_key, String passwd) {
		this.idcard_no = idcard_no;
		this.public_key = public_key;
		this.private_key = private_key;
		this.passwd = passwd;
	}
	
	
	public UserKey(){
		
	}
	
}
