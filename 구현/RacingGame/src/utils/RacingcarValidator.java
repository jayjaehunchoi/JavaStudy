package utils;

import java.util.HashSet;
import java.util.Set;

import exception.NameDuplicatedException;
import exception.NameNotExistedException;
import exception.NameOverflowException;
import exception.NoCommaException;

public class RacingcarValidator {
	
	public static boolean isCommaExisted(String input) {
		for(int i = 0 ; i < input.length(); i++) {
			if(input.charAt(i) == ',') {
				return true;
			}
		}
		
		try {
			throw new NoCommaException("[ERROR] ��ǥ�� ���еž� �մϴ�.");
		}catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	
	}
	
	public static boolean isNameNotOverflow(String[] names) {
		for(int i = 0 ; i < names.length; i++) {
			String name = names[i];
			if(name.length() > 5) {
				try {
					throw new NameOverflowException("[ERROR] �̸��� �ټ��� ���Ͽ��� �մϴ�.");
				}catch (Exception e) {
					System.out.println(e.getMessage());
					return false;
				}
			}
		}
		
		return true;
	}
	
	public static boolean isNameNotDuplicated(String[] names) {
		Set<String> nameSet = new HashSet<>();
		for(int i = 0 ; i < names.length; i++) {
			nameSet.add(names[i]);
		}
		if(names.length != nameSet.size()) {
			try {
				throw new NameDuplicatedException("[ERROR] �ߺ��� �̸��� �ֽ��ϴ�.");
			}catch (Exception e) {
				System.out.println(e.getMessage());
				return false;
			}	
		}
		return true;
	}
	
	public static boolean isNameExisted(String[] names) {
		for(int i = 0 ; i < names.length; i++) {
			String name = names[i];
			if(name.equals("")) {
				try {
					throw new NameNotExistedException("[ERROR] �̸��� �������� �ʽ��ϴ�.");
				}catch (Exception e) {
					System.out.println(e.getMessage());
					return false;
				}
			}
		}
		return true;
	}
	
}
