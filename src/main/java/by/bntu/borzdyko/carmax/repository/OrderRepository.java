package by.bntu.borzdyko.carmax.repository;

import by.bntu.borzdyko.carmax.model.Car;
import by.bntu.borzdyko.carmax.model.Order;
import by.bntu.borzdyko.carmax.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByStatus(boolean status);

    List<Order> findAllByUser(User user);

    Optional<Order> findByCarAndUser(Car car, User user);
}
