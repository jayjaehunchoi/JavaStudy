package polymorphism.woowacontract;

public class Main {

    public static void main(String[] args) {
        User huni = new User("huni", "12312412sd", "01023456789");

        // .. 인증관련 검증.....


        AuthWay authWay = AuthWay.PASS; // 사용자가 클릭한 버튼

        ContractService contractService = new ContractService(authWay.getNotifier());
        contractService.sendAuthenticateNumber(huni);
        contractService.authenticate(huni, "12345");

    }
}
