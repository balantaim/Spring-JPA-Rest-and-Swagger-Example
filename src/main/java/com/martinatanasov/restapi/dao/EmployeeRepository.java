package com.martinatanasov.restapi.dao;

import com.martinatanasov.restapi.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

//This is the way to specify custom name of the object
//@RepositoryRestResource(path = "person")
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
