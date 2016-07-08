package nl.gridshore.elastic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.gridshore.Employee;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.StringEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.sniff.HostsSniffer;
import org.elasticsearch.client.sniff.Sniffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jettrocoenradie on 07/07/2016.
 */
@Service
public class ConnectionService {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionService.class);

    private RestClient client;
    private Sniffer sniffer;

    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @Autowired
    private LoggingFailureListener loggingFailureListener;

    public ClusterHealth checkClusterHealth() {
        try {
            Response response = client.performRequest(
                    "GET",
                    "/_cluster/health",
                    new Hashtable<>(),
                    null);
            HttpEntity entity = response.getEntity();

            ClusterHealth clusterHealth = jacksonObjectMapper.readValue(entity.getContent(), ClusterHealth.class);

            response.close();

            return clusterHealth;

        } catch (IOException e) {
            logger.warn("Problem while executing request.", e);
            throw new QueryExecutionException("Error when executing a query");
        }
    }

    public void createEmployee(Employee employee) {
        try {
            HttpEntity requestBody = new StringEntity(jacksonObjectMapper.writeValueAsString(employee));
            Response response = client.performRequest(
                    "POST",
                    "/luminis/ams",
                    new Hashtable<>(),
                    requestBody);

            response.close();

        } catch (IOException e) {
            logger.warn("Problem while executing request.", e);
            throw new QueryExecutionException("Error when executing a query");
        }

    }

    public List<Employee> findEmployees(String employee) {
        try {
            String query = "{\"query\":{\"match\":{\"employee\":\"" + employee + "\"}}}";
            Response response = client.performRequest(
                    "GET",
                    "/luminis/_search",
                    new Hashtable<>(),
                    new StringEntity(query));

            HttpEntity entity = response.getEntity();

            TypeReference ref = new TypeReference<ResponseHits<Employee>>() {
            };
            ResponseHits<Employee> responseHits = jacksonObjectMapper.readValue(entity.getContent(), ref);

            response.close();


            return responseHits.getHits().getHits().stream()
                    .map(Hit::getSource)
                    .collect(Collectors.toList());

        } catch (IOException e) {
            logger.warn("Problem while executing request.", e);
            throw new QueryExecutionException("Error when executing a query");
        }
    }

    @PostConstruct
    public void afterCreation() {
        this.client = RestClient
                .builder(new HttpHost("localhost", 9200))
                .setFailureListener(loggingFailureListener)
                .build();

        this.sniffer = Sniffer.builder(this.client,
                HostsSniffer.builder(this.client).setScheme(HostsSniffer.Scheme.HTTP)
                        .build()
        ).build();
    }
}
