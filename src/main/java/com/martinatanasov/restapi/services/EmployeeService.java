package com.martinatanasov.restapi.services;

import com.martinatanasov.restapi.model.EmployeeDTO;
import com.martinatanasov.restapi.model.EmployeeLoginDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface EmployeeService {

    Page<EmployeeDTO> getAllEmployees(Pageable pageable);

    Optional<EmployeeDTO> getEmployee(Integer id);

    Optional<EmployeeDTO> getEmployeeByEmail(String email);

    Optional<EmployeeDTO> getFirstEmployeeByFirstName(String firstName);

    EmployeeDTO addEmployee(EmployeeLoginDTO employeeLoginDTO);

    Optional<EmployeeDTO> updateEmployee(Integer employeeId, EmployeeDTO employeeDTO);

    void deleteEmployee(Integer employeeId);

}
