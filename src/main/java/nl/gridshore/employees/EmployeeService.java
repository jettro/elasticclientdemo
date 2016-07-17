package nl.gridshore.employees;

import nl.gridshore.elastic.IndexTemplate;
import nl.gridshore.elastic.QueryTemplate;
import nl.gridshore.elastic.QueryTemplateFactory;
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

    private QueryTemplateFactory queryTemplateFactory;

    @Autowired
    public EmployeeService(QueryTemplateFactory queryTemplateFactory) {
        this.queryTemplateFactory = queryTemplateFactory;
    }

    public void createEmployee(Employee employee) {
        IndexTemplate<Employee> indexTemplate = queryTemplateFactory.createIndexTemplate();
        indexTemplate.setDocToIndex(employee);
        indexTemplate.setIndex(INDEX);
        indexTemplate.setType(TYPE);
        indexTemplate.execute();
    }

    public List<Employee> queryForEmployees(String name) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("operator", "and");

        QueryTemplate<Employee> queryTemplate = queryTemplateFactory.createQueryTemplate();
        queryTemplate.setIndexString(INDEX);
        queryTemplate.setQueryFromTemplate("find_employee.twig", params);
        queryTemplate.setQueryTypeReference(new EmployeeTypeReference());

        return queryTemplate.execute();
    }

    public List<Employee> queryForEmployeesByNameAndEmail(String searchString) {
        Map<String, Object> params = new HashMap<>();
        params.put("searchText", searchString);
        params.put("operator", "and");

        QueryTemplate<Employee> queryTemplate = queryTemplateFactory.createQueryTemplate();
        queryTemplate.setIndexString(INDEX);
        queryTemplate.setQueryFromTemplate("find_employee_by_email.twig", params);
        queryTemplate.setQueryTypeReference(new EmployeeTypeReference());

        return queryTemplate.execute();
    }

}
