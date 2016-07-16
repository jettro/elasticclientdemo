package nl.gridshore.elastic;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QueryTemplateFactory {
    private final ConnectionService connectionService;
    private final ObjectMapper jacksonObjectMapper;

    @Autowired
    public QueryTemplateFactory(ConnectionService connectionService, ObjectMapper jacksonObjectMapper) {
        this.connectionService = connectionService;
        this.jacksonObjectMapper = jacksonObjectMapper;
    }

    public <T> QueryTemplate<T> createQueryTemplate() {
        return new QueryTemplate<T>(connectionService, jacksonObjectMapper);
    }

    public <T> IndexTemplate<T> createIndexTemplate() {
        return new IndexTemplate<T>(connectionService, jacksonObjectMapper);
    }
}
