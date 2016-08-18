package nl.gridshore.elastic.query;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.gridshore.elastic.query.response.QueryResponse;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
    private boolean addId = false;

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
                    T source = tHit.getSource();
                    if (addId) {
                        Method setIdMethod = null;
                        try {
                            setIdMethod = source.getClass().getMethod("setId", String.class);
                            setIdMethod.invoke(source,tHit.getId());
                        } catch (NoSuchMethodException | InvocationTargetException e) {
                            throw new QueryExecutionException("The setter for the id method is not available.",e);
                        } catch (IllegalAccessException e) {
                            throw new QueryExecutionException("Id argument seems to be wrong",e);
                        }
                    }
                    result.add(source);
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

    public void addId(boolean addId) {
        this.addId = addId;
    }

    public String query() {
        return this.query;
    }
}
