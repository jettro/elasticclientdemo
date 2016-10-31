package nl.gridshore;

import eu.luminis.elastic.RestClientConfig;
import eu.luminis.elastic.index.IndexService;
import nl.gridshore.employees.Employee;
import nl.gridshore.employees.EmployeeService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(RestClientConfig.class)
public class ElasticClientDemoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ElasticClientDemoApplication.class, args);

        EmployeeService employeeService = context.getBean(EmployeeService.class);
        IndexService indexService = context.getBean(IndexService.class);

        if (!indexService.indexExist(EmployeeService.INDEX)) {
            createEmployees(employeeService);
        }
    }

    private static void createEmployees(EmployeeService employeeService) {
        employeeService.createIndex();

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
