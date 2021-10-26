package car.impl;

import car.LightCar;
import ingredientFactory.IngredientFactory;

public class HyundaiLightCar extends LightCar{
	private IngredientFactory ingredientFactory;
	
	public HyundaiLightCar(IngredientFactory ingredientFactory) {
		this.ingredientFactory = ingredientFactory;
	}
	
	@Override
	public void assembling() {
		body = ingredientFactory.body().companyName();
		wheel = ingredientFactory.wheel().companyName();
		engine = ingredientFactory.engine().companyName();
		setSize();
		System.out.println("name = " + name + " body = " +  body + " wheel = " + wheel + " engine = " + engine + " size = " + size );
	}

}
