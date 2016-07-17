package nl.gridshore.elastic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.gridshore.elastic.response.ResponseHits;
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

    private final ConnectionService connectionService;
    private final ObjectMapper jacksonObjectMapper;

    private String query;
    private String indexString;
    private TypeReference typeReference;

    public QueryTemplate(ConnectionService connectionService, ObjectMapper jacksonObjectMapper) {
        this.connectionService = connectionService;
        this.jacksonObjectMapper = jacksonObjectMapper;
    }

    public List<T> execute() {
        List<T> result = new ArrayList<>();

        this.connectionService.executeQuery(indexString, query(), entity -> {
            try {
                ResponseHits<T> responseHits = jacksonObjectMapper.readValue(entity.getContent(), this.typeReference);

                responseHits.getHits().getHits().forEach(tHit -> {
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
