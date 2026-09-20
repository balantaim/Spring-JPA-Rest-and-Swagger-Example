package com.martinatanasov.restapi.services;

import com.martinatanasov.restapi.entities.Employee;
import com.martinatanasov.restapi.exception.ResourceAlreadyExistsException;
import com.martinatanasov.restapi.mappers.EmployeeMapper;
import com.martinatanasov.restapi.model.EmployeeDTO;
import com.martinatanasov.restapi.model.EmployeeLoginDTO;
import com.martinatanasov.restapi.repositories.EmployeeRepository;
import com.martinatanasov.restapi.result.EmployeeResult;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Service
class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeMapper mapper;
    private final EmployeeRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<EmployeeDTO> getAllEmployees(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toEmployeeDTO);
    }

    @Override
    public EmployeeResult getEmployee(final Integer id) {
        Optional<EmployeeDTO> employeeDTO = repository.findById(id).map(mapper::toEmployeeDTO);
        if (employeeDTO.isPresent()) {
            return new EmployeeResult.Success(employeeDTO.get());
        }
        return new EmployeeResult.NotFound();
    }

    @Override
    public EmployeeResult getFirstEmployeeByFirstName(final String firstName) {
        Optional<EmployeeDTO> employeeDTO = repository.findFirstByFirstName(firstName)
                .map(mapper::toEmployeeDTO);
        if (employeeDTO.isPresent()) {
            return new EmployeeResult.Success(employeeDTO.get());
        }
        return new EmployeeResult.NotFound();
    }

    @Override
    public EmployeeResult getEmployeeByEmail(final String email) {
        Optional<EmployeeDTO> employeeDTO = repository.findByEmail(email).map(mapper::toEmployeeDTO);
        if (employeeDTO.isPresent()) {
            return new EmployeeResult.Success(employeeDTO.get());
        }
        return new EmployeeResult.NotFound();
    }

    @Transactional
    @Override
    public EmployeeResult addEmployee(EmployeeLoginDTO employeeLoginDTO) {
        Optional<Employee> existingEmployee = repository.findByEmail(employeeLoginDTO.email());
        if (existingEmployee.isPresent()) {
            log.error("Employee with this email already exists: {}", employeeLoginDTO.email());
            throw new ResourceAlreadyExistsException("Employee with this email already exists: " + employeeLoginDTO.email());
        }
        Employee newEmployee = mapper.toEmployee(employeeLoginDTO);
        //Encode employee's password
        newEmployee.setPassword(passwordEncoder.encode(employeeLoginDTO.password()));

        final Employee savedEmployee = repository.save(newEmployee);
        log.info("Added new employee: {}", savedEmployee);
        return new EmployeeResult.Success(mapper.toEmployeeDTO(savedEmployee));
    }

    @Transactional
    @Override
    public EmployeeResult updateEmployee(final Integer employeeId, EmployeeDTO employeeDTO) {
        Optional<EmployeeDTO> newEmployeeDTO = repository.findById(employeeId)
                .map(existing -> {
                    existing.setFirstName(employeeDTO.firstName());
                    existing.setLastName(employeeDTO.lastName());
                    existing.setEmail(employeeDTO.email());
                    return mapper.toEmployeeDTO(repository.save(existing));
                });
        if (newEmployeeDTO.isPresent()) {
            return new EmployeeResult.Success(newEmployeeDTO.get());
        }
        return  new EmployeeResult.NotFound();
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
