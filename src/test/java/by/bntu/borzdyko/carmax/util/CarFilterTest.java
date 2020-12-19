package by.bntu.borzdyko.carmax.util;

import by.bntu.borzdyko.carmax.model.Car;
import by.bntu.borzdyko.carmax.model.description.Brand;
import by.bntu.borzdyko.carmax.model.description.Color;
import by.bntu.borzdyko.carmax.service.CarService;
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
public class CarFilterTest {

    @Mock
    CarService carService;

    @InjectMocks
    CarFilter carFilter;

    @Test
    public void filter_tryWithBrand_filteredCars() {
        Brand brand = Brand.builder().name("FIRST").build();

        List<Car> expected = List.of(
                Car.builder().brand(Brand.builder().name("FIRST").build()).price(new BigDecimal("100")).build(),
                Car.builder().brand(Brand.builder().name("FIRST").build()).price(new BigDecimal("200")).build()
        );

        when(carService.findAllByBrand(brand)).thenReturn(expected);

        List<Car> actual = carFilter.filter(brand);

        assertArrayEquals(expected.toArray(), actual.toArray());
        verify(carService, times(1)).findAllByBrand(brand);
        verifyNoMoreInteractions(carService);
    }

    @Test
    public void filter_tryWithBrandIsNull_cars() {
        Brand brand = null;
        List<Car> expected = List.of(
                Car.builder().brand(Brand.builder().name("FIRST").build()).price(new BigDecimal("200")).build(),
                Car.builder().brand(Brand.builder().name("SECOND").build()).price(new BigDecimal("200")).build(),
                Car.builder().brand(Brand.builder().name("FIRST").build()).price(new BigDecimal("100")).build()
        );

        when(carService.findAll()).thenReturn(expected);

        List<Car> actual = carFilter.filter(brand);

        assertArrayEquals(expected.toArray(), actual.toArray());
        verify(carService, times(1)).findAll();
        verifyNoMoreInteractions(carService);
    }

    @Test
    public void sort_tryToSortWithPrice_sortedCars() {
        String sortBy = "price";
        List<Car> cars = List.of(
                Car.builder().price(new BigDecimal("150")).build(),
                Car.builder().price(new BigDecimal("200")).build(),
                Car.builder().price(new BigDecimal("100")).build()
        );

        List<Car> expected = List.of(
                Car.builder().price(new BigDecimal("100")).build(),
                Car.builder().price(new BigDecimal("150")).build(),
                Car.builder().price(new BigDecimal("200")).build()
        );

        List<Car> actual = carFilter.sort(cars, sortBy);

        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    public void sort_tryToSortWithColor_sortedCars() {
        String sortBy = "color";
        List<Car> cars = List.of(
                Car.builder().color(Color.builder().name("White").build()).build(),
                Car.builder().color(Color.builder().name("Black").build()).build(),
                Car.builder().color(Color.builder().name("Orange").build()).build()
        );

        List<Car> expected = List.of(
                Car.builder().color(Color.builder().name("Black").build()).build(),
                Car.builder().color(Color.builder().name("Orange").build()).build(),
                Car.builder().color(Color.builder().name("White").build()).build()
        );

        List<Car> actual = carFilter.sort(cars, sortBy);

        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    public void sort_tryToSortWithMileage_sortedCars() {
        String sortBy = "mileage";
        List<Car> cars = List.of(
                Car.builder().mileage(300D).build(),
                Car.builder().mileage(100D).build(),
                Car.builder().mileage(150D).build()
        );

        List<Car> expected = List.of(
                Car.builder().mileage(100D).build(),
                Car.builder().mileage(150D).build(),
                Car.builder().mileage(300D).build()
        );

        List<Car> actual = carFilter.sort(cars, sortBy);

        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    public void sort_tryToSortWithYear_sortedCars() {
        String sortBy = "year";
        List<Car> cars = List.of(
                Car.builder().yearOfIssue(2015).build(),
                Car.builder().yearOfIssue(2000).build(),
                Car.builder().yearOfIssue(2005).build()
        );

        List<Car> expected = List.of(
                Car.builder().yearOfIssue(2000).build(),
                Car.builder().yearOfIssue(2005).build(),
                Car.builder().yearOfIssue(2015).build()
        );

        List<Car> actual = carFilter.sort(cars, sortBy);

        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    public void sort_tryToSortWithOtherSortType_sortedCars() {
        String sortBy = "other_or_null";
        List<Car> expected = List.of(new Car(), new Car(), new Car());

        List<Car> actual = carFilter.sort(expected, sortBy);

        assertArrayEquals(expected.toArray(), actual.toArray());
    }
}