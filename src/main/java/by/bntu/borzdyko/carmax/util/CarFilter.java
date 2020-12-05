package by.bntu.borzdyko.carmax.util;

import by.bntu.borzdyko.carmax.model.Car;
import by.bntu.borzdyko.carmax.model.description.Brand;
import by.bntu.borzdyko.carmax.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CarFilter {

    private static final String COLOR = "color";
    private static final String PRICE = "price";

    private final CarService carService;

    @Autowired
    public CarFilter(CarService carService) {
        this.carService = carService;
    }

    public List<Car> filter(Brand brand) {
        List<Car> cars;

        if (brand != null) {
            cars = carService.findAllByBrand(brand);
        } else {
            cars = carService.findAll();
        }

        return cars;
    }

    public List<Car> sort(List<Car> cars, String sort) {
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
        }

        return cars;
    }
}
