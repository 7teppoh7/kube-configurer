package ru.sstu.hobbyboard.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.sstu.hobbyboard.entities.Maker;

public interface MakerRepository extends PagingAndSortingRepository<Maker, Integer> {
}
