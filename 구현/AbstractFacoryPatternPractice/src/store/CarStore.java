package store;

import car.Car;

public abstract class CarStore {
	
	public Car orderCar(String name) {
		Car car = makeOrder(name);
		car.assembling();
		return car;
	}
	
	protected abstract Car makeOrder(String name);
	
}
