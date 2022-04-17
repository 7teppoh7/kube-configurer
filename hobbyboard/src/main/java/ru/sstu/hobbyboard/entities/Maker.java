package ru.sstu.hobbyboard.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sstu.hobbyboard.entities.dto.MakerDTO;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Maker {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    private String name;

    private String logo;

    private String description;

    public Maker(MakerDTO makerDto) {
        this.name = makerDto.getName();
        this.description = makerDto.getDescription();
    }
}
