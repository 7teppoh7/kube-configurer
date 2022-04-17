package ru.sstu.hobbyboard.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.sstu.hobbyboard.entities.PhotoProduct;

public interface PhotoProductRepository extends PagingAndSortingRepository<PhotoProduct, Integer> {
}
