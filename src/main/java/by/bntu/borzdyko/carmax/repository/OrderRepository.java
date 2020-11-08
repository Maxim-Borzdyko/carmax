package by.bntu.borzdyko.carmax.repository;

import by.bntu.borzdyko.carmax.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.lang.management.OperatingSystemMXBean;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByStatus(boolean status);

    Optional<Order> findById(Long id);
}
