package com.martinatanasov.restapi.mockmvc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.martinatanasov.restapi.controllers.AuthController;
import com.martinatanasov.restapi.security.SecurityConfig;
import com.martinatanasov.restapi.services.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(AuthController.class)
@Import({SecurityConfig.class})
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private AuthController authController;

    @Test
    void getToken() throws Exception {
        final ObjectMapper mapper = new ObjectMapper();
        MvcResult result = mockMvc.perform(post("/api/v1/auth/token")
                        .with(httpBasic("user", "password"))
                )
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JsonNode rootNode = mapper.readTree(responseBody);
        String token = rootNode.path("token").asText();
        log.info("Token: {}", token);
    }


}
