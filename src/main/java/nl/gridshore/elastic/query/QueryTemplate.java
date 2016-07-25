package nl.gridshore.elastic.query;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.gridshore.elastic.query.response.QueryResponse;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QueryTemplate<T> {
    private final static Logger logger = LoggerFactory.getLogger(QueryTemplate.class);

    private final QueryService queryService;
    private final ObjectMapper jacksonObjectMapper;

    private String query;
    private String indexString;
    private TypeReference typeReference;

    public QueryTemplate(QueryService queryService, ObjectMapper jacksonObjectMapper) {
        this.queryService = queryService;
        this.jacksonObjectMapper = jacksonObjectMapper;
    }

    public List<T> execute() {
        List<T> result = new ArrayList<>();

        this.queryService.executeQuery(indexString, query(), entity -> {
            try {
                QueryResponse<T> queryResponse = jacksonObjectMapper.readValue(entity.getContent(), this.typeReference);

                queryResponse.getHits().getHits().forEach(tHit -> {
                    result.add(tHit.getSource());
                });
            } catch (IOException e) {
                logger.warn("Cannot execute query", e);
            }
        });

        return result;
    }

    public void setQueryFromTemplate(String templateName, Map<String, Object> modelParams) {
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/" + templateName);
        JtwigModel model = JtwigModel.newModel();
        modelParams.forEach(model::with);

        this.query = template.render(model);
    }

    public void setQueryTypeReference(TypeReference typeReference) {
        this.typeReference = typeReference;
    }

    public void setIndexString(String indexString) {
        this.indexString = indexString;
    }

    public String query() {
        return this.query;
    }
}
