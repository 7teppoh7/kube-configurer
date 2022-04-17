package ru.sstu.hobbyboard.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private Purchase purchase;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private Product product;

    @Min(0)
    @Max(5)
    private Byte deliveryRate = 5;

    @Min(0)
    @Max(5)
    private Byte productRate = 5;

    @Min(0)
    @Max(5)
    private Byte siteRate = 5;

    private Boolean isApproved = false;

    @OneToMany(fetch = FetchType.EAGER)
    private Set<PhotoFeedback> photos;
}
