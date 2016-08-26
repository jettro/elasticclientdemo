package nl.gridshore.elastic;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.gridshore.elastic.index.IndexDocumentException;
import nl.gridshore.elastic.index.response.IndexResponse;
import nl.gridshore.elastic.query.QueryByIdNotFoundException;
import nl.gridshore.elastic.query.QueryExecutionException;
import nl.gridshore.elastic.query.response.GetByIdResponse;
import nl.gridshore.elastic.query.response.QueryResponse;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by jettrocoenradie on 23/08/2016.
 */
@Component
public class ElasticTemplate {
    private static final Logger logger = LoggerFactory.getLogger(ElasticTemplate.class);

    private final RestClient client;
    private final ObjectMapper jacksonObjectMapper;

    @Autowired
    public ElasticTemplate(RestClient client, ObjectMapper jacksonObjectMapper) {
        this.client = client;
        this.jacksonObjectMapper = jacksonObjectMapper;
    }

    public <T> List<T> queryByTemplate(QueryByTemplateRequest request) {
        Assert.notNull(request, "Need to provide a QueryByTemplateRequest object");

        try {
            Response response = client.performRequest(
                    "GET",
                    request.getIndexName() + "/_search",
                    new HashMap<>(),
                    new StringEntity(request.createQuery(), Charset.defaultCharset()));

            QueryResponse<T> queryResponse = jacksonObjectMapper.readValue(response.getEntity().getContent(), request.getTypeReference());

            List<T> result = new ArrayList<>();
            queryResponse.getHits().getHits().forEach(tHit -> {
                T source = tHit.getSource();
                if (request.getAddId()) {
                    addIdToEntity(tHit.getId(), source);
                }
                result.add(source);
            });

            return result;
        } catch (IOException e) {
            logger.warn("Problem while executing request.", e);
            throw new QueryExecutionException("Error when executing a query");
        }

    }

    public <T> T querybyId(QueryByIdRequest request) {
        try {
            Response response = client.performRequest(
                    "GET",
                    request.getIndex() + "/" + request.getType() + "/" + request.getId());

            GetByIdResponse<T> queryResponse = jacksonObjectMapper.readValue(response.getEntity().getContent(), request.getTypeReference());

            if (!queryResponse.getFound()) {
                throw new QueryByIdNotFoundException(request.getIndex(), request.getType(), request.getId());
            }

            T entity = queryResponse.getSource();

            if (request.getAddId()) {
                addIdToEntity(request.getId(), entity);
            }

            return entity;
        } catch (IOException e) {
            logger.warn("Problem while executing request.", e);
            throw new QueryExecutionException("Error when executing a query");
        }
    }

    public String index(IndexRequest indexRequest) {
        try {
            HttpEntity requestBody = new StringEntity(jacksonObjectMapper.writeValueAsString(indexRequest.getEntity()), Charset.defaultCharset());

            Response response;
            if (indexRequest.getId() != null) {
                response = client.performRequest(
                        "PUT",
                        indexRequest.getIndex() + "/" + indexRequest.getType() + "/" + indexRequest.getId(),
                        new Hashtable<>(),
                        requestBody);
            } else {
                response = client.performRequest(
                        "POST",
                        indexRequest.getIndex() + "/" + indexRequest.getType(),
                        new Hashtable<>(),
                        requestBody);
            }

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode > 299) {
                logger.warn("Problem while indexing a document: {}", response.getStatusLine().getReasonPhrase());
                throw new QueryExecutionException("Could not index a document, status code is " + statusCode);
            }

            IndexResponse queryResponse = jacksonObjectMapper.readValue(response.getEntity().getContent(), IndexResponse.class);

            return queryResponse.getId();

        } catch (IOException e) {
            logger.warn("Problem while executing request.", e);
            throw new IndexDocumentException("Error when executing a query");
        }

    }

    public String remove(String index, String type, String id) {
        try {
            Response response = client.performRequest(
                    "DELETE",
                    index + "/" + type + "/" + id,
                    new Hashtable<>());
            return response.getStatusLine().getReasonPhrase();
        } catch (IOException e) {
            logger.warn("Problem while removing a document.", e);
            throw new IndexDocumentException("Error when removing a document");
        }
    }


    private <T> void addIdToEntity(String id, T source) {
        Method setIdMethod;
        try {
            setIdMethod = source.getClass().getMethod("setId", String.class);
            setIdMethod.invoke(source, id);
        } catch (NoSuchMethodException | InvocationTargetException e) {
            throw new QueryExecutionException("The setter for the id method is not available.", e);
        } catch (IllegalAccessException e) {
            throw new QueryExecutionException("Id argument seems to be wrong", e);
        }
    }
}
