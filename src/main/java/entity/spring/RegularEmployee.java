package entity.spring;

public class RegularEmployee extends Employee {

    private Float salary;

    public Float getSalary() {
        return salary;
    }

    public void setSalary(Float salary) {
        this.salary = salary;
    }
}