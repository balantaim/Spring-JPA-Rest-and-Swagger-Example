package com.martinatanasov.restapi.mockmvc;

import com.martinatanasov.restapi.controllers.AuthController;
import com.martinatanasov.restapi.security.SecurityConfig;
import com.martinatanasov.restapi.services.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

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
    @Autowired
    private JsonMapper jsonMapper;

    @Test
    void getToken() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/auth/token")
                        .with(httpBasic("abv@abv.bg", "password"))
                )
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        JsonNode rootNode = jsonMapper.readTree(responseBody);
        String token = rootNode.path("token").asString();

        log.info("Token: {}", token);
    }


}
