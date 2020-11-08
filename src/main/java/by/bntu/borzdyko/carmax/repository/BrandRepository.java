package by.bntu.borzdyko.carmax.repository;

import by.bntu.borzdyko.carmax.model.description.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
}
