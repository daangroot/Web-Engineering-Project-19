package models;

public class Airport {
	private String code;
	private String name;
	
	public Airport(String code, String name) {
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
