package nl.gridshore.elastic.index;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by jettrocoenradie on 25/07/16.
 */
@Component
public class IndexTemplateFactory {
    private final IndexService indexService;
    private final ObjectMapper jacksonObjectMapper;

    @Autowired
    public IndexTemplateFactory(IndexService indexService, ObjectMapper jacksonObjectMapper) {
        this.indexService = indexService;
        this.jacksonObjectMapper = jacksonObjectMapper;
    }

    public <T> IndexTemplate<T> createIndexTemplate() {
        return new IndexTemplate<T>(indexService, jacksonObjectMapper);
    }

}
