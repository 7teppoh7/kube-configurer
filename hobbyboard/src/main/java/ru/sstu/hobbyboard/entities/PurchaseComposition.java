package ru.sstu.hobbyboard.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseComposition {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Product product;

    private Float price;

    @Max(100)
    private Byte discount = 0;

    private Integer count = 1;
}
