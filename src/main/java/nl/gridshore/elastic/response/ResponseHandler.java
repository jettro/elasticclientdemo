package nl.gridshore.elastic.response;

import org.apache.http.HttpEntity;

public interface ResponseHandler {
    void handle(HttpEntity httpEntity);
}
