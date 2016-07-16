package nl.gridshore;

import nl.gridshore.elastic.*;
import nl.gridshore.employees.Employee;
import nl.gridshore.employees.EmployeeService;
import nl.gridshore.employees.EmployeeTypeReference;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class ElasticClientDemoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ElasticClientDemoApplication.class, args);
        ConnectionService connectionService = context.getBean(ConnectionService.class);
        EmployeeService employeeService = context.getBean(EmployeeService.class);

        ClusterHealth clusterHealth = connectionService.checkClusterHealth();

        System.out.println(clusterHealth);

//        createEmployee(employeeService);

        System.out.println("Should only print one result");
        queryForEmployees(employeeService, "Jettro Coenradie");

        System.out.println("Should do a match_all query");
        queryForEmployees(employeeService, "");
    }

    private static void queryForEmployees(EmployeeService employeeService, String name) {
        List<Employee> foundEmployees = employeeService.queryForEmployees(name);
        foundEmployees.forEach(System.out::println);
    }

    private static void createEmployee(EmployeeService employeeService) {
        Employee emp = new Employee();
        emp.setEmployee("Ian Coenradie");

        employeeService.createEmployee(emp);
    }
}
