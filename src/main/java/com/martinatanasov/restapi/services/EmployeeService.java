package com.martinatanasov.restapi.services;

import com.martinatanasov.restapi.model.EmployeeDTO;
import com.martinatanasov.restapi.model.EmployeeLoginDTO;
import com.martinatanasov.restapi.result.EmployeeResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {

    Page<EmployeeDTO> getAllEmployees(Pageable pageable);

    EmployeeResult getEmployee(Integer id);

    EmployeeResult getEmployeeByEmail(String email);

    EmployeeResult getFirstEmployeeByFirstName(String firstName);

    EmployeeResult addEmployee(EmployeeLoginDTO employeeLoginDTO);

    EmployeeResult updateEmployee(Integer employeeId, EmployeeDTO employeeDTO);

    void deleteEmployee(Integer employeeId);

}
