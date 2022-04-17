package ru.sstu.kubeconfigurerui.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sstu.kubeconfigurerui.entity.Category;
import ru.sstu.kubeconfigurerui.entity.Maker;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchDTO {

    @Min(0)
    private Float minPrice;

    @Min(0)
    private Float maxPrice;

    @Min(1)
    private Integer minPlayers;

    @Min(1)
    private Integer maxPlayers;

    @Min(0)
    @Max(18)
    private Integer minAge;

    @Min(0)
    @Max(18)
    private Integer maxAge;

    private String ageCategory;

    @Min(0)
    @Max(5)
    private Double rate;

    @Min(5)
    private Integer minTime;

    @Min(5)
    private Integer maxTime;

    private List<Category> category;

    private List<Maker> maker;

    private Boolean isDependence;

    private String sort;

    private String search;
}
