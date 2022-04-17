package ru.sstu.hobbyboard.servicies;

import org.springframework.stereotype.Service;
import ru.sstu.hobbyboard.entities.Category;
import ru.sstu.hobbyboard.repositories.CategoryRepository;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Iterable<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public Integer countProducts(Integer categoryId) {
        return categoryRepository.countProducts(categoryId);
    }
}
