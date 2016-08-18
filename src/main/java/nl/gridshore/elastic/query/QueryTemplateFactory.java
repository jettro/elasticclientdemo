package nl.gridshore.elastic.query;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.gridshore.elastic.index.IndexService;
import nl.gridshore.elastic.index.IndexTemplate;
import nl.gridshore.elastic.query.QueryService;
import nl.gridshore.elastic.query.QueryTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QueryTemplateFactory {
    private final QueryService queryService;
    private final ObjectMapper jacksonObjectMapper;

    @Autowired
    public QueryTemplateFactory(QueryService queryService, ObjectMapper jacksonObjectMapper) {
        this.queryService = queryService;
        this.jacksonObjectMapper = jacksonObjectMapper;
    }

    public <T> QueryTemplate<T> createQueryTemplate() {
        return new QueryTemplate<T>(queryService, jacksonObjectMapper);
    }

    public <T> QueryByIdTemplate<T> createQueryByIdTemplate() {
        return new QueryByIdTemplate<T>(queryService, jacksonObjectMapper);
    }
}
