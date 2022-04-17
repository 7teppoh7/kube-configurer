package ru.sstu.hobbyboard.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.sstu.hobbyboard.entities.Product;

import java.util.List;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer>, JpaSpecificationExecutor<Product> {
    List<Product> findAllByName(String name);

    @Query(value = "SELECT min(price) FROM Product")
    Float minPrice();

    @Query(value = "SELECT max(price) FROM Product")
    Float maxPrice();

    @Query(value = "SELECT max(maxPlayers) FROM Product")
    Integer maxMaxPlayers();

    @Query(value = "SELECT max(maxTime) FROM Product")
    Integer maxMaxTime();

    Integer countProductByMakerId(Integer id);
}
