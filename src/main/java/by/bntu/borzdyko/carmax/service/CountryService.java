package by.bntu.borzdyko.carmax.service;

import by.bntu.borzdyko.carmax.model.description.Brand;
import by.bntu.borzdyko.carmax.model.description.Country;
import by.bntu.borzdyko.carmax.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CountryService {

    private final CountryRepository countryRepository;

    @Autowired
    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public Country getOne(Long id) {
        return countryRepository.getOne(id);
    }

    public List<Country> getAll() {
        return countryRepository.findAll();
    }
}
