package com.gcp.pcd.example.employee.repository;

import com.gcp.pcd.example.employee.entity.Employee;
import org.springframework.data.repository.CrudRepository;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {
}
