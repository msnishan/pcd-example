package com.gcp.pcd.example.employee.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gcp.pcd.example.employee.entity.Employee;
import com.gcp.pcd.example.employee.pubsub.MessagePublisher;
import com.gcp.pcd.example.employee.service.EmployeeService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;
    
    private final MessagePublisher messagePublisher;
    
    private final ObjectMapper objectMapper;

    public EmployeeController(EmployeeService employeeService, MessagePublisher messagePublisher) {
        this.employeeService = employeeService;
        this.messagePublisher = messagePublisher;
        this.objectMapper = new ObjectMapper();
    }

    @GetMapping
    public Iterable<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public Optional<Employee> getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id);
    }

    @PostMapping
    public Employee createEmployee(@RequestBody Employee employee) {
        Employee employeeRes = employeeService.saveEmployee(employee);
        String employeeString = getEmployeeString(employeeRes);
        messagePublisher.publishMessageToTopic("GCP_Topic_Employee", employeeString);
        return employeeRes;
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
    }

    private String getEmployeeString(Employee employeeRes) {
        try {
            return objectMapper.writeValueAsString(employeeRes);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}