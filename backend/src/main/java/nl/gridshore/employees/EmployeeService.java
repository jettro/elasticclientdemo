package nl.gridshore.employees;

import eu.luminis.elastic.document.DocumentService;
import eu.luminis.elastic.document.IndexRequest;
import eu.luminis.elastic.document.QueryByIdRequest;
import eu.luminis.elastic.document.QueryByTemplateRequest;
import eu.luminis.elastic.index.IndexService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jettrocoenradie on 16/07/16.
 */
@Service
public class EmployeeService {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    public static final String INDEX = "luminis";
    private static final String TYPE = "ams";

    private final DocumentService documentService;
    private final IndexService indexService;

    @Autowired
    public EmployeeService(DocumentService documentService, IndexService indexService) {
        this.documentService = documentService;
        this.indexService = indexService;
    }

    public String storeEmployee(Employee employee) {
        IndexRequest request = IndexRequest.create()
                .setIndex(INDEX)
                .setType(TYPE)
                .setEntity(employee);

        if (employee.getId() != null) {
            request.setId(employee.getId());
        }

        return documentService.index(request);
    }

    public List<Employee> queryForEmployees(String name) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("operator", "or");

        QueryByTemplateRequest request = QueryByTemplateRequest.create()
                .setAddId(true)
                .setTypeReference(new EmployeeTypeReference())
                .setIndexName(INDEX)
                .setModelParams(params)
                .setTemplateName("find_employee.twig");

        return documentService.queryByTemplate(request);
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

        return documentService.queryByTemplate(request);
    }

    public Employee loadEmployeeById(String id) {
        QueryByIdRequest request = QueryByIdRequest.create()
                .setAddId(true)
                .setIndex(INDEX)
                .setType(TYPE)
                .setId(id)
                .setTypeReference(new EmployeeByIdTypeReference());

        return documentService.querybyId(request);
    }

    public void removeEmployee(String id) {
        documentService.remove(INDEX, TYPE, id);
    }

    public void createIndex() {
        try {
            File file = ResourceUtils.getFile("classpath:elastic/luminis-mapping.json");
            FileReader fileReader = new FileReader(file);
            String body = FileCopyUtils.copyToString(fileReader);
            fileReader.close();
            indexService.createIndex(INDEX, body);
        } catch (IOException e) {
            logger.warn("Could not read mapping file for creating index", e);
        }
    }
}
