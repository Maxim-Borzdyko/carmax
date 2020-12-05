package by.bntu.borzdyko.carmax.service;

import by.bntu.borzdyko.carmax.model.description.Fuel;
import by.bntu.borzdyko.carmax.repository.FuelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FuelService {

    private final FuelRepository fuelRepository;

    @Autowired
    public FuelService(FuelRepository fuelRepository) {
        this.fuelRepository = fuelRepository;
    }

    public Fuel findOne(Long id) {
        return fuelRepository.getOne(id);
    }

    public List<Fuel> findAll() {
        return fuelRepository.findAll();
    }
}
