package nl.gridshore.elastic;

import org.apache.http.HttpEntity;

public interface ResponseHandler {
    void handle(HttpEntity httpEntity);
}
