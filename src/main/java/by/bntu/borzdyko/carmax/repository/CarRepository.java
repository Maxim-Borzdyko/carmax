package by.bntu.borzdyko.carmax.repository;

import by.bntu.borzdyko.carmax.model.Car;
import by.bntu.borzdyko.carmax.model.description.Brand;
import by.bntu.borzdyko.carmax.model.description.Color;
import by.bntu.borzdyko.carmax.model.description.Fuel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findAllByBrand(Brand brand);

    Optional<Car> findById(Long id);

    Optional<Car> findCar(Car car);
}
