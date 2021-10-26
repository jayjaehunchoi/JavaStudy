package utils;

import exception.TryTimesNotNumberException;
import exception.TryTimesOverflowException;

public class TryTimesValidator {
	
	public static boolean isTryTimeNumber(String times) {
		for(int i  = 0 ; i < times.length(); i++) {
			if(!Character.isDigit(times.charAt(i))) {
				try {
					throw new TryTimesNotNumberException("[ERROR] �õ�ȸ���� ���ڿ��� �մϴ�.");
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
				throw new TryTimesOverflowException("[ERROR] �õ�ȸ���� 0 �̻��̿��� �մϴ�.");
			}catch (Exception e) {
				System.out.println(e.getMessage());
				return false;
			}
		}
		return true;
	}
	
}
