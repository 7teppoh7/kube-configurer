package ru.sstu.hobbyboard.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private User user;

    private Date purDate = Date.valueOf(LocalDate.now());

    private Float bonusPaid = 0F;

    private Float bonusGet = 0F;

    @NotNull
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private String address;

    @NotNull
    private Float price;

    @NotNull
    private Float total;

    @Enumerated(EnumType.STRING)
    private State state;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<PurchaseComposition> compositions;


}
