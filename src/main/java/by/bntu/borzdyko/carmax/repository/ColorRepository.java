package by.bntu.borzdyko.carmax.repository;

import by.bntu.borzdyko.carmax.model.description.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColorRepository extends JpaRepository<Color, Long> {
}
