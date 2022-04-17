package ru.sstu.hobbyboard.servicies;

import org.springframework.stereotype.Service;
import ru.sstu.hobbyboard.entities.PhotoProduct;
import ru.sstu.hobbyboard.repositories.PhotoProductRepository;

@Service
public class PhotoProductService {

    private final PhotoProductRepository photoProductRepository;

    public PhotoProductService(PhotoProductRepository photoProductRepository) {
        this.photoProductRepository = photoProductRepository;
    }

    public Iterable<PhotoProduct> saveAll(Iterable<PhotoProduct> photoProducts) {
        return photoProductRepository.saveAll(photoProducts);
    }
}
