package ru.sstu.hobbyboard.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.sstu.hobbyboard.entities.ProductInfo;

public interface ProductInfoRepository extends PagingAndSortingRepository<ProductInfo, Integer> {
}
