package car.impl;

import car.Suv;
import ingredientFactory.IngredientFactory;

public class KiaSUVCar extends Suv{
	private IngredientFactory ingredientFactory;
	
	public KiaSUVCar(IngredientFactory ingredientFactory) {
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
