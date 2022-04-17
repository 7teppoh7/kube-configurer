package ru.sstu.hobbyboard.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.sstu.hobbyboard.entities.dto.ProductDTO;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    private String name;

    @NotNull
    private String originalName;

    @NotNull
    private Float price;

    @NotNull
    private Integer count;

    @Max(100)
    private Byte discount = 0;

    private Date releaseYear = Date.valueOf(LocalDate.now());

    @ManyToOne(fetch = FetchType.EAGER)
    private Maker maker;

    private Date delDate = Date.valueOf(LocalDate.now());

    @Min(0)
    @Max(18)
    private Integer age = 0;

    @Enumerated(EnumType.STRING)
    private AgeCategory ageCategory;

    private Integer minTime;

    private Integer maxTime;

    private Integer minPlayers = 1;

    @NotNull
    private Integer maxPlayers;

    private String code;

    @ManyToOne(fetch = FetchType.EAGER)
    private Product original;

    private Boolean isIndependent = true;

    private String description;

    private Float rate = 0F;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private PhotoProduct mainPhoto;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "product_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<PhotoProduct> photos;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Category> categories;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "product", cascade = {CascadeType.ALL})
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private ProductInfo productInfo;

    public static Product fromDto(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setOriginalName(productDTO.getOriginalName());
        product.setReleaseYear(productDTO.getDateRelease());
        product.setMaker(productDTO.getMaker());
        product.setPrice(productDTO.getPrice());
        product.setAge(productDTO.getAge());
        product.setMinTime(productDTO.getMinTime());
        product.setMaxTime(productDTO.getMaxTime());
        product.setMinPlayers(productDTO.getMaxPlayers());
        product.setMaxPlayers(productDTO.getMaxPlayers());
        product.setOriginal(productDTO.getOriginal());
        product.setIsIndependent(productDTO.getOriginal() == null);
        product.setCategories(productDTO.getCategories());
        product.setCount(productDTO.getCount());
        product.setDescription(productDTO.getDescription());
        return product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) &&
                Objects.equals(name, product.name) &&
                Objects.equals(originalName, product.originalName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, originalName);
    }
}
