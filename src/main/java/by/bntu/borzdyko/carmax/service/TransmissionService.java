package by.bntu.borzdyko.carmax.service;

import by.bntu.borzdyko.carmax.model.description.Transmission;
import by.bntu.borzdyko.carmax.repository.TransmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TransmissionService {

    private final TransmissionRepository transmissionRepository;

    @Autowired
    public TransmissionService(TransmissionRepository transmissionRepository) {
        this.transmissionRepository = transmissionRepository;
    }

    public Transmission findOne(Long id) {
        return transmissionRepository.getOne(id);
    }

    public List<Transmission> findAll() {
        return transmissionRepository.findAll();
    }
}
