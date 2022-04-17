package ru.sstu.hobbyboard.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Objects;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Basket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private Product product;

    private Integer count = 1;

    private Date addDate = Date.valueOf(LocalDate.now());

    public Basket(User user, Product product){
        this.user = user;
        this.product = product;
    }

    public Basket(User user, Product product, Integer count){
        this.user = user;
        this.product = product;
        this.count = count;
    }


    public Basket(Product product){
        this.product = product;
    }

    @Override
    public String toString() {
        return "Basket{" +
                "id=" + id +
                ", product=" + product.getId() + " " + product.getName() +
                ", count=" + count +
                ", addDate=" + addDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Basket basket = (Basket) o;
        return Objects.equals(product, basket.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product);
    }
}
