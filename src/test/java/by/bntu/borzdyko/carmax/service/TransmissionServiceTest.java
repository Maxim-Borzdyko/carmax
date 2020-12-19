package by.bntu.borzdyko.carmax.service;

import by.bntu.borzdyko.carmax.model.description.Transmission;
import by.bntu.borzdyko.carmax.repository.TransmissionRepository;
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
public class TransmissionServiceTest {

    @Mock
    TransmissionRepository transmissionRepository;

    @InjectMocks
    TransmissionService transmissionService;

    @Test
    public void findOne_checkWithIdInDatabase_transmission() {
        Long id = 1L;
        Transmission expected = Transmission.builder().id(id).build();

        when(transmissionRepository.getOne(id)).thenReturn(expected);

        Transmission actual = transmissionService.findOne(id);

        assertEquals(expected, actual);
        verify(transmissionRepository, times(1)).getOne(id);
        verifyNoMoreInteractions(transmissionRepository);
    }

    @Test
    public void findOne_checkWithIdNotInDatabase_null() {
        Long id = 10L;
        Transmission expected = null;

        Transmission actual = transmissionService.findOne(id);

        assertEquals(expected, actual);
        verify(transmissionRepository, times(1)).getOne(id);
        verifyNoMoreInteractions(transmissionRepository);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findOne_checkWithIdIsNull_IllegalArgumentException() {
        Long id = null;

        when(transmissionRepository.getOne(id)).thenThrow(new IllegalArgumentException());

        Transmission actual = transmissionService.findOne(id);
        verify(transmissionRepository, times(1)).getOne(id);
        verifyNoMoreInteractions(transmissionRepository);
    }

    @Test
    public void findAll_transmissionsExistsInDatabase_transmissions() {
        List<Transmission> expected = List.of(new Transmission(), new Transmission());

        when(transmissionRepository.findAll()).thenReturn(expected);

        List<Transmission> actual = transmissionService.findAll();

        assertArrayEquals(expected.toArray(), actual.toArray());
        verify(transmissionRepository, times(1)).findAll();
        verifyNoMoreInteractions(transmissionRepository);
    }

    @Test
    public void findAll_transmissionsNotExistsInDatabase_emptyList() {
        List<Transmission> expected = List.of();

        when(transmissionRepository.findAll()).thenReturn(expected);

        List<Transmission> actual = transmissionService.findAll();

        assertArrayEquals(expected.toArray(), actual.toArray());
        verify(transmissionRepository, times(1)).findAll();
        verifyNoMoreInteractions(transmissionRepository);
    }
}