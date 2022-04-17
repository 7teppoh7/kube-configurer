package ru.sstu.hobbyboard.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewPurchaseDTO {

    @NotBlank
    private String address;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotNull
    @Size(min = 11, max = 12) //TODO: regex?
    private String phone;

    public NewPurchaseDTO(@NotBlank String firstName, @NotBlank String lastName, @NotBlank String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }
}
