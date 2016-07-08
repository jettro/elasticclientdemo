package nl.gridshore;

/**
 * Created by jettrocoenradie on 07/07/2016.
 */
public class Employee {
    private String employee;

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employee='" + employee + '\'' +
                '}';
    }
}
