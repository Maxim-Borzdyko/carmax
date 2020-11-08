package by.bntu.borzdyko.carmax.service;

import by.bntu.borzdyko.carmax.model.User;
import by.bntu.borzdyko.carmax.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    private static final String TEST_EMAIL = "testing@mail.com";
    private static final String PASSWORD = "password";

    private static final String ACTUAL_EMAIL = "actual@mail.com";
    private static final String ACTUAL_FIRST_NAME = "actualName";
    private static final String ACTUAL_SECOND_NAME = "actual";

    private static final String UPDATE_EMAIL = "update@mail.com";
    private static final String UPDATE_FIRST_NAME = "updateName";
    private static final String UPDATE_SECOND_NAME = "update";

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    public void addUser_tryToSaveUser_true() {
        User user = new User();
        user.setEmail(TEST_EMAIL);
        user.setPassword(PASSWORD);

        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());

        boolean isSaved = userService.addUser(user);

        assertTrue(isSaved);
        verify(userRepository, times(1)).findByEmail(TEST_EMAIL);
        verify(passwordEncoder, times(1)).encode(PASSWORD);
        verify(userRepository, times(1)).save(user);
        verifyNoMoreInteractions(userRepository, passwordEncoder);
    }

    @Test
    public void addUser_tryToSaveUser_false() {
        User user = new User();
        user.setEmail(TEST_EMAIL);
        user.setPassword(PASSWORD);

        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(new User()));

        boolean isSaved = userService.addUser(user);

        assertFalse(isSaved);
        verify(userRepository, times(1)).findByEmail(TEST_EMAIL);
        verify(passwordEncoder, times(0)).encode(PASSWORD);
        verify(userRepository, times(0)).save(user);
        verifyNoMoreInteractions(userRepository, passwordEncoder);
    }

    @Test
    public void updateUser_tryToUpdateProfile_actualUserIsChanged() {
        User actualUser = new User();
        actualUser.setEmail(ACTUAL_EMAIL);
        actualUser.setFirstName(ACTUAL_FIRST_NAME);
        actualUser.setSecondName(ACTUAL_SECOND_NAME);

        User updateUser = new User();
        updateUser.setEmail(UPDATE_EMAIL);
        updateUser.setFirstName(UPDATE_FIRST_NAME);
        updateUser.setSecondName(UPDATE_SECOND_NAME);

        actualUser = userService.updateUser(actualUser, updateUser);

        assertEquals(updateUser, actualUser);
        verify(userRepository, times(1)).save(actualUser);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void updateUser_tryToUpdateProfile_actualUserIsNotChanged() {
        User actualUser = new User();
        actualUser.setEmail(ACTUAL_EMAIL);
        actualUser.setFirstName(ACTUAL_FIRST_NAME);
        actualUser.setSecondName(ACTUAL_SECOND_NAME);

        User updateUser = new User();
        updateUser.setEmail(ACTUAL_EMAIL);
        updateUser.setFirstName(ACTUAL_FIRST_NAME);
        updateUser.setSecondName(ACTUAL_SECOND_NAME);

        actualUser = userService.updateUser(actualUser, updateUser);

        assertEquals(updateUser, actualUser);
        verify(userRepository, times(0)).save(actualUser);
        verifyNoMoreInteractions(userRepository);
    }
}