package by.bntu.borzdyko.carmax.service;

import by.bntu.borzdyko.carmax.model.Role;
import by.bntu.borzdyko.carmax.model.User;
import by.bntu.borzdyko.carmax.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    private static final boolean ACTIVE = true;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findOne(Long id) {
        return userRepository.getOne(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public List<User> findAllByRole(Role role) {
        return userRepository.findAllByRole(role);
    }

    public boolean addUser(User user) {
        boolean isSaved = false;

        if (userRepository.findByEmail(user.getEmail()).isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setStatus(ACTIVE);
            user.setRole(Role.USER);
            userRepository.save(user);
            isSaved = true;
        }
        return isSaved;
    }

    public User updateUser(User actualUser, User updateUser) {
        boolean isChanged = !actualUser.getEmail().equals(updateUser.getEmail())
                || !actualUser.getFirstName().equals(updateUser.getFirstName())
                || !actualUser.getSecondName().equals(updateUser.getSecondName());

        if (isChanged) {
            actualUser.setEmail(updateUser.getEmail());
            actualUser.setFirstName(updateUser.getFirstName());
            actualUser.setSecondName(updateUser.getSecondName());

            userRepository.save(actualUser);
        }

        return actualUser;
    }

}
