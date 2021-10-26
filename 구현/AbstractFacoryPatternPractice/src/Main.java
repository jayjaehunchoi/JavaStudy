import store.HyundaiStore;
import store.KiaStore;

public class Main {
	public static void main(String[] args) {
		HyundaiStore h = new HyundaiStore();
		h.orderCar("세단");
		h.orderCar("대형");
		h.orderCar("소형");
		
		KiaStore k = new KiaStore();
		k.orderCar("세단");
		k.orderCar("대형");
		k.orderCar("소형");
	}
}
