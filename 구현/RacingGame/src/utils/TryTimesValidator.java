package utils;

import exception.TryTimesNotNumberException;
import exception.TryTimesOverflowException;

public class TryTimesValidator {
	
	public static boolean isTryTimeNumber(String times) {
		for(int i  = 0 ; i < times.length(); i++) {
			if(!Character.isDigit(times.charAt(i))) {
				try {
					throw new TryTimesNotNumberException("[ERROR] 시도회수는 숫자여야 합니다.");
				}catch (Exception e) {
					System.out.println(e.getMessage());
					return false;
				}
			}
		}
		return true;
	}
	
	public static boolean isTryTimeNotZero(int times) {
		
		if(times == 0) {
			try {
				throw new TryTimesOverflowException("[ERROR] 시도회수는 0 이상이여야 합니다.");
			}catch (Exception e) {
				System.out.println(e.getMessage());
				return false;
			}
		}
		return true;
	}
	
}
