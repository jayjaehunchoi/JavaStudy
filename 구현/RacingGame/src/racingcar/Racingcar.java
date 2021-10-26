package racingcar;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import service.NameService;
import service.RacingService;
import service.TryTimesService;

import static utils.Constant.*;


public class Racingcar {
	
	public static final List<Car> cars = new ArrayList<Car>();
	NameService nameService = new NameService();
	TryTimesService tryTimesService = new TryTimesService();
	RacingService racingService = new RacingService();
	Scanner scanner;
	
	
	public Racingcar(Scanner scanner) {
		this.scanner = scanner;
	}
	
	public void run() {
		nameService.nameInput(scanner);
		int times = tryTimesService.timesInput(scanner);
		racingService.recursion(times);
		
		
	}
	
	
}
