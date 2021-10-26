package main.java.baseball;

import java.util.List;
import java.util.Scanner;
import static main.java.utils.Constant.*;

public class BaseBallGame {
	
	public void run(Scanner scanner) {
		Player player = new Player();
		Computer computer = new Computer(); 
		int restartParam = 0;
		
		while(restartParam != END) {

			List<Integer> computerList = computer.computerTurn();
			System.out.print(NUMBER_INPUT_REQUEST);
			List<Integer>  playerList = player.playerTurn(scanner);
			
			System.out.println(checkAnswer(playerList, computerList));
			
			if(checkFinish(Player.strike, Player.ball)) {
				if(restartOFinish(scanner,Player.strike, Player.ball)==RESTART) {
					resetForRestart();
				}else {
					restartParam = END;
				}
			}
		}
	}

	public String checkAnswer(List<Integer> playerList, List<Integer> computerList) {
		boolean strikeCheck = false;
		boolean ballCheck = false;
		for(int i = 0 ; i < INPUT_LENGTH; i++ ) {
			if(playerList.get(i) == computerList.get(i)) {
				Player.strike++;
				strikeCheck = true;
			}else if(computerList.contains(playerList.get(i))){
				Player.ball++;
				ballCheck = true;
			}
		}
		blockOverFlow();
		if(strikeCheck && ballCheck) {
			return Player.ball+BALL_HAPPEND+" "+Player.strike+STRIKE_HAPPEND;
		}else if(strikeCheck) {
			return Player.strike+STRIKE_HAPPEND;
		}else if(ballCheck){
			return Player.ball+BALL_HAPPEND;
		}
		return NOTHING;
	}
	
	public boolean checkFinish(int strike, int ball) {
		if(strike == STRIKE_MAX) {
			return true;
		}else if(ball == BALL_MAX) {
			return true;
		}
		return false;
	}
	
	public int restartOFinish(Scanner scanner, int strike, int ball) {
		if(strike == STRIKE_MAX) {
			System.out.println(THREE_STRIKE_HAPPEND);
		}else if(ball == BALL_MAX) {
			System.out.println(FOUR_BALL_HAPPEND);
		}
		System.out.println(ASK_FOR_START_AGAIN);
		return scanner.nextInt();
	}
	
	public void resetForRestart() {
		Player.strike = 0;
		Player.ball = 0;
	}
	
	public void blockOverFlow() {
		if(Player.strike >= 3) {
			Player.strike = 3;
		}
		if(Player.ball >= 4) {
			Player.ball = 4;
		}
	}
	
}
