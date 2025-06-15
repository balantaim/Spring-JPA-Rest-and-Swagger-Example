package com.martinatanasov.restapi.services;

import com.martinatanasov.restapi.model.EmployeeDTO;

import java.util.Set;

public interface EmployeeService {

    Set<EmployeeDTO> getAllEmployees();

    EmployeeDTO getEmployee(Integer id);

    EmployeeDTO addEmployee(EmployeeDTO employeeDTO);

    EmployeeDTO updateEmployee(Integer employeeId, EmployeeDTO employeeDTO);

    void deleteEmployee(Integer employeeId);

}
