package nl.gridshore.employees;

import com.fasterxml.jackson.core.type.TypeReference;
import nl.gridshore.elastic.query.response.QueryResponse;

/**
 * TypeReference ref = new TypeReference<ResponseHits<Employee>>() {};
 */
public class EmployeeTypeReference extends TypeReference<QueryResponse<Employee>> {
}
