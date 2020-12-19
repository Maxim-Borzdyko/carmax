package by.bntu.borzdyko.carmax.service;

import by.bntu.borzdyko.carmax.model.Car;
import by.bntu.borzdyko.carmax.model.description.Brand;
import by.bntu.borzdyko.carmax.repository.CarRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

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
        List<Car> expected = List.of(new Car(), new Car());

        when(carRepository.findAll()).thenReturn(expected);

        List<Car> actual = carService.findAll();

        assertArrayEquals(expected.toArray(), actual.toArray());
        verify(carRepository, times(1)).findAll();
        verifyNoMoreInteractions(carRepository);
    }

    @Test
    public void findAll_carsNotExistsInDatabase_emptyList() {
        List<Car> expected = List.of();

        when(carRepository.findAll()).thenReturn(expected);

        List<Car> actual = carService.findAll();

        assertArrayEquals(expected.toArray(), actual.toArray());
        verify(carRepository, times(1)).findAll();
        verifyNoMoreInteractions(carRepository);
    }

    @Test
    public void findAllByBrand_tryWithNormalBrand_cars() {
        Brand brand = Brand.builder().name("brand").build();
        List<Car> expected = List.of(new Car(), new Car());

        when(carRepository.findAllByBrand(brand)).thenReturn(expected);

        List<Car> actual = carService.findAllByBrand(brand);

        assertArrayEquals(expected.toArray(), actual.toArray());
        verify(carRepository, times(1)).findAllByBrand(brand);
        verifyNoMoreInteractions(carRepository);
    }

    @Test
    public void findAllByBrand_tryWithBrandIsNull_emptyList() {
        Brand brand = null;
        List<Car> expected = List.of();

        when(carRepository.findAllByBrand(brand)).thenReturn(expected);

        List<Car> actual = carService.findAllByBrand(brand);

        assertArrayEquals(expected.toArray(), actual.toArray());
        verify(carRepository, times(1)).findAllByBrand(brand);
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