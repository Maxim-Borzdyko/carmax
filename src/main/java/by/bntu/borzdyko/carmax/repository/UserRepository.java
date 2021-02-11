package by.bntu.borzdyko.carmax.repository;

import by.bntu.borzdyko.carmax.model.Role;
import by.bntu.borzdyko.carmax.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Page<User> findAllByRole(Role role, Pageable pageable);
}
