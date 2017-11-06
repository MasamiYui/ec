package tk.mybatis.springboot.model;

public class IdCard {

	private Long id;
	
	private String name;
	
	private String sex;
	
	private String nation;
	
	private String date;
	
	private String address;
	
	private String idcard_no;
	
	private String txhash;
	
	private String time;
	
	private String url;
	
	private int state;

	
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

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

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getIdcard_no() {
		return idcard_no;
	}

	public void setIdcard_no(String idcard_no) {
		this.idcard_no = idcard_no;
	}

	public String getTxhash() {
		return txhash;
	}

	public void setTxhash(String txhash) {
		this.txhash = txhash;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public IdCard(Long id, String name, String sex, String nation, String date,
			String address, String idcard_no, String txhash, String time, String url,
			int state) {
		super();
		this.id = id;
		this.name = name;
		this.sex = sex;
		this.nation = nation;
		this.date = date;
		this.address = address;
		this.idcard_no = idcard_no;
		this.txhash = txhash;
		this.time = time;
		this.url = url;
		this.state = state;
	}
	
	public IdCard(String name, String sex, String nation, String date,
			String address, String idcard_no, String txhash, String time,String url,
			int state) {
		this.name = name;
		this.sex = sex;
		this.nation = nation;
		this.date = date;
		this.address = address;
		this.idcard_no = idcard_no;
		this.txhash = txhash;
		this.time = time;
		this.url = url;
		this.state = state;
	}
	
	
	public IdCard(){
		
	}
	

}
