package ru.sstu.hobbyboard.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.sstu.hobbyboard.entities.Basket;

import java.util.Set;

public interface BasketRepository extends PagingAndSortingRepository<Basket, Integer> {
    Set<Basket> findBasketsByUserId(Integer id);
}
