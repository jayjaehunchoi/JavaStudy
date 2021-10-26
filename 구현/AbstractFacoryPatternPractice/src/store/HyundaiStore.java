package store;

import car.Car;
import car.impl.HyundaiLightCar;
import car.impl.HyundaiSUVCar;
import car.impl.HyundaiSedan;
import ingredientFactory.HyundaiFactory;
import ingredientFactory.IngredientFactory;

public class HyundaiStore extends CarStore {
	
	@Override
	protected Car makeOrder(String name) {
		Car car = null;
		IngredientFactory ingredientFactory = new HyundaiFactory();
		if(name.equals("소형")) {
			car = new HyundaiLightCar(ingredientFactory);
			car.setName("캐스퍼");
		}else if(name.equals("세단")) {
			car = new HyundaiSedan(ingredientFactory);
			car.setName("그랜져");
		}else if(name.equals("대형")) {
			car = new HyundaiSUVCar(ingredientFactory);
			car.setName("싼타페");
		}
		
		return car;
		
		
	}

}
