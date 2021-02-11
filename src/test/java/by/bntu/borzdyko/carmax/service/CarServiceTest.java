package by.bntu.borzdyko.carmax.service;

import by.bntu.borzdyko.carmax.model.Car;
import by.bntu.borzdyko.carmax.model.description.Brand;
import by.bntu.borzdyko.carmax.repository.CarRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CarServiceTest {

    @Mock
    CarRepository carRepository;

    @InjectMocks
    CarService carService;

    private static final int PAGE = 0;
    private static final Pageable PAGEABLE = PageRequest.of(PAGE, 9);

    @Test
    public void findOne_checkWithIdInDatabase_car() {
        Long id = 1L;
        Car expected = Car.builder().id(id).build();

        when(carRepository.getOne(id)).thenReturn(expected);

        Car actual = carService.findOne(id);

        assertEquals(expected, actual);
        verify(carRepository, times(1)).getOne(id);
        verifyNoMoreInteractions(carRepository);
    }

    @Test
    public void findOne_checkWithIdNotInDatabase_null() {
        Long id = 10L;
        Car expected = null;

        Car actual = carService.findOne(id);

        assertEquals(expected, actual);
        verify(carRepository, times(1)).getOne(id);
        verifyNoMoreInteractions(carRepository);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findOne_checkWithIdIsNull_IllegalArgumentException() {
        Long id = null;

        when(carRepository.getOne(id)).thenThrow(new IllegalArgumentException());

        Car actual = carService.findOne(id);
        verify(carRepository, times(1)).getOne(id);
        verifyNoMoreInteractions(carRepository);
    }

    @Test
    public void findAll_carsExistsInDatabase_cars() {
        List<Car> cars = List.of(new Car(), new Car());

        Page<Car> expected = new PageImpl<>(cars);

        when(carRepository.findAll(PAGEABLE)).thenReturn(expected);

        Page<Car> actual = carService.findAll(PAGE);

        assertArrayEquals(expected.toList().toArray(), actual.toList().toArray());
        verify(carRepository, times(1)).findAll(PAGEABLE);
        verifyNoMoreInteractions(carRepository);
    }

    @Test
    public void findAll_carsNotExistsInDatabase_emptyList() {
        List<Car> cars = List.of();

        Page<Car> expected = new PageImpl<>(cars);

        when(carRepository.findAll(PAGEABLE)).thenReturn(expected);

        Page<Car> actual = carService.findAll(PAGE);

        assertArrayEquals(expected.toList().toArray(), actual.toList().toArray());
        verify(carRepository, times(1)).findAll(PAGEABLE);
        verifyNoMoreInteractions(carRepository);
    }

    @Test
    public void findAllByBrand_tryWithNormalBrand_cars() {
        Brand brand = Brand.builder().name("brand").build();
        List<Car> cars = List.of(new Car(), new Car());

        Page<Car> expected = new PageImpl<>(cars);

        when(carRepository.findAllByBrand(brand, PAGEABLE)).thenReturn(expected);

        Page<Car> actual = carService.findAllByBrand(PAGE, brand);

        assertArrayEquals(expected.toList().toArray(), actual.toList().toArray());
        verify(carRepository, times(1)).findAllByBrand(brand, PAGEABLE);
        verifyNoMoreInteractions(carRepository);
    }

    @Test
    public void findAllByBrand_tryWithBrandIsNull_emptyList() {
        Brand brand = null;
        List<Car> cars = List.of();

        Page<Car> expected = new PageImpl<>(cars);

        when(carRepository.findAllByBrand(brand, PAGEABLE)).thenReturn(expected);

        Page<Car> actual = carService.findAllByBrand(PAGE, brand);

        assertArrayEquals(expected.toList().toArray(), actual.toList().toArray());
        verify(carRepository, times(1)).findAllByBrand(brand, PAGEABLE);
        verifyNoMoreInteractions(carRepository);
    }

    @Test
    public void save_checkWithNormalOrder_saved() {
        Car car = new Car();

        carService.save(car);

        verify(carRepository, times(1)).save(car);
        verifyNoMoreInteractions(carRepository);
    }

    @Test(expected = IllegalArgumentException.class)
    public void save_checkWithOrderIsNull_IllegalArgumentException() {
        Car car = null;

        when(carRepository.save(car)).thenThrow(new IllegalArgumentException());

        carService.save(car);

        verify(carRepository, times(1)).save(car);
        verifyNoMoreInteractions(carRepository);
    }

    @Test
    public void delete_checkWithCorrectUser_deleted() {
        Car car = new Car();

        carService.delete(car);

        verify(carRepository, times(1)).delete(car);
        verifyNoMoreInteractions(carRepository);
    }
}