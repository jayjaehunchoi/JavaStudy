package store;

import car.Car;
import car.impl.KiaLightCar;
import car.impl.KiaSUVCar;
import car.impl.KiaSedan;
import ingredientFactory.IngredientFactory;
import ingredientFactory.KiaFactory;

public class KiaStore extends CarStore {

	@Override
	protected Car makeOrder(String name) {
		Car car = null;
		IngredientFactory ingredientFactory = new KiaFactory();
		if(name.equals("����")) {
			car = new KiaLightCar(ingredientFactory);
			car.setName("���");
		}else if(name.equals("����")) {
			car = new KiaSedan(ingredientFactory);
			car.setName("K8");
		}else if(name.equals("����")) {
			car = new KiaSUVCar(ingredientFactory);
			car.setName("���Ϻ�");
		}
		
		return car;
		
	}

}
