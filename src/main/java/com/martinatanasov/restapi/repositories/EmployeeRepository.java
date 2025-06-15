package com.martinatanasov.restapi.repositories;

import com.martinatanasov.restapi.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//This is the way to specify custom name of the object
//@RepositoryRestResource(path = "person")
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
