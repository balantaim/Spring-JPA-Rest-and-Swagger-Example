package com.martinatanasov.restapi.boostrap;

import com.martinatanasov.restapi.entities.Employee;
import com.martinatanasov.restapi.repositories.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class InsertInitialData implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (employeeRepository.count() == 0) {
            Employee employee = getInitialUser();
            employeeRepository.save(employee);

            log.info("Datasource is filed with data");
        }
    }

    private Employee getInitialUser() {
        Employee employee = new Employee();
        employee.setFirstName("Martin");
        employee.setLastName("Atanasov");
        employee.setEmail("abv@abv.bg");
        employee.setPassword(passwordEncoder.encode("password"));
        return employee;
    }

}
