package com.martinatanasov.restapi.controller;

import com.martinatanasov.restapi.dao.EmployeeRepository;
import com.martinatanasov.restapi.entity.Employee;
import com.martinatanasov.restapi.exceptionhandler.EmployeeErrorResponse;
import com.martinatanasov.restapi.exceptionhandler.EmployeeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class CrudController {

    final private EmployeeRepository employeeRepository;

    @Autowired
    public CrudController(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/employees")
    public List<Employee> getAllEmployees(){
        //Return list of all employees from the Database
        return employeeRepository.findAll();
    }

    @GetMapping("/employees/{employeeId}")
    public Employee getEmployee(@PathVariable int employeeId){
        //Find record by ID and return optional
        Optional<Employee> result = employeeRepository.findById(employeeId);
        if (result.isPresent()){
            return result.get();
        } else {
            throw new EmployeeNotFoundException("Employee id not found " + employeeId);
        }
    }

    @PostMapping("/employees")
    public Employee addEmployee(@RequestBody Employee employee){
        //In order to force create new item set id to 0
        employee.setId(0);
        //Save the employee to the DB
        Employee resultEmployee = employeeRepository.save(employee);
        //Return updated result from the DB
        return  resultEmployee;
    }

    @PutMapping("/employees/{employeeId}")
    public Employee updateEmployee(@RequestBody Employee employee, @PathVariable int employeeId){
        //Find record by ID and return optional
        Optional<Employee> result = employeeRepository.findById(employeeId);
        if(result.isPresent()){
            //If the id from the database match Update old record
            Employee newEmployee = result.get();
            newEmployee.setFirstName(employee.getFirstName());
            newEmployee.setLastName(employee.getLastName());
            newEmployee.setEmail(employee.getEmail());
            employeeRepository.save(newEmployee);
            return newEmployee;
        }else{
            //Otherwise create new record
            employee.setId(employeeId);
            return employeeRepository.save(employee);
        }
    }

    @DeleteMapping("/employees/{employeeId}")
    public Employee deleteEmployee(@PathVariable int employeeId){
        Optional<Employee> result = employeeRepository.findById(employeeId);
        if (result.isPresent()){
            Employee employee = result.get();
            //Delete record with ID if the object exist in the database
            employeeRepository.deleteById(employeeId);
            return employee;
        } else {
            //throw new RuntimeException("Employee is not found: " + employeeId);
            throw new EmployeeNotFoundException("Employee id not found " + employeeId);
        }
    }

    @ExceptionHandler
    public ResponseEntity<EmployeeErrorResponse> handleException(EmployeeNotFoundException exception){
        EmployeeErrorResponse error = new EmployeeErrorResponse();
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exception.getMessage());
        error.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }


}
