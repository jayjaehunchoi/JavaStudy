package car.impl;

import car.Sedan;
import ingredientFactory.IngredientFactory;

public class KiaSedan extends Sedan{
	
	private IngredientFactory ingredientFactory;
	
	public KiaSedan (IngredientFactory ingredientFactory) {
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
