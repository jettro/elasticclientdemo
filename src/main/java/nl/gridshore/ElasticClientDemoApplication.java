package nl.gridshore;

import nl.gridshore.elastic.ClusterHealth;
import nl.gridshore.elastic.ConnectionService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

@SpringBootApplication
public class ElasticClientDemoApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(ElasticClientDemoApplication.class, args);
        ConnectionService bean = context.getBean(ConnectionService.class);

        ClusterHealth clusterHealth = bean.checkClusterHealth();

        System.out.println(clusterHealth);

//        Employee emp = new Employee();
//        emp.setEmployee("Jettro");
//        bean.createEmployee(emp);

        List<Employee> jettro = bean.findEmployees("jettro");

        jettro.stream().forEach(System.out::println);
    }
}
