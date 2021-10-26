package service;

import static utils.TryTimesValidator.*;
import static utils.Constant.*;
import java.util.Scanner;

public class TryTimesService {
	
	public int timesInput(Scanner scanner) {
		while(true) {
			System.out.println(ASK_FOR_TRY_TIMES_INPUT);
			String times = scanner.next();
			if(!isTryTimeNumber(times)) {
				continue;
			}
			int time = Integer.parseInt(times);
			if(!isTryTimeNotZero(time)) {
				continue;
			}
			return time;
		}
	}
}
