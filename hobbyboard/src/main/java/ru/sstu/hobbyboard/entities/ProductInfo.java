package ru.sstu.hobbyboard.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ProductInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToOne(fetch = FetchType.EAGER)
    private Product product;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private User creator;

    @ManyToOne(fetch = FetchType.EAGER)
    private User updater;

    private Date createDate = Date.valueOf(LocalDate.now());

    private Date updateDate = Date.valueOf(LocalDate.now());

    public ProductInfo(@NotNull Product product, @NotNull User creator, User updater) {
        this.product = product;
        this.creator = creator;
        this.updater = updater;
    }
}
