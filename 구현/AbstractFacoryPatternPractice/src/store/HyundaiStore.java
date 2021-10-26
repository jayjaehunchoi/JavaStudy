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
		if(name.equals("����")) {
			car = new HyundaiLightCar(ingredientFactory);
			car.setName("ĳ����");
		}else if(name.equals("����")) {
			car = new HyundaiSedan(ingredientFactory);
			car.setName("�׷���");
		}else if(name.equals("����")) {
			car = new HyundaiSUVCar(ingredientFactory);
			car.setName("��Ÿ��");
		}
		
		return car;
		
		
	}

}
