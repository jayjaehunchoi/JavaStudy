package application.weather;

public class weatherVO {
	
	private String location;
	private int temperature;
	private String status;
	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public int getTemperature() {
		return temperature;
	}
	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public weatherVO() {
		
	}
	
	public weatherVO(String location, int temperature, String status) {
		super();
		this.location = location;
		this.temperature = temperature;
		this.status = status;
	}
	
	
	

}
