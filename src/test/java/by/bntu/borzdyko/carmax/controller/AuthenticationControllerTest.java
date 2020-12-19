package by.bntu.borzdyko.carmax.controller;

import by.bntu.borzdyko.carmax.model.Role;
import by.bntu.borzdyko.carmax.model.User;
import by.bntu.borzdyko.carmax.repository.UserRepository;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class AuthenticationControllerTest {

    private static final User ADMIN = User.builder().email("admin@test.com").password("root").role(Role.ADMIN)
            .firstName("first").secondName("second").build();
    private static final User ADD_USER = User.builder().email("add@test.com").password("root").role(Role.USER)
            .status(true).firstName("first").secondName("second").build();
    private static final User EMPTY_USER = new User();

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    private static MultiValueMap<String, String> userCorrectParams;
    private static MultiValueMap<String, String> userWrongParams;

    @BeforeClass
    public static void setUp() {
        userCorrectParams = new LinkedMultiValueMap<>();
        userCorrectParams.add("email", ADD_USER.getEmail());
        userCorrectParams.add("password", ADD_USER.getPassword());
        userCorrectParams.add("firstName", ADD_USER.getFirstName());
        userCorrectParams.add("secondName", ADD_USER.getSecondName());

        userWrongParams = new LinkedMultiValueMap<>();
        userWrongParams.add("email", "1");
        userWrongParams.add("password", "___");
        userWrongParams.add("firstName", "i");
        userWrongParams.add("secondName", "a");
    }

    @Test
    public void getLoginPage_tryToGetPage_loginPage() throws Exception {
        this.mockMvc.perform(get("/auth/login"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    public void getLoginPage_tryWithCorrectLogin_redirect3xxCarmax() throws Exception {
        this.mockMvc.perform(formLogin("/auth/login").user(ADMIN.getEmail()).password(ADMIN.getPassword()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/carmax"));

        assertNotNull(userRepository.findByEmail(ADMIN.getEmail()));
    }

    @Test
    public void getLoginPage_tryWithBadCredentials_redirect3xxLoginErrorPage() throws Exception {
        this.mockMvc.perform(formLogin("/auth/login")
                .user(userWrongParams.getFirst("email")).password(userWrongParams.getFirst("password")))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth/login?error"));

        assertEquals(Optional.empty(), userRepository.findByEmail(userWrongParams.getFirst("email")));
    }

    @Test
    public void getRegistrationPage_tryToGetRegistrationPage_registrationPage() throws Exception {
        this.mockMvc.perform(get("/auth/registration"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", EMPTY_USER))
                .andExpect(view().name("registration"));
    }

    @Test
    public void addUser_tryWithCorrectInputs_redirect3xxCarmax() throws Exception {
        int expectedSize = userRepository.findAll().size() + 1;

        this.mockMvc.perform(post("/auth/create").params(userCorrectParams))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/carmax"));

        assertNotNull(userRepository.findByEmail(ADD_USER.getEmail()));
        assertEquals(expectedSize, userRepository.findAll().size());
    }

    @Test
    public void addUser_tryWithWrongInputs_redirect3xxRegistration() throws Exception {
        int expectedSize = userRepository.findAll().size();

        this.mockMvc.perform(post("/auth/create").params(userWrongParams))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("registration"));

        assertEquals(expectedSize, userRepository.findAll().size());
    }
}