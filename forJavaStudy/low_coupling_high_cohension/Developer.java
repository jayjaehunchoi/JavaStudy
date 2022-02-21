package low_coupling_high_cohension;

public class Developer implements Employee{
    @Override
    public void doWork() {
        System.out.println("개발자가 일을 합니다.");
    }
}

class Designer implements Employee {
    @Override
    public void doWork() {
        System.out.println("디자이너가 일을 합니다.");
    }
}

class Company {
    private Employee employee;

    public Company(Employee employee) {
        this.employee = employee;
    }

    public void work() {
        employee.doWork();
    }
}