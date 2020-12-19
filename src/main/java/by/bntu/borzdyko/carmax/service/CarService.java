package by.bntu.borzdyko.carmax.service;

import by.bntu.borzdyko.carmax.model.Car;
import by.bntu.borzdyko.carmax.model.description.Brand;
import by.bntu.borzdyko.carmax.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CarService {

    private final CarRepository carRepository;

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public Car findOne(Long id) {
        return carRepository.getOne(id);
    }

    public List<Car> findAll() {
        return carRepository.findAll();
    }

    public void save(Car car) {
        carRepository.save(car);
    }

    public void delete(Car car) {
        carRepository.delete(car);
    }

    public List<Car> findAllByBrand(Brand brand) {
        return carRepository.findAllByBrand(brand);
    }
}
