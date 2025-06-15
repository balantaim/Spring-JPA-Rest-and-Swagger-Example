package com.martinatanasov.restapi.controllers;

import com.martinatanasov.restapi.model.EmployeeDTO;
import com.martinatanasov.restapi.services.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Set;


@Tag(name = "Employees REST API")
@RequiredArgsConstructor
@RestController
public class CrudController {

    private final EmployeeService employeeService;
    public static final String BASE_PATH = "/api/v1";

    @Operation(summary = "Get all employees", description = "Retrieve a list of all employees")
    @ApiResponse(responseCode = "200", description = "Found the employees", content = {
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = EmployeeDTO.class))
    })
    @ApiResponse(responseCode = "404", description = "Employees not found")
    @GetMapping(BASE_PATH + "/employees")
    public ResponseEntity<Set<EmployeeDTO>> getAllEmployees() {
        //Return list of all employees from the Database
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @Operation(summary = "Get employee", description = "Retrieve a single employee by id")
    @ApiResponse(responseCode = "200", description = "Found the employee", content = {
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = EmployeeDTO.class))
    })
    @ApiResponse(responseCode = "404", description = "Employee not found")
    @GetMapping(BASE_PATH + "/employees/{employeeId}")
    public ResponseEntity<EmployeeDTO> getEmployee(@PathVariable Integer employeeId) {
        if (employeeId != null) {
            return ResponseEntity.ok(employeeService.getEmployee(employeeId));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Add employee", description = "Add new employee record")
    @ApiResponse(responseCode = "201", description = "Create a new employee", content = {
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = EmployeeDTO.class))
    })
    @PostMapping(BASE_PATH + "/employees")
    public ResponseEntity<EmployeeDTO> addEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
        if (employeeDTO.id() == null) {
            EmployeeDTO createdEmployee = employeeService.addEmployee(employeeDTO);
            return ResponseEntity.created(URI.create("/api/v1/employees/" + createdEmployee.id()))
                    .body(createdEmployee);
        }
        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "Update employee", description = "Update existing employee")
    @ApiResponse(responseCode = "200", description = "Update the employee", content = {
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = EmployeeDTO.class))
    })
    @ApiResponse(responseCode = "404", description = "Employee not found")
    @PutMapping(BASE_PATH + "/employees/{employeeId}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@Valid @RequestBody EmployeeDTO employeeDTO,
                                                      @PathVariable Integer employeeId) {
        if (employeeId != null) {
            return ResponseEntity.ok(employeeService.updateEmployee(employeeId, employeeDTO));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete employees", description = "Delete single employee")
    @ApiResponse(responseCode = "204", description = "Employee deleted", content = {
            @Content(mediaType = "application/json",
                    schema = @Schema(implementation = EmployeeDTO.class))
    })
    @ApiResponse(responseCode = "404", description = "Employee not found")
    @DeleteMapping(BASE_PATH + "/employees/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Integer employeeId) {
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.noContent().build();
    }

}
