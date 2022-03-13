package polymorphism.woowacontract;

public class User {

    private final String name;
    private final String contractNumber;
    private final String phoneNumber;

    public User(String name, String contractNumber, String phoneNumber) {
        this.name = name;
        this.contractNumber = contractNumber;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
