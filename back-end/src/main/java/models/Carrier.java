package models;

public class Carrier {
	private String code;
	private String name;
	
	public Carrier(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getName() {
		return name;
	}
}
