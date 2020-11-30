package by.bntu.borzdyko.carmax.service;

import by.bntu.borzdyko.carmax.model.description.Brand;
import by.bntu.borzdyko.carmax.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BrandService {

    private final BrandRepository brandRepository;

    @Autowired
    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public Brand getOne(Long id) {
        return brandRepository.getOne(id);
    }

    public List<Brand> getAll() {
        return brandRepository.findAll();
    }

    public boolean isBrandEmpty(Brand brand) {
        boolean isEmpty = true;

        if(brand ==  null) {
            throw new NullPointerException("Brand is null");
        } else if (brand.getId() != null && brand.getName() != null && brand.getCars() != null) {
            isEmpty = false;
        }

        return isEmpty;
    }

    public boolean isBrandNotInDatabase(Brand brand) {
        if (isBrandEmpty(brand)) {
            throw new NullPointerException("Brand is empty!");
        }
        return brandRepository.findByName(brand.getName()).isEmpty();
    }

    public boolean addBrand(Brand brand) {
        boolean isSaved = false;

        if (brand != null && !isBrandEmpty(brand) && isBrandNotInDatabase(brand)) {
            brandRepository.save(brand);
            isSaved = true;
        }

        return isSaved;
    }

}
