package ru.sstu.hobbyboard.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.sstu.hobbyboard.entities.Category;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer> {
    @Query(value = "SELECT count(product_id) FROM product_categories WHERE categories_id = ?1", nativeQuery = true)
    Integer countProducts(Integer id);
}
