package ru.sstu.hobbyboard.servicies.specification;


import org.springframework.data.jpa.domain.Specification;
import ru.sstu.hobbyboard.entities.AgeCategory;
import ru.sstu.hobbyboard.entities.Product;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ProductSpecification implements Specification<Product> {

    private final SearchCriteria criteria;

    public ProductSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate
            (Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (criteria.getOperation().equalsIgnoreCase(">")) {
            System.out.println(criteria.getValue());
            return builder.greaterThanOrEqualTo(
                    root.get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equalsIgnoreCase("<")) {
            return builder.lessThanOrEqualTo(
                    root.get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equalsIgnoreCase("b")) {
            if (root.get(criteria.getKey()).getJavaType() == Float.class) {
                return builder.between(
                        root.get(criteria.getKey()), (Float) criteria.getValue(),
                        (Float) criteria.getSecondValue());
            } else if (root.get(criteria.getKey()).getJavaType() == Integer.class) {
                return builder.between(
                        root.get(criteria.getKey()), (Integer) criteria.getValue(),
                        (Integer) criteria.getSecondValue());
            }
        } else if (criteria.getOperation().equalsIgnoreCase("d")) {
            return builder.and(
                    builder.greaterThanOrEqualTo(
                            root.get(criteria.getKey()), criteria.getValue().toString()),
                    builder.lessThanOrEqualTo(
                            root.get(criteria.getSecondKey()), criteria.getSecondValue().toString()));
        } else if (criteria.getOperation().equalsIgnoreCase(":")) {

            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return builder.like(
                        root.<String>get(criteria.getKey()), "%" + criteria.getValue() + "%");
            } else if (root.get(criteria.getKey()).getJavaType() == Boolean.class) {
                return builder.equal(root.<Boolean>get(criteria.getKey()), criteria.getValue());
            } else if (root.get(criteria.getKey()).getJavaType() == AgeCategory.class) {
                return builder.equal(root.<AgeCategory>get(criteria.getKey()), criteria.getValue());
            }
        } else if (criteria.getOperation().equalsIgnoreCase("?")) {
            return builder.or(
                    builder.like(
                            root.<String>get(criteria.getKey()), "%" + criteria.getValue() + "%"),
                    builder.like(
                            root.<String>get(criteria.getSecondKey()), "%" + criteria.getValue() + "%"));
        }
        return null;
    }
}