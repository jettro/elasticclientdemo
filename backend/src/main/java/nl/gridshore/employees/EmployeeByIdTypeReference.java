package nl.gridshore.employees;

import com.fasterxml.jackson.core.type.TypeReference;
import eu.luminis.elastic.document.response.GetByIdResponse;

/**
 * Created by jettrocoenradie on 18/08/2016.
 */
public class EmployeeByIdTypeReference extends TypeReference<GetByIdResponse<Employee>> {
}
