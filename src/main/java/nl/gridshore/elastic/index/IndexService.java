package nl.gridshore.elastic.index;

import nl.gridshore.elastic.query.QueryService;
import nl.gridshore.elastic.query.QueryExecutionException;
import org.apache.http.HttpEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Hashtable;

/**
 * Exposes index api related services.
 */
@Service
public class IndexService {
    private static final Logger logger = LoggerFactory.getLogger(QueryService.class);

    private final RestClient client;

    @Autowired
    public IndexService(RestClient client) {
        this.client = client;
    }

    public Boolean indexExist(String indexName) {
        try {
            Response response = client.performRequest(
                    "HEAD",
                    indexName,
                    new Hashtable<>(),
                    null
            );

            int statusCode = response.getStatusLine().getStatusCode();

            response.close();

            if (statusCode == 200) {
                return true;
            } else if (statusCode == 404) {
                return false;
            } else {
                throw new QueryExecutionException("Could not check index existence, status code is " + statusCode);
            }
        } catch (IOException e) {
            logger.warn("Problem while verifying if index exists.", e);
            throw new IndexApiException("Error when checking for existing index.");
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
            throw new IndexDocumentException("Error when executing a query");
        }

    }
}
