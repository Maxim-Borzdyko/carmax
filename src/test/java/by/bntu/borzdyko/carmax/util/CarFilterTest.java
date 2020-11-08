package by.bntu.borzdyko.carmax.util;

import by.bntu.borzdyko.carmax.model.Car;
import by.bntu.borzdyko.carmax.model.description.Brand;
import by.bntu.borzdyko.carmax.service.CarService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CarFilterTest {

    @Mock
    CarService carService;

    @InjectMocks
    CarFilter carFilter;

    private static final String BRAND_NAME = "brand";

    private Brand brand;
    private List<Car> cars;

    @Before
    public void setUp() {
        brand = null;
        cars = List.of(new Car(), new Car(), new Car());
    }

    @Test
    public void filterByBrand_findAllCarsByBrand_carsWithBrand() {
        brand = new Brand();
        brand.setName(BRAND_NAME);
        cars.get(0).setBrand(brand);
        cars.get(1).setBrand(brand);
        List<Car> expected = cars.subList(0, 1);

        when(carService.findAllByBrand(brand)).thenReturn(cars.subList(0, 1));

        List<Car> actual = carFilter.filterByBrand(brand);

        assertArrayEquals(expected.toArray(), actual.toArray());
        verify(carService, Mockito.times(1)).findAllByBrand(brand);
        verify(carService, Mockito.times(0)).findAll();
        verifyNoMoreInteractions(carService);
    }

    @Test
    public void filterByBrand_findAllCarsByBrandNotInDatabase_allCars() {
        List<Car> expected = List.copyOf(cars);

        when(carService.findAll()).thenReturn(cars);

        List<Car> actual = carFilter.filterByBrand(brand);

        assertArrayEquals(expected.toArray(), actual.toArray());
        verify(carService, Mockito.times(0)).findAllByBrand(brand);
        verify(carService, Mockito.times(1)).findAll();
        verifyNoMoreInteractions(carService);
    }
}