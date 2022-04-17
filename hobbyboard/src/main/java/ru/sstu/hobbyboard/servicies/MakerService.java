package ru.sstu.hobbyboard.servicies;

import org.springframework.stereotype.Service;
import ru.sstu.hobbyboard.entities.Maker;
import ru.sstu.hobbyboard.repositories.MakerRepository;

@Service
public class MakerService {

    private final MakerRepository makerRepository;

    public MakerService(MakerRepository makerRepository) {
        this.makerRepository = makerRepository;
    }

    public Iterable<Maker> findAll() {
        return makerRepository.findAll();
    }

    public Maker save(Maker maker) {
        return makerRepository.save(maker);
    }
}
