package nl.gridshore.elastic;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.gridshore.elastic.response.ResponseHandler;
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
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Hashtable;

/**
 * Created by jettrocoenradie on 07/07/2016.
 */
@Service
public class ConnectionService {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionService.class);

    private RestClient client;
    private Sniffer sniffer;

    private final ObjectMapper jacksonObjectMapper;

    private final LoggingFailureListener loggingFailureListener;

    @Autowired
    public ConnectionService(ObjectMapper jacksonObjectMapper, LoggingFailureListener loggingFailureListener) {
        this.jacksonObjectMapper = jacksonObjectMapper;
        this.loggingFailureListener = loggingFailureListener;
    }

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

    public void indexDocument(String index, String type, HttpEntity entity) {
        try {
            Response response = client.performRequest(
                    "POST",
                    index + "/" + type,
                    new Hashtable<>(),
                    entity);

            response.close();

        } catch (IOException e) {
            logger.warn("Problem while executing request.", e);
            throw new QueryExecutionException("Error when executing a query");
        }

    }

    public void executeQuery(String indexString, String query, ResponseHandler handler) {
        logger.debug("Query to be executed {}", query);

        try {
            Response response = client.performRequest(
                    "GET",
                    indexString + "/_search",
                    new Hashtable<>(),
                    new StringEntity(query, Charset.defaultCharset()));

            handler.handle(response.getEntity());

            response.close();

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

    @PreDestroy
    public void destroy() {
        try {
            this.sniffer.close();
        } catch (IOException e) {
            logger.warn("Failed to close the elasticsearch sniffer");
        }
    }
}
