package by.bntu.borzdyko.carmax.service;

import by.bntu.borzdyko.carmax.model.description.Country;
import by.bntu.borzdyko.carmax.repository.CountryRepository;
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
public class CountryServiceTest {

    @Mock
    CountryRepository countryRepository;

    @InjectMocks
    CountryService countryService;

    @Test
    public void findOne_checkWithIdInDatabase_country() {
        Long id = 1L;
        Country expected = Country.builder().id(id).build();

        when(countryRepository.getOne(id)).thenReturn(expected);

        Country actual = countryService.findOne(id);

        assertEquals(expected, actual);
        verify(countryRepository, times(1)).getOne(id);
        verifyNoMoreInteractions(countryRepository);
    }

    @Test
    public void findOne_checkWithIdNotInDatabase_null() {
        Long id = 10L;
        Country expected = null;

        Country actual = countryService.findOne(id);

        assertEquals(expected, actual);
        verify(countryRepository, times(1)).getOne(id);
        verifyNoMoreInteractions(countryRepository);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findOne_checkWithIdIsNull_IllegalArgumentException() {
        Long id = null;

        when(countryRepository.getOne(id)).thenThrow(new IllegalArgumentException());

        Country actual = countryService.findOne(id);
        verify(countryRepository, times(1)).getOne(id);
        verifyNoMoreInteractions(countryRepository);
    }

    @Test
    public void findAll_countriesExistsInDatabase_countries() {
        List<Country> expected = List.of(new Country(), new Country());

        when(countryRepository.findAll()).thenReturn(expected);

        List<Country> actual = countryService.findAll();

        assertArrayEquals(expected.toArray(), actual.toArray());
        verify(countryRepository, times(1)).findAll();
        verifyNoMoreInteractions(countryRepository);
    }

    @Test
    public void findAll_countriesNotExistsInDatabase_emptyList() {
        List<Country> expected = List.of();

        when(countryRepository.findAll()).thenReturn(expected);

        List<Country> actual = countryService.findAll();

        assertArrayEquals(expected.toArray(), actual.toArray());
        verify(countryRepository, times(1)).findAll();
        verifyNoMoreInteractions(countryRepository);
    }
}