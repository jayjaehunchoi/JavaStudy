package ingredientFactory;

import ingredients.Body;
import ingredients.Engine;
import ingredients.KiaBody;
import ingredients.KiaEngine;
import ingredients.KiaWheel;
import ingredients.Wheel;

public class KiaFactory implements IngredientFactory {

	@Override
	public Body body() {
		return new KiaBody();
	}

	@Override
	public Engine engine() {
		return new KiaEngine();
	}

	@Override
	public Wheel wheel() {
		return new KiaWheel();
	}

}
