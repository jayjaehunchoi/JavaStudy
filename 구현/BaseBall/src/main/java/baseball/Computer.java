package main.java.baseball;

import java.util.ArrayList;
import java.util.List;

import main.java.utils.RandomUtils;

import static main.java.utils.Constant.*;
import static main.java.utils.Validator.*;

public class Computer {
	
	public List<Integer> giveNewNumber(){
		int i = 0;
		List<Integer> computerGivenList = new ArrayList<Integer>();
		while(i++ < INPUT_LENGTH) {
			computerGivenList.add(RandomUtils.nextInt(MIN_INPUT, MAX_INPUT));
		}
		isDuplicatedNum(computerGivenList);	
		return computerGivenList;
	}
	
	
	public List<Integer> computerTurn() {
		int a = 0;
		List<Integer> computerList = new ArrayList<>();
		while(a != 1) {
			try {
				computerList = this.giveNewNumber();
				a=1;
			}catch(IllegalArgumentException e) {
			}
		}
		return computerList;
	}
	
}
