package nl.gridshore.elastic.query;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.gridshore.elastic.query.response.GetByIdResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class QueryByIdTemplate<T> {
    private static final Logger logger = LoggerFactory.getLogger(QueryByIdTemplate.class);

    private final QueryService queryService;
    private final ObjectMapper jacksonObjectMapper;

    private TypeReference typeReference;
    private String index;
    private String type;
    private String id;

    public QueryByIdTemplate(QueryService queryService, ObjectMapper jacksonObjectMapper) {
        this.queryService = queryService;
        this.jacksonObjectMapper = jacksonObjectMapper;
    }

    public T execute() {
        TWrapper<T> wrapper = new TWrapper<>();
        this.queryService.executeGetById(index, type, id, entity -> {
            try {
                GetByIdResponse<T> queryResponse = jacksonObjectMapper.readValue(entity.getContent(), this.typeReference);

                if (!queryResponse.getFound()) {
                    throw new QueryByIdNotFoundException(index, type, id);
                }

                T source = queryResponse.getSource();
                wrapper.setT(source);
            } catch (IOException e) {
                logger.warn("Cannot execute query", e);
            }
        });
        return wrapper.getT();
    }

    public TypeReference getTypeReference() {
        return typeReference;
    }

    public void setTypeReference(TypeReference typeReference) {
        this.typeReference = typeReference;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private class TWrapper<T> {
        T t;

        public T getT() {
            return t;
        }

        public void setT(T t) {
            this.t = t;
        }
    }
}
