package com.martinatanasov.restapi.services;


import com.martinatanasov.restapi.entities.Employee;
import com.martinatanasov.restapi.exception.ResourceAlreadyExistsException;
import com.martinatanasov.restapi.mappers.EmployeeMapper;
import com.martinatanasov.restapi.model.EmployeeDTO;
import com.martinatanasov.restapi.model.EmployeeLoginDTO;
import com.martinatanasov.restapi.repositories.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeMapper mapper;
    private final EmployeeRepository repository;

    @Override
    public Set<EmployeeDTO> getAllEmployees() {
        return repository.findAll()
                .stream()
                .map(mapper::toEmployeeDTO)
                .collect(Collectors.toSet());
    }

    @Override
    public Optional<EmployeeDTO> getEmployee(final Integer id) {
        return repository.findById(id).map(mapper::toEmployeeDTO);
    }

    @Override
    public Optional<EmployeeDTO> getFirstEmployeeByFirstName(final String firstName) {
        return repository.findFirstByFirstName(firstName).map(mapper::toEmployeeDTO);
    }

    @Override
    public Optional<EmployeeDTO> getEmployeeByEmail(final String email) {
        return repository.findByEmail(email).map(mapper::toEmployeeDTO);
    }

    @Transactional
    @Override
    public EmployeeDTO addEmployee(final EmployeeLoginDTO employeeLoginDTO) {
        final Optional<Employee> employee = repository.findByEmail(employeeLoginDTO.email());
        if (employee.isPresent()) {
            log.error("Employee with this email already exists: {}", employeeLoginDTO.email());
            throw new ResourceAlreadyExistsException("Employee with this email already exists: " + employeeLoginDTO.email());
        }
        final Employee savedEmployee = repository.save(mapper.toEmployee(employeeLoginDTO));
        log.info("Added new employee: {}", savedEmployee);
        return mapper.toEmployeeDTO(savedEmployee);
    }

    @Transactional
    @Override
    public Optional<EmployeeDTO> updateEmployee(final Integer employeeId, EmployeeDTO employeeDTO) {
        return repository.findById(employeeId)
                .map(existing -> {
                    existing.setFirstName(employeeDTO.firstName());
                    existing.setLastName(employeeDTO.lastName());
                    existing.setEmail(employeeDTO.email());
                    return mapper.toEmployeeDTO(repository.save(existing));
                });
    }

    @Transactional
    @Override
    public void deleteEmployee(final Integer employeeId) {
        final Optional<Employee> optionalEmployee = repository.findById(employeeId);
        if (optionalEmployee.isPresent()) {
            repository.delete(optionalEmployee.get());
            log.info("Deleted employee with ID: {}", employeeId);
        } else {
            log.error("Employee not found for ID: {}", employeeId);
        }
    }

}
