package ru.sstu.hobbyboard.entities;

import lombok.*;
import ru.sstu.hobbyboard.entities.dto.CategoryDTO;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    private String name;

    private String description;

    @Transient
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Product> products;

    public Category(CategoryDTO categoryDto) {
        this.name = categoryDto.getName();
        this.description = categoryDto.getDescription();
    }
}
