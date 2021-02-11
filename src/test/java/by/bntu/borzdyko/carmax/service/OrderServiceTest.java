package by.bntu.borzdyko.carmax.service;

import by.bntu.borzdyko.carmax.model.Car;
import by.bntu.borzdyko.carmax.model.Order;
import by.bntu.borzdyko.carmax.model.User;
import by.bntu.borzdyko.carmax.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

    private static final boolean ACTIVE = true;
    private final static List<Order> ORDERS = List.of(new Order(), new Order());

    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    OrderService orderService;

    private static final int PAGE = 0;
    private static final Pageable PAGEABLE = PageRequest.of(PAGE, 6);

    @Test
    public void findOne_checkWithIdInDatabase_order() {
        Long id = 1L;
        Order expected = Order.builder().id(id).build();

        when(orderRepository.getOne(id)).thenReturn(expected);

        Order actual = orderService.findOne(id);

        assertEquals(expected, actual);
        verify(orderRepository, times(1)).getOne(id);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    public void findOne_checkWithIdNotInDatabase_null() {
        Long id = 10L;
        Order expected = null;

        Order actual = orderService.findOne(id);

        assertEquals(expected, actual);
        verify(orderRepository, times(1)).getOne(id);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findOne_checkWithIdIsNull_IllegalArgumentException() {
        Long id = null;

        when(orderRepository.getOne(id)).thenThrow(new IllegalArgumentException());

        Order actual = orderService.findOne(id);
        verify(orderRepository, times(1)).getOne(id);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    public void findAll_ordersExistsInDatabase_orders() {
        List<Order> orders = List.of(new Order(), new Order());

        Page<Order> expected = new PageImpl<>(orders);

        when(orderRepository.findAll(PAGEABLE)).thenReturn(expected);

        Page<Order> actual = orderService.findAll(PAGE);

        assertArrayEquals(expected.toList().toArray(), actual.toList().toArray());
        verify(orderRepository, times(1)).findAll(PAGEABLE);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    public void findAll_ordersNotExistsInDatabase_emptyList() {
        List<Order> orders = List.of();

        Page<Order> expected = new PageImpl<>(orders);

        when(orderRepository.findAll(PAGEABLE)).thenReturn(expected);

        Page<Order> actual = orderService.findAll(PAGE);

        assertArrayEquals(expected.toList().toArray(), actual.toList().toArray());
        verify(orderRepository, times(1)).findAll(PAGEABLE);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    public void findActiveOrders_tryToTakeActiveOrdersFromOrders_activeOrders() {
        // arrange
        List<Order> expected = List.copyOf(ORDERS);

        when(orderRepository.findAllByStatus(ACTIVE)).thenReturn(ORDERS);

        // action
        List<Order> actual = orderService.findActiveOrders();

        // assert
        assertArrayEquals(expected.toArray(), actual.toArray());
        verify(orderRepository, times(1)).findAllByStatus(ACTIVE);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    public void findActiveOrders_tryToTakeActiveOrdersWhenThereAreNoSuchOrders_emptyOrderList() {
        // arrange
        List<Order> expected = List.of();

        when(orderRepository.findAllByStatus(ACTIVE)).thenReturn(List.of());

        // action
        List<Order> actual = orderService.findActiveOrders();

        // assert
        assertArrayEquals(expected.toArray(), actual.toArray());
        verify(orderRepository, times(1)).findAllByStatus(ACTIVE);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    public void findUserOrders_tryWithUserWithOrders_userOrdersList() {
        // arrange
        Page<Order> expected = new PageImpl<>(ORDERS);
        User user = new User();

        when(orderRepository.findAllByUser(user, PAGEABLE)).thenReturn(expected);

        // action
        Page<Order> actual = orderService.findUserOrders(PAGE, user);

        // assert
        assertArrayEquals(expected.toList().toArray(), actual.toList().toArray());
        verify(orderRepository, times(1)).findAllByUser(user, PAGEABLE);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    public void findUserOrders_tryWithUserWithoutOrders_emptyList() {
        // arrange
        Page<Order> expected = new PageImpl<>(List.of());
        User user = new User();

        when(orderRepository.findAllByUser(user, PAGEABLE)).thenReturn(expected);

        // action
        Page<Order> actual = orderService.findUserOrders(PAGE, user);

        // assert
        assertArrayEquals(expected.toList().toArray(), actual.toList().toArray());
        verify(orderRepository, times(1)).findAllByUser(user, PAGEABLE);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    public void findUserOrders_tryWithUserNull_emptyList() {
        // arrange
        Page<Order> expected = new PageImpl<>(List.of());

        when(orderRepository.findAllByUser(null, PAGEABLE)).thenReturn(expected);

        // action
        Page<Order> actual = orderService.findUserOrders(PAGE,null);

        // assert
        assertArrayEquals(expected.toList().toArray(), actual.toList().toArray());
        verify(orderRepository, times(1)).findAllByUser(null, PAGEABLE);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    public void isPresent_withOrderWhichExists_true() {
        User user = new User();
        Car car = new Car();
        Order order = Order.builder().user(user).car(car).build();

        when(orderRepository.findByCarAndUser(car, user)).thenReturn(Optional.of(order));

        boolean actual = orderService.isPresent(order);

        assertTrue(actual);
        verify(orderRepository, times(1)).findByCarAndUser(car, user);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    public void isPresent_withOrderWhichIsNotExists_false() {
        User user = new User();
        Car car = new Car();
        Order order = Order.builder().user(user).car(car).build();

        when(orderRepository.findByCarAndUser(car, user)).thenReturn(Optional.empty());

        boolean actual = orderService.isPresent(order);

        assertFalse(actual);
        verify(orderRepository, times(1)).findByCarAndUser(car, user);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test(expected = NullPointerException.class)
    public void isPresent_withOrderNull_NullPointerException() {
        Order order = null;
        boolean actual = orderService.isPresent(order);
    }

    @Test
    public void save_checkWithNormalOrder_saved() {
        Order order = new Order();

        orderService.save(order);

        verify(orderRepository, times(1)).save(order);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test(expected = IllegalArgumentException.class)
    public void save_checkWithOrderIsNull_IllegalArgumentException() {
        Order order = null;

        when(orderRepository.save(order)).thenThrow(new IllegalArgumentException());

        orderService.save(order);

        verify(orderRepository, times(1)).save(order);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    public void delete_checkWithCorrectUser_deleted() {
        Order order = new Order();

        orderService.delete(order);

        verify(orderRepository, times(1)).delete(order);
        verifyNoMoreInteractions(orderRepository);
    }
}