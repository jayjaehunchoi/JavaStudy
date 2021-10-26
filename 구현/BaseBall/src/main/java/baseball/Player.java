package main.java.baseball;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static main.java.utils.Constant.NUMBER_INPUT_REQUEST;
import static main.java.utils.Validator.*;

public class Player {
	public static int strike = 0;
	public static int ball = 0;
	
	public List<Integer> InputNewNumber(String num){
		List<Integer> playerInputList = new ArrayList<Integer>();
		for(int i = 0 ; i < num.length(); i++) {
			playerInputList.add(Integer.parseInt(num.substring(i,i+1)));
		}
		isLengthNotThree(playerInputList);
		isDuplicatedNum(playerInputList);
		isZeroExist(playerInputList);
		
		return playerInputList;
	}
	
	public List<Integer> playerTurn(Scanner scanner) {
        int a = 0;
        List<Integer> playerList = new ArrayList<>();
        while(a != 1) {
	        try {
	        	playerList = this.InputNewNumber(scanner.next());
	        	a = 1;
	        }catch (IllegalArgumentException e) {
				System.out.println(e.getMessage());
				System.out.print(NUMBER_INPUT_REQUEST);
			}
        }
        return playerList;
	}
}
