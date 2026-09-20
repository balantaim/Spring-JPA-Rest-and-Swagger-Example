package com.martinatanasov.restapi.controllers;

import com.martinatanasov.restapi.model.EmployeeDTO;
import com.martinatanasov.restapi.model.EmployeeLoginDTO;
import com.martinatanasov.restapi.result.EmployeeResult;
import com.martinatanasov.restapi.services.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

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
    public ResponseEntity<Page<EmployeeDTO>> getAllEmployees(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        return ResponseEntity.ok(employeeService.getAllEmployees(getPage(page, size)));
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
        return handleResult(employeeService.getEmployee(employeeId));
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
        return handleResult(employeeService.getFirstEmployeeByFirstName(name));
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
        return handleResult(employeeService.getEmployeeByEmail(email));
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
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, value = BASE_PATH + "/employees")
    public ResponseEntity<EmployeeDTO> registerEmployee(@Valid @RequestBody EmployeeLoginDTO employeeLoginDTO) {
        if (employeeLoginDTO.id() == null) {
            return handleCreateResult(employeeService.addEmployee(employeeLoginDTO));
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
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, value = BASE_PATH + "/employees/{employeeId}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@Valid @RequestBody EmployeeDTO employeeDTO,
            @PathVariable final Integer employeeId) {
        return handleResult(employeeService.updateEmployee(employeeId, employeeDTO));
    }

    @Operation(summary = "Delete employees", description = "Delete single employee")
    @ApiResponse(responseCode = "204", description = "Employee deleted")
    @DeleteMapping(BASE_PATH + "/employees/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Integer employeeId) {
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.noContent().build();
    }

    private static Pageable getPage(@Nullable Integer page, @Nullable Integer size) {
        if (page == null) {
            page = 0;
        }
        if (size == null || size > 30) {
            size = 5;
        }
        return PageRequest.of(page, size);
    }

    private static ResponseEntity<EmployeeDTO> handleResult(EmployeeResult result) {
        return switch (result) {
            case EmployeeResult.Success success -> ResponseEntity.ok(success.employee());
            case EmployeeResult.NotFound ignored -> ResponseEntity.notFound().build();
        };
    }

    private static ResponseEntity<EmployeeDTO> handleCreateResult(EmployeeResult result) {
        return switch (result) {
            case EmployeeResult.Success success -> ResponseEntity.created(URI.create("/api/v1/employees/" + success.employee().id()))
                    .body(success.employee());
            case EmployeeResult.NotFound ignored -> ResponseEntity.notFound().build();
        };
    }

}
