package by.bntu.borzdyko.carmax.service;

import by.bntu.borzdyko.carmax.model.description.Fuel;
import by.bntu.borzdyko.carmax.repository.FuelRepository;
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
public class FuelServiceTest {

    @Mock
    FuelRepository fuelRepository;

    @InjectMocks
    FuelService fuelService;

    @Test
    public void findOne_checkWithIdInDatabase_fuel() {
        Long id = 1L;
        Fuel expected = Fuel.builder().id(id).build();

        when(fuelRepository.getOne(id)).thenReturn(expected);

        Fuel actual = fuelService.findOne(id);

        assertEquals(expected, actual);
        verify(fuelRepository, times(1)).getOne(id);
        verifyNoMoreInteractions(fuelRepository);
    }

    @Test
    public void findOne_checkWithIdNotInDatabase_null() {
        Long id = 10L;
        Fuel expected = null;

        Fuel actual = fuelService.findOne(id);

        assertEquals(expected, actual);
        verify(fuelRepository, times(1)).getOne(id);
        verifyNoMoreInteractions(fuelRepository);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findOne_checkWithIdIsNull_IllegalArgumentException() {
        Long id = null;

        when(fuelRepository.getOne(id)).thenThrow(new IllegalArgumentException());

        Fuel actual = fuelService.findOne(id);
        verify(fuelRepository, times(1)).getOne(id);
        verifyNoMoreInteractions(fuelRepository);
    }

    @Test
    public void findAll_fuelsExistsInDatabase_fuels() {
        List<Fuel> expected = List.of(new Fuel(), new Fuel());

        when(fuelRepository.findAll()).thenReturn(expected);

        List<Fuel> actual = fuelService.findAll();

        assertArrayEquals(expected.toArray(), actual.toArray());
        verify(fuelRepository, times(1)).findAll();
        verifyNoMoreInteractions(fuelRepository);
    }

    @Test
    public void findAll_fuelsNotExistsInDatabase_emptyList() {
        List<Fuel> expected = List.of();

        when(fuelRepository.findAll()).thenReturn(expected);

        List<Fuel> actual = fuelService.findAll();

        assertArrayEquals(expected.toArray(), actual.toArray());
        verify(fuelRepository, times(1)).findAll();
        verifyNoMoreInteractions(fuelRepository);
    }
}