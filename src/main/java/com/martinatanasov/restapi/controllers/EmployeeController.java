package com.martinatanasov.restapi.controllers;

import com.martinatanasov.restapi.model.EmployeeDTO;
import com.martinatanasov.restapi.model.EmployeeLoginDTO;
import com.martinatanasov.restapi.services.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;
import java.util.Set;


@Tag(name = "Employees REST API")
@RequiredArgsConstructor
@RestController
public class EmployeeController {

    private final EmployeeService employeeService;
    public static final String BASE_PATH = "/api/v1";

    @Operation(summary = "Get all employees", description = "Retrieve a list of all employees")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found the employees", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeDTO.class))
            }),
            @ApiResponse(responseCode = "404", description = "Employees not found", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(type = "string"))
            })
    })
    @GetMapping(BASE_PATH + "/employees")
    public ResponseEntity<Set<EmployeeDTO>> getAllEmployees() {
        //Return list of all employees from the Database
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @Operation(summary = "Get employee", description = "Retrieve a single employee by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found the employee", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeDTO.class))
            }),
            @ApiResponse(responseCode = "404", description = "Employee not found", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(type = "string"))
            })
    })
    @GetMapping(BASE_PATH + "/employees/{employeeId}")
    public ResponseEntity<EmployeeDTO> getEmployee(@PathVariable final Integer employeeId) {
        return employeeService.getEmployee(employeeId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get employee by first name", description = "Retrieve a single employee by first name")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found the employee", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeDTO.class))
            }),
            @ApiResponse(responseCode = "404", description = "Employee not found", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(type = "string"))
            })
    })
    @GetMapping(BASE_PATH + "/employees/names/{name}")
    public ResponseEntity<EmployeeDTO> getEmployeeByFirstName(@PathVariable final String name) {
        return employeeService.getFirstEmployeeByFirstName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get employee by email", description = "Retrieve a single employee by email")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found the employee", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeDTO.class))
            }),
            @ApiResponse(responseCode = "404", description = "Employee not found", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(type = "string"))
            })
    })
    @GetMapping(BASE_PATH + "/employees/emails/{email}")
    public ResponseEntity<EmployeeDTO> getEmployeeByEmail(@PathVariable final String email) {
        return employeeService.getEmployeeByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Add employee", description = "Add new employee record")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Create a new employee", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeDTO.class))
            }),
            @ApiResponse(responseCode = "409", description = "Employee already exists", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(type = "string"))
            })
    })
    @PostMapping(BASE_PATH + "/employees")
    public ResponseEntity<EmployeeDTO> registerEmployee(@Valid @RequestBody EmployeeLoginDTO employeeLoginDTO) {
        if (employeeLoginDTO.id() == null) {
            final EmployeeDTO createdEmployee = employeeService.addEmployee(employeeLoginDTO);
            return ResponseEntity.created(URI.create("/api/v1/employees/" + createdEmployee.id()))
                    .body(createdEmployee);
        }
        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "Update employee", description = "Update existing employee")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Update the employee", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeDTO.class))
            }),
            @ApiResponse(responseCode = "404", description = "Employee not found", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(type = "string"))
            })
    })
    @PutMapping(BASE_PATH + "/employees/{employeeId}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@Valid @RequestBody EmployeeDTO employeeDTO,
                                                      @PathVariable final Integer employeeId) {
        Optional<EmployeeDTO> updatedEmployeeDTO = employeeService.updateEmployee(employeeId, employeeDTO);

        return updatedEmployeeDTO.map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));
    }

    @Operation(summary = "Delete employees", description = "Delete single employee")
    @ApiResponse(responseCode = "204", description = "Employee deleted")
    @DeleteMapping(BASE_PATH + "/employees/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Integer employeeId) {
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.noContent().build();
    }

}
