package car;

public abstract class Car {
	
	public String name;
	public String wheel;
	public String body;
	public String engine;
	public String size;
	
	public abstract void assembling(); 
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
}
