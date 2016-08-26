package nl.gridshore;

import nl.gridshore.elastic.cluster.ClusterService;
import nl.gridshore.elastic.cluster.response.ClusterHealth;
import nl.gridshore.elastic.index.IndexService;
import nl.gridshore.employees.Employee;
import nl.gridshore.employees.EmployeeService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

@SpringBootApplication
public class ElasticClientDemoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ElasticClientDemoApplication.class, args);

        EmployeeService employeeService = context.getBean(EmployeeService.class);
        ClusterService clusterService = context.getBean(ClusterService.class);
        IndexService indexService = context.getBean(IndexService.class);

        ClusterHealth clusterHealth = clusterService.checkClusterHealth();

        System.out.println(clusterHealth);

        if (!indexService.indexExist(EmployeeService.INDEX)) {
            createEmployees(employeeService);
        }

        System.out.println("Should only print one result");
        queryForEmployees(employeeService, "Jettro Coenradie");

        System.out.println("Should do a match_all query");
        queryForEmployees(employeeService, "");

        System.out.println("Should do a multi_match on name and email query");
        List<Employee> foundEmployees = employeeService.queryForEmployeesByNameAndEmail("gridshore.nl");
        foundEmployees.forEach(System.out::println);

    }

    private static void queryForEmployees(EmployeeService employeeService, String name) {
        List<Employee> foundEmployees = employeeService.queryForEmployees(name);
        foundEmployees.forEach(System.out::println);
    }

    private static void createEmployees(EmployeeService employeeService) {
        Employee jettro = new Employee();
        jettro.setName("Jettro Coenradie");
        jettro.setEmail("jettro@gridshore.nl");
        jettro.setPhoneNumber("+31612345678");
        jettro.setSpecialties(new String[]{"java", "elasticsearch", "angularjs"});

        employeeService.storeEmployee(jettro);

        Employee byron = new Employee();
        byron.setName("Byron Voorbach");
        byron.setEmail("byron@gridshore.nl");
        byron.setPhoneNumber("+31612345678");
        byron.setSpecialties(new String[]{"java", "elasticsearch", "security"});

        employeeService.storeEmployee(byron);

        Employee ralph = new Employee();
        ralph.setName("Ralph Broers");
        ralph.setEmail("ralph@gridshore.nl");
        ralph.setPhoneNumber("+31612345678");
        ralph.setSpecialties(new String[]{"java", "elasticsearch", "rx"});

        employeeService.storeEmployee(ralph);

        Employee roberto = new Employee();
        roberto.setName("Roberto van der Linden");
        roberto.setEmail("roberto@gridshore.nl");
        roberto.setPhoneNumber("+31612345678");
        roberto.setSpecialties(new String[]{"java", "elasticsearch", "ionicframework"});

        employeeService.storeEmployee(roberto);
    }
}
