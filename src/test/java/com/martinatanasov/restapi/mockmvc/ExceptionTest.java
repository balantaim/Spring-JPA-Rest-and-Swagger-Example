package com.martinatanasov.restapi.mockmvc;


import com.martinatanasov.restapi.controllers.AuthController;
import com.martinatanasov.restapi.controllers.EmployeeController;
import com.martinatanasov.restapi.security.SecurityConfig;
import com.martinatanasov.restapi.services.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import({SecurityConfig.class, TokenService.class})
public class ExceptionTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeController employeeController;

    @Test
    void testUnauthorizedAccess() throws Exception {
        mockMvc.perform(get("/api/v1/employees"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void testResourceBadRequest() throws Exception {
        mockMvc.perform(get("/api/v1/employees/aa"))
                .andExpect(status().isBadRequest());
    }

}
