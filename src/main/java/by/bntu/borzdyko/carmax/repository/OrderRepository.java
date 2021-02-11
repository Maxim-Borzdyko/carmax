package by.bntu.borzdyko.carmax.repository;

import by.bntu.borzdyko.carmax.model.Car;
import by.bntu.borzdyko.carmax.model.Order;
import by.bntu.borzdyko.carmax.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findAll(Pageable pageable);

    Page<Order> findAllByUser(User user, Pageable pageable);

    List<Order> findAllByStatus(boolean status);

    Optional<Order> findByCarAndUser(Car car, User user);
}
