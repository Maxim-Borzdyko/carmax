package by.bntu.borzdyko.carmax.service;

import by.bntu.borzdyko.carmax.model.description.Model;
import by.bntu.borzdyko.carmax.repository.ModelRepository;
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
public class ModelServiceTest {

    @Mock
    ModelRepository modelRepository;

    @InjectMocks
    ModelService modelService;

    @Test
    public void findOne_checkWithIdInDatabase_model() {
        Long id = 1L;
        Model expected = Model.builder().id(id).build();

        when(modelRepository.getOne(id)).thenReturn(expected);

        Model actual = modelService.findOne(id);

        assertEquals(expected, actual);
        verify(modelRepository, times(1)).getOne(id);
        verifyNoMoreInteractions(modelRepository);
    }

    @Test
    public void findOne_checkWithIdNotInDatabase_null() {
        Long id = 10L;
        Model expected = null;

        Model actual = modelService.findOne(id);

        assertEquals(expected, actual);
        verify(modelRepository, times(1)).getOne(id);
        verifyNoMoreInteractions(modelRepository);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findOne_checkWithIdIsNull_IllegalArgumentException() {
        Long id = null;

        when(modelRepository.getOne(id)).thenThrow(new IllegalArgumentException());

        Model actual = modelService.findOne(id);
        verify(modelRepository, times(1)).getOne(id);
        verifyNoMoreInteractions(modelRepository);
    }

    @Test
    public void findAll_modelsExistsInDatabase_models() {
        List<Model> expected = List.of(new Model(), new Model());

        when(modelRepository.findAll()).thenReturn(expected);

        List<Model> actual = modelService.findAll();

        assertArrayEquals(expected.toArray(), actual.toArray());
        verify(modelRepository, times(1)).findAll();
        verifyNoMoreInteractions(modelRepository);
    }

    @Test
    public void findAll_modelsNotExistsInDatabase_emptyList() {
        List<Model> expected = List.of();

        when(modelRepository.findAll()).thenReturn(expected);

        List<Model> actual = modelService.findAll();

        assertArrayEquals(expected.toArray(), actual.toArray());
        verify(modelRepository, times(1)).findAll();
        verifyNoMoreInteractions(modelRepository);
    }
}