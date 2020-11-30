package by.bntu.borzdyko.carmax.service;

import by.bntu.borzdyko.carmax.model.description.Brand;
import by.bntu.borzdyko.carmax.model.description.Color;
import by.bntu.borzdyko.carmax.repository.ColorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ColorService {

    private final ColorRepository colorRepository;

    @Autowired
    public ColorService(ColorRepository colorRepository) {
        this.colorRepository = colorRepository;
    }

    public Color getOne(Long id) {
        return colorRepository.getOne(id);
    }

    public List<Color> getAll() {
        return colorRepository.findAll();
    }
}
