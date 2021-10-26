package service;

import static utils.Constant.ASK_FOR_NAME_INPUT;
import static utils.RacingcarValidator.*;
import java.util.Scanner;

import racingcar.Car;
import racingcar.Racingcar;

public class NameService {
	
	public void nameInput(Scanner scanner) {
		while(true) {
			System.out.println(ASK_FOR_NAME_INPUT);
			String inputs = scanner.next();
			if(!isCommaExisted(inputs)) {
				continue;
			}
			String[] names = nameToArray(inputs);
			if(!isNameNotOverflow(names) || !isNameNotDuplicated(names) || !isNameExisted(names)) {
				continue;
			}
			arrayToCars(names);
			break;
		}
	}
	
	private String[] nameToArray(String inputs) {
		String[] names = inputs.split(",");
		return names;
	}
	
	private void arrayToCars(String[] names) {
		for(int i = 0 ; i < names.length; i++) {
			Racingcar.cars.add(new Car(names[i]));
		}
	}
}
