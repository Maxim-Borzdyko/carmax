package by.bntu.borzdyko.carmax.util;

import by.bntu.borzdyko.carmax.model.Car;
import by.bntu.borzdyko.carmax.model.description.Brand;
import by.bntu.borzdyko.carmax.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CarFilter {

    private static final String COLOR = "color";
    private static final String PRICE = "price";
    private static final String MILEAGE = "mileage";
    private static final String YEAR = "year";

    private final CarService carService;

    @Autowired
    public CarFilter(CarService carService) {
        this.carService = carService;
    }

    public Page<Car> filter(int page, Brand brand) {
        Page<Car> cars;

        if (brand != null) {
            cars = carService.findAllByBrand(page, brand);
        } else {
            cars = carService.findAll(page);
        }

        return cars;
    }

    public List<Car> sort(List<Car> cars, String sort) {
        if (sort != null) {
            switch (sort) {
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
                case  MILEAGE: {
                    cars = cars.stream()
                            .sorted(Comparator.comparing(Car::getMileage))
                            .collect(Collectors.toList());
                    break;
                }
                case  YEAR: {
                    cars = cars.stream()
                            .sorted(Comparator.comparing(Car::getYearOfIssue))
                            .collect(Collectors.toList());
                    break;
                }
            }
        }

        return cars;
    }
}
