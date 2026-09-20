package com.martinatanasov.restapi.result;

import com.martinatanasov.restapi.model.EmployeeDTO;

public sealed interface EmployeeResult {

    record Success(EmployeeDTO employee) implements EmployeeResult {}

    record NotFound() implements  EmployeeResult {}

}
