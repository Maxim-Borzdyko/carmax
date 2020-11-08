package by.bntu.borzdyko.carmax.repository;

import by.bntu.borzdyko.carmax.model.description.Transmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransmissionRepository extends JpaRepository<Transmission, Long> {
}
