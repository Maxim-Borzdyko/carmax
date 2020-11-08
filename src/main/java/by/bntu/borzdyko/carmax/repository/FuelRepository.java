package by.bntu.borzdyko.carmax.repository;

import by.bntu.borzdyko.carmax.model.description.Fuel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FuelRepository extends JpaRepository<Fuel, Long> {
}
