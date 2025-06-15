package com.martinatanasov.restapi.mappers;

import com.martinatanasov.restapi.entities.Employee;
import com.martinatanasov.restapi.model.EmployeeDTO;
import org.mapstruct.Mapper;

@Mapper
public interface EmployeeMapper {

    EmployeeDTO toEmployeeDTO(Employee employee);

    Employee toEmployee(EmployeeDTO employeeDTO);

}
