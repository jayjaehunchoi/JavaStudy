package ingredientFactory;

import ingredients.Body;
import ingredients.Engine;
import ingredients.Wheel;

public interface IngredientFactory {
	
	
	public Body body();
	public Engine engine();
	public Wheel wheel();
	
}
