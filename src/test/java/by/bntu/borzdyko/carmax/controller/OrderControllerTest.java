package by.bntu.borzdyko.carmax.controller;

import by.bntu.borzdyko.carmax.model.Order;
import by.bntu.borzdyko.carmax.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-car-before.sql","/create-user-before.sql","/create-order-before.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-order-after.sql", "/create-car-after.sql","/create-user-after.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class OrderControllerTest {

    private final Order USER_ORDER = Order.builder().id(2L).status(true).build();

    @Autowired
    MockMvc mockMvc;

    @Autowired
    OrderRepository orderRepository;

    @Test
    @WithUserDetails("admin@test.com")
    public void getOrders_tryToGetAllOrders_orders() throws Exception {
        USER_ORDER.setCar(orderRepository.getOne(USER_ORDER.getId()).getCar());
        USER_ORDER.setUser(orderRepository.getOne(USER_ORDER.getId()).getUser());

        this.mockMvc.perform(get("/orders"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(model().attribute("orders", hasSize(4)))
                .andExpect(model().attribute("orders", hasItem(USER_ORDER)))
                .andExpect(view().name("/order/orders"));

        assertEquals(USER_ORDER, orderRepository.getOne(USER_ORDER.getId()));
    }

    @Test
    @WithUserDetails("user1@test.com")
    public void getOrders_tryToGetUserOrders_orders() throws Exception {
        USER_ORDER.setUser(orderRepository.getOne(USER_ORDER.getId()).getUser());

        this.mockMvc.perform(get("/orders"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(model().attribute("orders", hasSize(1)))
                .andExpect(model().attribute("orders", hasItem(
                        hasProperty("user", is(USER_ORDER.getUser()))
                )))
                .andExpect(view().name("/order/orders"));

        assertNotNull(orderRepository.findAllByUser(USER_ORDER.getUser()));
    }

    @Test
    public void getOrders_tryNotAuthentication_redirect3xxLogin() throws Exception {
        this.mockMvc.perform(get("/orders"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/auth/login"));
    }

    @Test
    @WithUserDetails("user1@test.com")
    public void addNewOrder_tryWithCorrectData_redirect3xxProfile() throws Exception {
        long carId = 1L;
        int expectedSize = orderRepository.findAll().size() + 1;

        this.mockMvc.perform(post("/orders/add")
                .param("id", Long.toString(carId)))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/profile"));

        assertEquals(expectedSize, orderRepository.findAll().size());
    }

    @Test
    @WithUserDetails("admin@test.com")
    public void confirmOrder_tryToConfirmExistedOrder_redirect3xxOrders() throws Exception {
        long notConfirmedOrderId = 4L;
        boolean expectedStatus = !orderRepository.getOne(notConfirmedOrderId).getStatus();

        this.mockMvc.perform(post("/orders/confirm")
                .param("id", Long.toString(notConfirmedOrderId)))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders"));

        assertEquals(expectedStatus, orderRepository.getOne(notConfirmedOrderId).getStatus());
    }

    @Test
    public void confirmOrder_tryNotAuthenticated_redirect3xxOrders() throws Exception {
        this.mockMvc.perform(post("/orders/confirm"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/auth/login"));
    }

    @Test
    @WithUserDetails("user1@test.com")
    public void deleteOrder_tryToDeleteExistedOrder_redirect3xxOrders() throws Exception {
        int expectedSize = orderRepository.findAll().size() - 1;

        this.mockMvc.perform(delete("/orders/" + USER_ORDER.getId() + "/delete"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders"));

        assertEquals(expectedSize, orderRepository.findAll().size());
        assertFalse(orderRepository.findAll().contains(USER_ORDER));
    }

    @Test
    public void deleteOrder_tryNotAuthenticated_redirect3xxOrders() throws Exception {
        this.mockMvc.perform(delete("/orders/2/delete"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/auth/login"));
    }
}