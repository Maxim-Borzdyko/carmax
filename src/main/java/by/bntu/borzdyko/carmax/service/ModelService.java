package by.bntu.borzdyko.carmax.service;

import by.bntu.borzdyko.carmax.model.description.Model;
import by.bntu.borzdyko.carmax.repository.ModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ModelService {

    private final ModelRepository modelRepository;

    @Autowired
    public ModelService(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

    public Model findOne(Long id) {
        return modelRepository.getOne(id);
    }

    public List<Model> findAll() {
        return modelRepository.findAll();
    }
}
