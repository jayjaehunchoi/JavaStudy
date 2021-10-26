package ingredientFactory;

import ingredients.Body;
import ingredients.Engine;
import ingredients.HyundaiBody;
import ingredients.HyundaiEngine;
import ingredients.HyundaiWheel;
import ingredients.Wheel;

public class HyundaiFactory implements IngredientFactory {

	@Override
	public Body body() {
		return new HyundaiBody();
	}

	@Override
	public Engine engine() {
		return new HyundaiEngine();
	}

	@Override
	public Wheel wheel() {
		return new HyundaiWheel();
	}

}
