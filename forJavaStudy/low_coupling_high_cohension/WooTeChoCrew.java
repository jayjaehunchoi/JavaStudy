package low_coupling_high_cohension;

public class WooTeChoCrew {

    public void startMorning() {
        wakeUp();
        turnOnComputer();
        startDaily();
    }

    private void wakeUp() {
        System.out.println("09시 55분 기상");
    }

    private void turnOnComputer() {
        System.out.println("컴퓨터를 켠다");
    }

    private void startDaily() {
        System.out.println("데일리 미팅을 한다");
    }
}

class WooTeCho {
    public void workAtMorning() {
        WooTeChoCrew wooTeChoCrew = new WooTeChoCrew();
        wooTeChoCrew.startMorning();
    }
}
