package by.bntu.borzdyko.carmax.service;

import by.bntu.borzdyko.carmax.model.Order;
import by.bntu.borzdyko.carmax.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

    private static final boolean ACTIVE = true;
    private final static List<Order> ORDERS = List.of(new Order(), new Order());

    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    OrderService orderService;

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
}