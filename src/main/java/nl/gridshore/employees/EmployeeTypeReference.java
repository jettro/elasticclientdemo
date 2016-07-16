package nl.gridshore.employees;

import com.fasterxml.jackson.core.type.TypeReference;
import nl.gridshore.elastic.response.ResponseHits;
import nl.gridshore.employees.Employee;

/**
 * TypeReference ref = new TypeReference<ResponseHits<Employee>>() {};
 */
public class EmployeeTypeReference extends TypeReference<ResponseHits<Employee>> {
}
