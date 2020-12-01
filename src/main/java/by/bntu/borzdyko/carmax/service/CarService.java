package by.bntu.borzdyko.carmax.service;

import by.bntu.borzdyko.carmax.model.Car;
import by.bntu.borzdyko.carmax.model.description.Brand;
import by.bntu.borzdyko.carmax.repository.BrandRepository;
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

    public void save(Car car) {
        carRepository.save(car);
    }

    public void delete(Car car) {
        carRepository.delete(car);
    }

    public List<Car> findAll() {
        return carRepository.findAll();
    }

    // TODO make check for exist
    public boolean addCar(Car car) {
        boolean isSaved = false;
        carRepository.save(car);
        return isSaved;
    }

    // TODO update car
    public Car updateCar(Car car, Car newCar) {
        // ???
        newCar.setId(car.getId());
        // ----
        boolean isChanged = !car.equals(newCar);

        if (isChanged) {
            carRepository.save(newCar);
        }

        return newCar;
    }

    public List<Car> findAllByBrand(Brand brand) {
        return carRepository.findAllByBrand(brand);
    }
}
