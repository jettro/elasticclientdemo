package nl.gridshore.elastic.query;

import nl.gridshore.elastic.query.response.GetByIdResponse;
import org.apache.http.HttpEntity;

public interface GetIdResponseHandler {
    void handle(HttpEntity entity);
}
