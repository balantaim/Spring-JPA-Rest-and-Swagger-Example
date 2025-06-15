package com.martinatanasov.restapi.services;


import com.martinatanasov.restapi.entities.Employee;
import com.martinatanasov.restapi.mappers.EmployeeMapper;
import com.martinatanasov.restapi.model.EmployeeDTO;
import com.martinatanasov.restapi.repositories.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmployeeServiceImpl implements EmployeeService{

    private final EmployeeMapper employeeMapper;
    private final EmployeeRepository repository;

    @Override
    public Set<EmployeeDTO> getAllEmployees() {
        return repository.findAll()
                .stream()
                .map(employeeMapper::toEmployeeDTO)
                .collect(Collectors.toSet());
    }

    @Override
    public EmployeeDTO getEmployee(final Integer id) {
        return repository.findById(id)
                .map(employeeMapper::toEmployeeDTO)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id));
    }

    @Override
    public EmployeeDTO addEmployee(EmployeeDTO employeeDTO) {
        final Employee employee = repository.save(employeeMapper.toEmployee(employeeDTO));
        log.info("Added new employee: {}", employee);
        return employeeMapper.toEmployeeDTO(employee);
    }

    @Override
    public EmployeeDTO updateEmployee(final Integer employeeId, EmployeeDTO employeeDTO) {
        final Optional<Employee> optionalEmployee = repository.findById(employeeId);
        if (optionalEmployee.isPresent()) {

            final Employee employee = Employee.builder()
                    .id(employeeId)
                    .firstName(employeeDTO.firstName())
                    .lastName(employeeDTO.lastName())
                    .email(employeeDTO.email())
                    .build();

            final Employee updatedEmployee = repository.save(employee);
            log.info("Updated employee: {}", updatedEmployee);
            return employeeMapper.toEmployeeDTO(updatedEmployee);
        } else {
            log.error("Employee not found for ID: {}", employeeId);
            throw new EntityNotFoundException("Employee not found");
        }
    }

    @Override
    public void deleteEmployee(final Integer employeeId) {
        final Optional<Employee> optionalEmployee = repository.findById(employeeId);
        if (optionalEmployee.isPresent()) {
            repository.delete(optionalEmployee.get());
            log.info("Deleted employee with ID: {}", employeeId);
        } else {
            log.error("Employee not found for ID: {}", employeeId);
            throw new EntityNotFoundException("Employee not found");
        }
    }

}
