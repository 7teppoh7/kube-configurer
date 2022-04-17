package ru.sstu.hobbyboard.servicies;

import org.springframework.stereotype.Service;
import ru.sstu.hobbyboard.repositories.ProductInfoRepository;

@Service
public class ProductInfoService {

    private final ProductInfoRepository productInfoRepository;

    public ProductInfoService(ProductInfoRepository productInfoRepository) {
        this.productInfoRepository = productInfoRepository;
    }
}
