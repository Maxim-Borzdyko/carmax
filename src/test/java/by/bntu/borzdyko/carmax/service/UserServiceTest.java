package by.bntu.borzdyko.carmax.service;

import by.bntu.borzdyko.carmax.model.Role;
import by.bntu.borzdyko.carmax.model.User;
import by.bntu.borzdyko.carmax.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
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

    private static final int PAGE = 0;
    private static final Pageable PAGEABLE = PageRequest.of(PAGE, 6);

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    public void addUser_tryToSaveUser_true() {
        User user = User.builder().email(TEST_EMAIL).password(PASSWORD).build();

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
        User user = User.builder().email(TEST_EMAIL).password(PASSWORD).build();

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
        User actualUser = User.builder()
                .id(1L)
                .email(ACTUAL_EMAIL)
                .firstName(ACTUAL_FIRST_NAME)
                .secondName(ACTUAL_SECOND_NAME).build();

        User updateUser = User.builder()
                .id(1L)
                .email(UPDATE_EMAIL)
                .firstName(UPDATE_FIRST_NAME)
                .secondName(UPDATE_SECOND_NAME).build();

        when(userRepository.save(actualUser)).thenReturn(actualUser);

        actualUser = userService.updateUser(actualUser, updateUser);

        assertEquals(updateUser, actualUser);
        verify(userRepository, times(1)).save(actualUser);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void updateUser_tryToUpdateProfile_actualUserIsNotChanged() {
        User actualUser = User.builder()
                .email(ACTUAL_EMAIL)
                .firstName(ACTUAL_FIRST_NAME)
                .secondName(ACTUAL_SECOND_NAME).build();

        User updateUser = User.builder()
                .email(ACTUAL_EMAIL)
                .firstName(ACTUAL_FIRST_NAME)
                .secondName(ACTUAL_SECOND_NAME).build();

        actualUser = userService.updateUser(actualUser, updateUser);

        assertEquals(updateUser, actualUser);
        verify(userRepository, times(0)).save(actualUser);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void findOne_checkWithIdInDatabase_user() {
        Long id = 1L;
        User expected = User.builder().id(id).build();

        when(userRepository.getOne(id)).thenReturn(expected);

        User actual = userService.findOne(id);

        assertEquals(expected, actual);
        verify(userRepository, times(1)).getOne(id);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void findOne_checkWithIdNotInDatabase_null() {
        Long id = 10L;
        User expected = null;

        User actual = userService.findOne(id);

        assertEquals(expected, actual);
        verify(userRepository, times(1)).getOne(id);
        verifyNoMoreInteractions(userRepository);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findOne_checkWithIdIsNull_IllegalArgumentException() {
        Long id = null;

        when(userRepository.getOne(id)).thenThrow(new IllegalArgumentException());

        User actual = userService.findOne(id);
        verify(userRepository, times(1)).getOne(id);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void findAll_usersExistsInDatabase_user() {
        List<User> expected = List.of(new User(), new User());

        when(userRepository.findAll()).thenReturn(expected);

        List<User> actual = userService.findAll();

        assertArrayEquals(expected.toArray(), actual.toArray());
        verify(userRepository, times(1)).findAll();
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void findAll_ordersNotExistsInDatabase_emptyList() {
        List<User> expected = List.of();

        when(userRepository.findAll()).thenReturn(expected);

        List<User> actual = userService.findAll();

        assertArrayEquals(expected.toArray(), actual.toArray());
        verify(userRepository, times(1)).findAll();
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void findAllByRole_findByNormalRole_users() {
        Role role = Role.USER;
        Page<User> expected = new PageImpl<>(List.of(User.builder().role(Role.USER).build(), User.builder().role(Role.USER).build()));

        when(userRepository.findAllByRole(role, PAGEABLE)).thenReturn(expected);

        Page<User> actual = userService.findAllByRole(PAGE, role);

        assertArrayEquals(expected.toList().toArray(), actual.toList().toArray());
        verify(userRepository, times(1)).findAllByRole(role, PAGEABLE);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void findAllByRole_tryWithRoleNotInDatabase_emptyList() {
        Role role = Role.ADMIN;
        Page<User> expected = new PageImpl<>(List.of());

        when(userRepository.findAllByRole(role, PAGEABLE)).thenReturn(expected);

        Page<User> actual = userService.findAllByRole(PAGE, role);

        assertArrayEquals(expected.toList().toArray(), actual.toList().toArray());
        verify(userRepository, times(1)).findAllByRole(role, PAGEABLE);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void findAllByRole_tryWithRoleIsNull_emptyList() {
        Role role = null;
        Page<User> expected = new PageImpl<>(List.of());

        when(userRepository.findAllByRole(role, PAGEABLE)).thenReturn(expected);

        Page<User> actual = userService.findAllByRole(PAGE, role);

        assertArrayEquals(expected.toList().toArray(), actual.toList().toArray());
        verify(userRepository, times(1)).findAllByRole(role, PAGEABLE);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void save_checkWithNormalOrder_saved() {
        User user = new User();

        userService.save(user);

        verify(userRepository, times(1)).save(user);
        verifyNoMoreInteractions(userRepository);
    }

    @Test(expected = IllegalArgumentException.class)
    public void save_checkWithOrderIsNull_IllegalArgumentException() {
        User user = null;

        when(userRepository.save(user)).thenThrow(new IllegalArgumentException());

        userService.save(user);

        verify(userRepository, times(1)).save(user);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void delete_checkWithCorrectUser_deleted() {
        User user = new User();

        userService.delete(user);

        verify(userRepository, times(1)).delete(user);
        verifyNoMoreInteractions(userRepository);
    }
}