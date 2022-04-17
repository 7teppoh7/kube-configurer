package ru.sstu.hobbyboard.servicies;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.sstu.hobbyboard.entities.AgeCategory;
import ru.sstu.hobbyboard.entities.Category;
import ru.sstu.hobbyboard.entities.Product;
import ru.sstu.hobbyboard.entities.dto.SearchDTO;
import ru.sstu.hobbyboard.repositories.ProductRepository;
import ru.sstu.hobbyboard.servicies.specification.ProductSpecification;
import ru.sstu.hobbyboard.servicies.specification.SearchCriteria;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private final CategoryService categoryService;
    private final MakerService makerService;
    private final ProductRepository productRepository;

    public ProductService(CategoryService categoryService, MakerService makerService, ProductRepository productRepository) {
        this.categoryService = categoryService;
        this.makerService = makerService;
        this.productRepository = productRepository;
    }

    public Iterable<Product> findAll() {
        return productRepository.findAll();
    }

    public Page<Product> findAllPageable(PageRequest of) {
        return productRepository.findAll(of);
    }

    public List<Product> findAllByName(String name) {
        return productRepository.findAllByName(name);
    }

    public Product findById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }

    public void delete(Product product){
        productRepository.delete(product);
    }

    public boolean validateProduct(Product product) {
        if (product.getMinPlayers() > product.getMaxPlayers()) return false;
        return product.getMinTime() <= product.getMinTime();
    }

    public AgeCategory getAgeCategory(Integer age) {
        if (age < 13) return AgeCategory.G;
        if (age < 17) return AgeCategory.PG;
        if (age < 18) return AgeCategory.R;
        return AgeCategory.X;
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public List<Product> findAllBySearchDto(SearchDTO searchDTO) {
        List<ProductSpecification> specs = new ArrayList<>();
        if (searchDTO.getMinPrice() != null && searchDTO.getMaxPrice() != null) {
            SearchCriteria searchCriteria = new SearchCriteria("price", "b", searchDTO.getMinPrice());
            searchCriteria.setSecondValue(searchDTO.getMaxPrice());
            specs.add(new ProductSpecification(searchCriteria));
        } else if (searchDTO.getMinPrice() != null) {
            specs.add(new ProductSpecification(new SearchCriteria("price", ">", searchDTO.getMinPrice())));
        } else if (searchDTO.getMaxPrice() != null)
            specs.add(new ProductSpecification(new SearchCriteria("price", "<", searchDTO.getMaxPrice())));

        if (searchDTO.getMinPlayers() != null && searchDTO.getMaxPlayers() != null) {
            SearchCriteria searchCriteria = new SearchCriteria("minPlayers", "d", searchDTO.getMinPlayers());
            searchCriteria.setSecondKey("maxPlayers");
            searchCriteria.setSecondValue(searchDTO.getMaxPlayers());
            specs.add(new ProductSpecification(searchCriteria));
        } else if (searchDTO.getMinPlayers() != null) {
            specs.add(new ProductSpecification(new SearchCriteria("minPlayers", ">", searchDTO.getMinPlayers())));
        } else if (searchDTO.getMaxPlayers() != null)
            specs.add(new ProductSpecification(new SearchCriteria("maxPlayers", "<", searchDTO.getMaxPlayers())));

        if (searchDTO.getMinTime() != null && searchDTO.getMaxTime() != null) {
            SearchCriteria searchCriteria = new SearchCriteria("minTime", "d", searchDTO.getMinTime());
            searchCriteria.setSecondKey("maxTime");
            searchCriteria.setSecondValue(searchDTO.getMaxTime());
            specs.add(new ProductSpecification(searchCriteria));
        } else if (searchDTO.getMinTime() != null)
            specs.add(new ProductSpecification(new SearchCriteria("minTime", ">", searchDTO.getMinTime())));
        else if (searchDTO.getMaxTime() != null)
            specs.add(new ProductSpecification(new SearchCriteria("maxTime", "<", searchDTO.getMaxTime())));

        if (searchDTO.getMinAge() != null && searchDTO.getMaxAge() != null) {
            SearchCriteria searchCriteria = new SearchCriteria("age", "b", searchDTO.getMinAge());
            searchCriteria.setSecondValue(searchDTO.getMaxAge());
            specs.add(new ProductSpecification(searchCriteria));
        } else if (searchDTO.getMinAge() != null)
            specs.add(new ProductSpecification(new SearchCriteria("age", ">", searchDTO.getMinAge())));
        else if (searchDTO.getMaxAge() != null)
            specs.add(new ProductSpecification(new SearchCriteria("age", "<", searchDTO.getMaxAge())));

        if (searchDTO.getIsDependence() != null)
            specs.add(new ProductSpecification(new SearchCriteria("isIndependent", ":", !searchDTO.getIsDependence())));

        if (searchDTO.getAgeCategory() != null) {
            AgeCategory value;
            try {
                value = AgeCategory.valueOf(searchDTO.getAgeCategory());
            } catch (IllegalArgumentException ignored) {
                value = null;
            }
            if (value != null)
                specs.add(new ProductSpecification(new SearchCriteria("ageCategory", ":", value)));
        }

        if (searchDTO.getSearch() != null) {
            SearchCriteria searchCriteria = new SearchCriteria("name", "?", searchDTO.getSearch());
            searchCriteria.setSecondKey("originalName");
            specs.add(new ProductSpecification(searchCriteria));
        }
        if (searchDTO.getRate() != null)
            specs.add(new ProductSpecification(new SearchCriteria("rate", ">", searchDTO.getRate())));

        Specification<Product> specification;
        if (specs.size() > 0) {
            specification = Specification.where(specs.get(0));
            if (specs.size() > 1) {
                for (int i = 1; i < specs.size(); i++) {
                    specification = specification.and(specs.get(i));
                }
            }
        } else specification = null;

        String sortName = "default";
        if (searchDTO.getSort() != null) sortName = searchDTO.getSort();
        Sort sort;
        switch (sortName) {
            case "priceD":
                sort = Sort.by(Sort.Direction.DESC, "price");
                break;
            case "priceA":
                sort = Sort.by(Sort.Direction.ASC, "price");
                break;
            case "rateD":
                sort = Sort.by(Sort.Direction.DESC, "rate");
                break;
            case "rateA":
                sort = Sort.by(Sort.Direction.ASC, "rate");
                break;
            case "dateD":
                sort = Sort.by(Sort.Direction.DESC, "delDate");
                break;
            case "dateA":
                sort = Sort.by(Sort.Direction.ASC, "delDate");
                break;
            default:
                sort = Sort.unsorted();
        }

        List<Product> result = new ArrayList<>();

        List<Product> specProducts = productRepository.findAll(specification, sort);

        if (searchDTO.getCategory() != null && searchDTO.getMaker() != null) {
            for (Product p : specProducts) {
                if (p.getCategories().stream().anyMatch((x) -> searchDTO.getCategory().contains(x)) && (searchDTO.getMaker().contains(p.getMaker())))
                    result.add(p);
            }
            return result;
        } else if (searchDTO.getCategory() != null) {
            for (Product p : specProducts) {
                if (p.getCategories().stream().anyMatch((x) -> searchDTO.getCategory().contains(x)))
                    result.add(p);
            }
            return result;
        } else if (searchDTO.getMaker() != null) {
            for (Product p : specProducts) {
                if (searchDTO.getMaker().contains(p.getMaker()))
                    result.add(p);
            }
            return result;
        }
        return specProducts;
    }

    public Float minPrice() {
        return productRepository.minPrice();
    }

    public Float maxPrice() {
        return productRepository.maxPrice();
    }

    public Integer maxMaxPlayers() {
        return productRepository.maxMaxPlayers();
    }

    public Integer maxMaxTime() {
        return productRepository.maxMaxTime();
    }

    public void deleteById(Integer product) {
        productRepository.deleteById(product);
    }

    public Integer countProductByMakerId(Integer id) {
        return productRepository.countProductByMakerId(id);
    }
}
