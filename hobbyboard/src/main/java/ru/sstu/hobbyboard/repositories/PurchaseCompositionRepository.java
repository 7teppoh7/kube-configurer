package ru.sstu.hobbyboard.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.sstu.hobbyboard.entities.PurchaseComposition;

public interface PurchaseCompositionRepository extends PagingAndSortingRepository<PurchaseComposition, Integer> {
}
