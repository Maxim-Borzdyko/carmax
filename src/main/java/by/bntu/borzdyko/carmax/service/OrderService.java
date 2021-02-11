package by.bntu.borzdyko.carmax.service;

import by.bntu.borzdyko.carmax.model.Order;
import by.bntu.borzdyko.carmax.model.User;
import by.bntu.borzdyko.carmax.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrderService {

    private static final boolean ACTIVE = true;
    private static final int AMOUNT_ON_PAGE = 6;

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order findOne(Long id) {
        return orderRepository.getOne(id);
    }

    public Page<Order> findAll(int page) {
        Pageable pageable = PageRequest.of(page, AMOUNT_ON_PAGE);
        return orderRepository.findAll(pageable);
    }

    public void save(Order order) {
        orderRepository.save(order);
    }

    public void delete(Order order) {
        orderRepository.delete(order);
    }

    public List<Order> findActiveOrders() {
        return orderRepository.findAllByStatus(ACTIVE);
    }

    public Page<Order> findUserOrders(int page, User user) {
        Pageable pageable = PageRequest.of(page, AMOUNT_ON_PAGE);
        return orderRepository.findAllByUser(user, pageable);
    }

    public boolean isPresent(Order order) {
        return orderRepository.findByCarAndUser(order.getCar(), order.getUser()).isPresent();
    }
}
