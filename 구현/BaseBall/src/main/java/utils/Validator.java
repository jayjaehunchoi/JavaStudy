package main.java.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static main.java.utils.Constant.*;

public class Validator {
	
	public static void isLengthNotThree(List<Integer> list) {
		if(list.size() != INPUT_LENGTH) {
			throw new IllegalArgumentException("3�ڸ� ���ڸ� �Է����ּ���.");
		}
		
	}
	
	public static void isZeroExist(List<Integer> list) {
		if(list.contains(0)) {
			throw new IllegalArgumentException("0�� �����ϰ� �Է����ּ���.");
		}
	}
	
	public static void isDuplicatedNum(List<Integer> list) {
		Set<Integer> set = new HashSet<>();
		for(int i = 0 ; i < list.size(); i++) {
			set.add(list.get(i));
		}
		if(set.size() != INPUT_LENGTH) {
			throw new IllegalArgumentException("�ݺ��� ���ڰ� �ֽ��ϴ�.");
		}
	}

}
