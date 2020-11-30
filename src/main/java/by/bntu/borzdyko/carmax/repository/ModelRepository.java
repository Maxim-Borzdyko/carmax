package by.bntu.borzdyko.carmax.repository;

import by.bntu.borzdyko.carmax.model.description.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelRepository extends JpaRepository<Model, Long> {
}
