package by.bntu.borzdyko.carmax.service;

import by.bntu.borzdyko.carmax.model.Car;
import by.bntu.borzdyko.carmax.model.description.Brand;
import by.bntu.borzdyko.carmax.repository.BrandRepository;
import by.bntu.borzdyko.carmax.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CarService {

    private static final String PRICE = "price";
    private static final String COLOR = "color";

    private final CarRepository carRepository;
    private final BrandRepository brandRepository;

    @Autowired
    public CarService(CarRepository carRepository, BrandRepository brandRepository) {
        this.carRepository = carRepository;
        this.brandRepository = brandRepository;
    }

    public List<Car> findAll() {
        return carRepository.findAll();
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

    // TODO not Tested if statement
    public boolean addCar(Car car) {
        boolean isSaved = false;

        if (carRepository.findCar(car).isEmpty()) {
            carRepository.save(car);
            isSaved = true;
        }

        return isSaved;
    }

    public void delete(Car car) {
        carRepository.delete(car);
    }

    public List<Car> findAllByBrand(Brand brand) {
        return carRepository.findAllByBrand(brand);
    }

    public List<Brand> findAllBrands() {
        return brandRepository.findAll();
    }

    public List<Car> sortByType(String sortType) {
        List<Car> cars = carRepository.findAll();

        switch (sortType) {
            case COLOR: {
                cars = cars.stream()
                        .sorted(Comparator.comparing(car -> car.getColor().getName()))
                        .collect(Collectors.toList());
                break;
            }
            case PRICE: {
                cars = cars.stream()
                        .sorted(Comparator.comparing(Car::getPrice))
                        .collect(Collectors.toList());
                break;
            }
        }

        return cars;
    }
}
