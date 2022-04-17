package ru.sstu.hobbyboard.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import ru.sstu.hobbyboard.entities.Category;
import ru.sstu.hobbyboard.entities.Maker;
import ru.sstu.hobbyboard.entities.Product;
import ru.sstu.hobbyboard.entities.dto.interfaces.Imageable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO implements Imageable {

    @NotNull
    private String name;

    @NotNull
    private String originalName;

    @NotNull
    private Date dateRelease;

    @NotNull
    private Maker maker;

    @NotNull
    private Float price = 0F;

    @NotNull
    @Min(0)
    @Max(18)
    private Integer age = 0;

    @NotNull
    @Min(5)
    private Integer minTime = 5;

    @NotNull
    private Integer maxTime = 5;

    @NotNull
    @Min(1)
    private Integer minPlayers = 1;

    @NotNull
    private Integer maxPlayers = 1;

    private Product original;

    private Integer count = 1;

    @NotNull
    private Boolean isDependence = false;

    private List<MultipartFile> photos;

    private Set<Category> categories;

    private String description;

    public ProductDTO(List<MultipartFile> photos) {
        this.photos = photos;
    }

    public ProductDTO(Product product){
        this.name = product.getName();
        this.originalName = product.getOriginalName();
        this.dateRelease = product.getReleaseYear();
        this.maker = product.getMaker();
        this.price = product.getPrice();
        this.age = product.getAge();
        this.minTime = product.getMinTime();
        this.maxTime = product.getMaxTime();
        this.minPlayers = product.getMinPlayers();
        this.maxPlayers = product.getMaxPlayers();
        this.original = product.getOriginal();
        this.count = product.getCount();
        this.isDependence = !product.getIsIndependent();
        this.categories = product.getCategories();
        this.description = product.getDescription();
    }

    @Override
    public String getDefaultImage() {
        return "product.jpg";
    }

    @Override
    public List<MultipartFile> getFiles() {
        return photos;
    }
}
