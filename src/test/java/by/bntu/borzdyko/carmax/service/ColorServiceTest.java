package by.bntu.borzdyko.carmax.service;

import by.bntu.borzdyko.carmax.model.description.Color;
import by.bntu.borzdyko.carmax.repository.ColorRepository;
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
public class ColorServiceTest {

    @Mock
    ColorRepository colorRepository;

    @InjectMocks
    ColorService colorService;

    @Test
    public void findOne_checkWithIdInDatabase_color() {
        Long id = 1L;
        Color expected = Color.builder().id(id).build();

        when(colorRepository.getOne(id)).thenReturn(expected);

        Color actual = colorService.findOne(id);

        assertEquals(expected, actual);
        verify(colorRepository, times(1)).getOne(id);
        verifyNoMoreInteractions(colorRepository);
    }

    @Test
    public void findOne_checkWithIdNotInDatabase_null() {
        Long id = 10L;
        Color expected = null;

        Color actual = colorService.findOne(id);

        assertEquals(expected, actual);
        verify(colorRepository, times(1)).getOne(id);
        verifyNoMoreInteractions(colorRepository);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findOne_checkWithIdIsNull_IllegalArgumentException() {
        Long id = null;

        when(colorRepository.getOne(id)).thenThrow(new IllegalArgumentException());

        Color actual = colorService.findOne(id);
        verify(colorRepository, times(1)).getOne(id);
        verifyNoMoreInteractions(colorRepository);
    }

    @Test
    public void findAll_colorsExistsInDatabase_colors() {
        List<Color> expected = List.of(new Color(), new Color());

        when(colorRepository.findAll()).thenReturn(expected);

        List<Color> actual = colorService.findAll();

        assertArrayEquals(expected.toArray(), actual.toArray());
        verify(colorRepository, times(1)).findAll();
        verifyNoMoreInteractions(colorRepository);
    }

    @Test
    public void findAll_colorsNotExistsInDatabase_emptyList() {
        List<Color> expected = List.of();

        when(colorRepository.findAll()).thenReturn(expected);

        List<Color> actual = colorService.findAll();

        assertArrayEquals(expected.toArray(), actual.toArray());
        verify(colorRepository, times(1)).findAll();
        verifyNoMoreInteractions(colorRepository);
    }
}