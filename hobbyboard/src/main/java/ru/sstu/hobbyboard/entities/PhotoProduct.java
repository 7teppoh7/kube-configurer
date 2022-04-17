package ru.sstu.hobbyboard.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class PhotoProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ToString.Exclude
    @OneToOne
    private Product product;

    @NotNull
    private String photo;

    public PhotoProduct(Product product, @NotNull String photo) {
        this.product = product;
        this.photo = photo;
    }

}
