package by.bntu.borzdyko.carmax.service;

import by.bntu.borzdyko.carmax.model.Car;
import by.bntu.borzdyko.carmax.model.description.Color;
import by.bntu.borzdyko.carmax.repository.CarRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CarServiceTest {

    private static final String COLOR = "color";
    private static final String PRICE = "price";
    private static final String DEFAULT = "default";

    private static final int FIRST_CAR = 0;
    private static final int SECOND_CAR = 1;
    private static final int THIRD_CAR = 2;

    @Mock
    CarRepository carRepository;

    @InjectMocks
    CarService carService;

    private List<Car> cars;

    @Before
    public void setUp() {
        cars = List.of(new Car(), new Car(), new Car());
    }

    @Test
    public void sortByType_checkSortedListOfCarsForCorrectSortWithColor_sortedListByColor() {
        cars.get(FIRST_CAR).setColor(new Color(null, "Red", null));
        cars.get(SECOND_CAR).setColor(new Color(null, "White", null));
        cars.get(THIRD_CAR).setColor(new Color(null, "Red", null));

        List<Car> expected = List.of(cars.get(FIRST_CAR), cars.get(THIRD_CAR), cars.get(SECOND_CAR));

        when(carRepository.findAll()).thenReturn(cars);

        List<Car> actual = carService.sortByType(COLOR);

        assertArrayEquals(expected.toArray(), actual.toArray());
        verify(carRepository, times(1)).findAll();
        verifyNoMoreInteractions(carRepository);
    }

    @Test
    public void sortByType_checkSortedListOfCarsForCorrectSortWithPrice_sortedListByPrice() {
        cars.get(FIRST_CAR).setPrice(new BigDecimal("199.9"));
        cars.get(SECOND_CAR).setPrice(new BigDecimal("549.9"));
        cars.get(THIRD_CAR).setPrice(new BigDecimal("229.9"));

        List<Car> expected = List.of(cars.get(FIRST_CAR), cars.get(THIRD_CAR), cars.get(SECOND_CAR));

        when(carRepository.findAll()).thenReturn(cars);

        List<Car> actual = carService.sortByType(PRICE);

        assertArrayEquals(expected.toArray(), actual.toArray());
        verify(carRepository, times(1)).findAll();
        verifyNoMoreInteractions(carRepository);
    }

    @Test
    public void sortByType_checkReturnWhenSortTypeIsNotInSwitchCases_defaultCarList() {
        List<Car> expected = List.copyOf(cars);

        when(carRepository.findAll()).thenReturn(cars);

        List<Car> actual = carService.sortByType(DEFAULT);

        assertArrayEquals(expected.toArray(), actual.toArray());
        verify(carRepository, times(1)).findAll();
        verifyNoMoreInteractions(carRepository);
    }
}