package nl.gridshore.elastic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

public class IndexTemplate<T> {
    private final static Logger logger = LoggerFactory.getLogger(IndexTemplate.class);
    private final ConnectionService connectionService;
    private final ObjectMapper jacksonObjectMapper;

    private T docToIndex;
    private String index;
    private String type;

    public IndexTemplate(ConnectionService connectionService, ObjectMapper jacksonObjectMapper) {
        this.connectionService = connectionService;
        this.jacksonObjectMapper = jacksonObjectMapper;
    }

    public void execute() {
        try {
            HttpEntity requestBody = new StringEntity(jacksonObjectMapper.writeValueAsString(docToIndex), Charset.defaultCharset());
            connectionService.indexDocument(index,type,requestBody);
        } catch (JsonProcessingException e) {
            logger.warn("Error parsing doc to json", e);
            throw new IndexDocumentException("Could not transform provided document into json");
        }
    }

    public void setDocToIndex(T docToIndex) {
        this.docToIndex = docToIndex;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public void setType(String type) {
        this.type = type;
    }
}
