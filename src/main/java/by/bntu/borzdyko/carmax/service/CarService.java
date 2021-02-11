package by.bntu.borzdyko.carmax.service;

import by.bntu.borzdyko.carmax.model.Car;
import by.bntu.borzdyko.carmax.model.description.Brand;
import by.bntu.borzdyko.carmax.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CarService {

    private static final int AMOUNT_ON_PAGE = 9;

    private final CarRepository carRepository;

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public Car findOne(Long id) {
        return carRepository.getOne(id);
    }

    public Page<Car> findAll(int page) {
        Pageable pageable = PageRequest.of(page, AMOUNT_ON_PAGE);
        return carRepository.findAll(pageable);
    }

    public void save(Car car) {
        carRepository.save(car);
    }

    public void delete(Car car) {
        carRepository.delete(car);
    }

    public Page<Car> findAllByBrand(int page, Brand brand) {
        Pageable pageable = PageRequest.of(page, AMOUNT_ON_PAGE);
        return carRepository.findAllByBrand(brand, pageable);
    }
}
