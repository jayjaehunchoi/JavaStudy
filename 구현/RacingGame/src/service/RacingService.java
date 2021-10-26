package service;

import static utils.RandomUtils.*;

import java.util.ArrayList;
import java.util.List;

import racingcar.Car;
import racingcar.Racingcar;

import static utils.Constant.*;

public class RacingService {
	
	public void recursion(int times) {
		System.out.println(RESULT);
		while(times-- > 0) {
			moveOrNot();
			for(int i = 0 ; i < Racingcar.cars.size(); i++) {
				Car car = Racingcar.cars.get(i);
				System.out.println(car.getName()+ " : " + showMove(car));
			}
			System.out.println();
		}
		findWinner();
		
	}
	
	private void moveOrNot(){
		for(int i = 0 ; i < Racingcar.cars.size(); i++) {
			Car car = Racingcar.cars.get(i);
			if(nextInt(STARTNUM, ENDNUM) >= 4) {
				car.goForward();
			}
		}
	}
	
	private String showMove(Car car) {
		String move = "";
		for(int i = 0 ; i < car.getPosition(); i++) {
			move += "-";
		}
		return move;
	}
	
	private void findWinner(){
		List<String> winners = new ArrayList<String>();
		int max = 0;
		for(int i = 0 ; i < Racingcar.cars.size(); i++) {
			Car car = Racingcar.cars.get(i);
			if(car.getPosition() > max) {
				max = car.getPosition();
				winners.clear();
				winners.add(car.getName());
			}else if(car.getPosition() == max) {
				winners.add(car.getName());
			}
		}
		System.out.print(FINAL_WINNER_IS);
		printWinner(winners);
		
	}
	
	private void printWinner(List<String> winners) {
		if(winners.size() == 1) {
			System.out.print(winners.get(0));
			return;
		}
		for(int i = 0 ; i < winners.size(); i++) {
			if(i == winners.size()-1) {
				System.out.print(winners.get(i));
				return;
			}
			System.out.print(winners.get(i)+ ", ");
		}
	}

}
