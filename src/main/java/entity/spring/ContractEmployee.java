package entity.spring;

public class ContractEmployee extends Employee {

    private Float payPerHour;

    public Float getPayPerHour() {
        return payPerHour;
    }

    public void setPayPerHour(Float payPerHour) {
        this.payPerHour = payPerHour;
    }
}
