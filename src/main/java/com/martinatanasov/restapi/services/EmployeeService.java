package com.martinatanasov.restapi.services;

import com.martinatanasov.restapi.model.EmployeeDTO;
import com.martinatanasov.restapi.model.EmployeeLoginDTO;

import java.util.Optional;
import java.util.Set;

public interface EmployeeService {

    Set<EmployeeDTO> getAllEmployees();

    Optional<EmployeeDTO> getEmployee(Integer id);

    Optional<EmployeeDTO> getEmployeeByEmail(String email);

    Optional<EmployeeDTO> getFirstEmployeeByFirstName(String firstName);

    EmployeeDTO addEmployee(EmployeeLoginDTO employeeLoginDTO);

    Optional<EmployeeDTO> updateEmployee(Integer employeeId, EmployeeDTO employeeDTO);

    void deleteEmployee(Integer employeeId);

}
