package nl.gridshore.employees;

import nl.gridshore.elastic.ElasticTemplate;
import nl.gridshore.elastic.IndexRequest;
import nl.gridshore.elastic.QueryByIdRequest;
import nl.gridshore.elastic.QueryByTemplateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jettrocoenradie on 16/07/16.
 */
@Service
public class EmployeeService {
    public static final String INDEX = "luminis";
    private static final String TYPE = "ams";

    private final ElasticTemplate elasticTemplate;

    @Autowired
    public EmployeeService(ElasticTemplate elasticTemplate) {
        this.elasticTemplate = elasticTemplate;
    }

    public void createEmployee(Employee employee) {
        IndexRequest request = IndexRequest.create()
                .setIndex(INDEX)
                .setType(TYPE)
                .setEntity(employee);

        elasticTemplate.index(request);
    }

    public List<Employee> queryForEmployees(String name) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("operator", "and");

        QueryByTemplateRequest request = QueryByTemplateRequest.create()
                .setAddId(true)
                .setTypeReference(new EmployeeTypeReference())
                .setIndexName(INDEX)
                .setModelParams(params)
                .setTemplateName("find_employee.twig");

        return elasticTemplate.queryByTemplate(request);
    }

    public List<Employee> queryForEmployeesByNameAndEmail(String searchString) {
        Map<String, Object> params = new HashMap<>();
        params.put("searchText", searchString);
        params.put("operator", "and");

        QueryByTemplateRequest request = QueryByTemplateRequest.create()
                .setAddId(true)
                .setTypeReference(new EmployeeTypeReference())
                .setIndexName(INDEX)
                .setModelParams(params)
                .setTemplateName("find_employee_by_email.twig");

        return elasticTemplate.queryByTemplate(request);
    }

    public Employee loadEmployeeById(String id) {
        QueryByIdRequest request = QueryByIdRequest.create()
                .setAddId(true)
                .setIndex(INDEX)
                .setType(TYPE)
                .setId(id)
                .setTypeReference(new EmployeeByIdTypeReference());

        return elasticTemplate.querybyId(request);
    }
}
