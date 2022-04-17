package ru.sstu.hobbyboard.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.sstu.hobbyboard.entities.Purchase;

import java.util.List;

public interface PurchaseRepository extends PagingAndSortingRepository<Purchase, Integer> {
    List<Purchase> findPurchasesByUserId(Integer id);

    Integer countByUserId(Integer id);
}
