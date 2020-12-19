package by.bntu.borzdyko.carmax.security;

import by.bntu.borzdyko.carmax.model.User;
import by.bntu.borzdyko.carmax.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class UserDetailsServiceImplTest {

    private static final String EMAIL = "user@test.com";

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserDetailsServiceImpl userDetailsService;

    @Test
    public void loadUserByUsername_tryWithNormalEmail_user() {
        User commonUser = User.builder().email(EMAIL).build();
        UserDetails expected = new SecurityUser(commonUser);

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(commonUser));

        UserDetails actual = userDetailsService.loadUserByUsername(EMAIL);

        assertEquals(expected, actual);
        verify(userRepository, times(1)).findByEmail(EMAIL);
        verifyNoMoreInteractions(userRepository);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsername_tryWithEmailNotInDatabase_UsernameNotFoundException() {
        UserDetails expected = new SecurityUser();

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        UserDetails actual = userDetailsService.loadUserByUsername(EMAIL);

        assertEquals(expected, actual);
        verify(userRepository, times(1)).findByEmail(EMAIL);
        verifyNoMoreInteractions(userRepository);
    }
}