import store.HyundaiStore;
import store.KiaStore;

public class Main {
	public static void main(String[] args) {
		HyundaiStore h = new HyundaiStore();
		h.orderCar("����");
		h.orderCar("����");
		h.orderCar("����");
		
		KiaStore k = new KiaStore();
		k.orderCar("����");
		k.orderCar("����");
		k.orderCar("����");
	}
}
