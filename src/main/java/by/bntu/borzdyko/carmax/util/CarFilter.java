package by.bntu.borzdyko.carmax.util;

import by.bntu.borzdyko.carmax.model.Car;
import by.bntu.borzdyko.carmax.model.description.Brand;
import by.bntu.borzdyko.carmax.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.List;

@Component
public class CarFilter {

    private final CarService carService;

    @Autowired
    public CarFilter(CarService carService) {
        this.carService = carService;
    }

    public List<Car> filterByBrand(Brand brand) {
        List<Car> cars;

        if (brand != null) {
            cars = carService.findAllByBrand(brand);
        } else {
            cars = carService.findAll();
        }

        return cars;
    }
}
